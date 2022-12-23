package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.MachineRepository;

import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Service
public class MachineService {

    private final MachineRepository machineRepository;

    @Autowired
    public MachineService(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    public void create(Machine mach) {
        this.machineRepository.save(mach);
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

//    public User findById(Long machId) {
//        return this.machineRepository.findByUserId(userId);
//    }
//
//    public User findByEmail(@Email String email) {
//        return this.userRepository.findByEmail(email);
//    }
//
//    public User findByUsername(String username) {
//        return this.userRepository.findByUsername(username);
//    }

    public List<Machine> getAllMachines() {
        return this.machineRepository.findAll();
    }
}
