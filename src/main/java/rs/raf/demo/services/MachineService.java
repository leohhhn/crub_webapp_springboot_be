package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import rs.raf.demo.DTO.MachineDTO;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.MachineStatus;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.MachineRepository;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;

@Service
public class MachineService {

    private final MachineRepository machineRepository;

    @Autowired
    public MachineService(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
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

    @Async
    public void startMachine(Machine m) {
 // todo add locks
        try {
            m.setStatus(MachineStatus.BOOTING);
            this.machineRepository.save(m);

            Random rand = new Random();
            int time = 10000 + rand.nextInt(4500);
            Thread.sleep(time);

            m.setStatus(MachineStatus.RUNNING);
            this.machineRepository.save(m);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void stopMachine(Machine m) {

        try {
            m.setStatus(MachineStatus.SHUTDOWN);
            this.machineRepository.save(m);

            Random rand = new Random();
            int time = 10000 + rand.nextInt(4500);
            Thread.sleep(time);

            m.setStatus(MachineStatus.STOPPED);
            this.machineRepository.save(m);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Async
    public void restartMachine(Machine m) {
        try {
            // todo reimplement as per specification
            m.setStatus(MachineStatus.REBOOT);
            this.machineRepository.save(m);

            Random rand = new Random();
            int time = 10000 + rand.nextInt(4500);
            Thread.sleep(time);

            m.setStatus(MachineStatus.RUNNING);
            this.machineRepository.save(m);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Machine findMachine(MachineDTO machineDTO){
        return this.machineRepository.findMachineByMachineId(machineDTO.getMachineId());
    }

}
