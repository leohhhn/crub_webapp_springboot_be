package rs.raf.demo.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.*;
import rs.raf.demo.repositories.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MachineRepository machineRepository;

    @Autowired
    public BootstrapData(UserRepository userRepository, MachineRepository machineRepository, PasswordEncoder passwordEncoder, MachineRepository machineRepository1) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.machineRepository = machineRepository1;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Loading Data...");

        User admin = new User();
        admin.setUsername("1");
        admin.setPassword(this.passwordEncoder.encode("1"));
        admin.setP_delete(1);
        admin.setP_update(1);
        admin.setP_create(1);
        admin.setP_read(1);
        admin.setPm_create(1);
        admin.setPm_destroy(1);
        admin.setPm_start(1);
        admin.setPm_stop(1);
        admin.setPm_restart(1);
        admin.setPm_search(1);
        admin.setEmail("1@gmail.com");
        this.userRepository.save(admin);

        for (int i = 1; i < 3; i++) {
            User user = new User();
            String t = "user".concat(String.valueOf(i));
            user.setUsername(t);
            user.setPassword(this.passwordEncoder.encode(t));
            user.setP_read(1);
            user.setEmail(t.concat("@gmail.com"));
            this.userRepository.save(user);
        }

        User mod = new User();
        mod.setUsername("mod");
        mod.setPassword(this.passwordEncoder.encode("mod"));
        mod.setP_delete(1);
        mod.setP_update(0);
        mod.setP_create(1);
        mod.setP_read(1);
        mod.setPm_create(1);
        mod.setPm_destroy(1);
        mod.setPm_start(1);
        mod.setPm_stop(1);
        mod.setPm_restart(1);
        mod.setPm_search(1);
        mod.setEmail("mod@gmail.com");
        this.userRepository.save(mod);

        for (int i = 1; i < 3; i++) {
            Machine m = new Machine();
            m.setActive(true);
            m.setName("mach".concat(String.valueOf(i)));
            m.setCreatedBy(admin);
            m.setCreatedOn(Date.from(Instant.now()));
            m.setStatus(MachineStatus.RUNNING);
            m.setCreatedById(admin.getUserId());
            this.machineRepository.save(m);
        }

        System.out.println("Data loaded!");
    }
}
