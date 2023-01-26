package rs.raf.demo.services;

import org.hibernate.tool.schema.SourceType;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import rs.raf.demo.DTO.MachineDTO;
import rs.raf.demo.DTO.SearchMachineDTO;
import rs.raf.demo.model.Err;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.MachineStatus;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.ErrRepository;
import rs.raf.demo.repositories.MachineRepository;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;

@Service
public class MachineService {

    private final MachineRepository machineRepository;
    private final ErrService errService;

    @Autowired
    public MachineService(MachineRepository machineRepository, ErrService errService) {
        this.machineRepository = machineRepository;
        this.errService = errService;
    }

    public void create(MachineDTO machineDTO, User creator) {
        Machine m = new Machine();
        m.setActive(true);
        m.setName(machineDTO.getName());
        m.setCreatedBy(creator);
        m.setCreatedOn(Date.from(Instant.now()));
        m.setStatus(MachineStatus.STOPPED);
        m.setCreatedById(creator.getUserId());

        this.machineRepository.save(m);
    }

    public void update(Machine mach) {
        this.machineRepository.save(mach);
    }

    public void delete(Machine mach) {
        this.machineRepository.delete(mach);
    }

    public Machine findById(Long machineId) {
        return this.machineRepository.findMachineByMachineId(machineId);
    }

    public List<Machine> getAllMachines(Long userId) {
        return this.machineRepository.findMachinesByCreatedById(userId);
    }

    // this is the function that you schedule
    public void wStartMachine(Machine m, User u) {
        System.out.println("Scheduled wStartMachine executing at: ".concat(new Date().toString()));
        if (m.getStatus() != MachineStatus.STOPPED) {
            Err e = new Err();
            e.setMessage("Could not start machine ".concat(m.getMachineId().toString()));
            e.setCreatedOn(new Date());
            e.setCreatedBy(u);
            this.errService.create(e);
            return;
        }
        startMachine(m);
    }

    // this is the function that you schedule
    public void wStopMachine(Machine m, User u) {
        System.out.println("Scheduled wStopMachine executing at: ".concat(new Date().toString()));

        if (m.getStatus() != MachineStatus.RUNNING) {
            Err e = new Err();
            e.setMessage("Could not stop machine".concat(m.getMachineId().toString()));
            e.setCreatedOn(new Date());
            e.setCreatedBy(u);
            this.errService.create(e);
            return;
        }
        stopMachine(m);
    }

    @Async
    public void startMachine(Machine m) {
        try {
            Random rand = new Random();
            int time = 10000 + rand.nextInt(4500);
            Thread.sleep(time);
            m.setStatus(MachineStatus.RUNNING);
            this.machineRepository.save(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ObjectOptimisticLockingFailureException e) {
            System.out.println("START MACHINE:: try again, locked lol");
        }
    }

    @Async
    public void stopMachine(Machine m) {
        try {
            Random rand = new Random();
            int time = 10000 + rand.nextInt(4500);
            Thread.sleep(time);

            m.setStatus(MachineStatus.STOPPED);
            this.machineRepository.save(m);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ObjectOptimisticLockingFailureException e) {
            System.out.println("STOP MACHINE:: try again, locked lol");
        }
    }

    @Async
    public void restartMachine(Long mId) {
        try {

            Machine m = this.machineRepository.getById(mId);

            Random rand = new Random();
            int time = 10000 + rand.nextInt(4500);
            Thread.sleep(time / 2);

            m.setStatus(MachineStatus.STOPPED);

            this.machineRepository.save(m);

            System.out.println("sleeping after stopping machine");

            Machine nv = this.machineRepository.getById(mId);
            Thread.sleep(time / 2);
            System.out.println("setting machine to running");
            nv.setStatus(MachineStatus.RUNNING);
            this.machineRepository.save(nv);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ObjectOptimisticLockingFailureException e) {
            System.out.println("RESTART MACHINE:: try again, locked lol");
        }
    }

    public Machine findMachine(MachineDTO machineDTO) {
        return this.machineRepository.findMachineByMachineId(machineDTO.getMachineId());
    }

    public List<Machine> searchMachines(SearchMachineDTO searchMachineDTO, User u) {
        List<Machine> machines = this.getAllMachines(u.getUserId());

        if (searchMachineDTO.getName() != null) {
            machines.retainAll(this.machineRepository.findMachinesByNameContainingIgnoreCase(searchMachineDTO.getName()));
        }
        if (searchMachineDTO.getDateFrom() != null) {
            machines.retainAll(this.machineRepository.findMachinesByCreatedOnAfter(searchMachineDTO.getDateFrom()));
        }
        if (searchMachineDTO.getDateTo() != null) {
            machines.retainAll(this.machineRepository.findMachinesByCreatedOnBefore(
                    new Date(searchMachineDTO.getDateTo().getTime() + (1000 * 60 * 60 * 24))));
        }

        if (searchMachineDTO.getStatuses().contains(MachineStatus.RUNNING) && !searchMachineDTO.getStatuses().contains(MachineStatus.STOPPED)) {
            List<Machine> t = this.machineRepository.findMachinesByStatus(MachineStatus.RUNNING);
            machines.retainAll(t);
        }
        if (!searchMachineDTO.getStatuses().contains(MachineStatus.RUNNING) && searchMachineDTO.getStatuses().contains(MachineStatus.STOPPED)) {
            List<Machine> t = this.machineRepository.findMachinesByStatus(MachineStatus.STOPPED);
            machines.retainAll(t);
        }

        return machines;
    }


}
