package rs.raf.demo.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.*;
import rs.raf.demo.repositories.*;

@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BootstrapData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Loading Data...");

        for (int i = 1; i < 3; i++) {
            User user = new User();
            String t = "user".concat(String.valueOf(i));
            user.setUsername(t);
            user.setPassword(this.passwordEncoder.encode(t));
            user.setP_read(1);
            user.setEmail(t.concat("@gmail.com"));
            this.userRepository.save(user);
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(this.passwordEncoder.encode("admin"));
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
        admin.setEmail("admin@gmail.com");
        this.userRepository.save(admin);

        User mod = new User();
        mod.setUsername("mod");
        mod.setPassword(this.passwordEncoder.encode("mod"));
        mod.setP_delete(1);
        mod.setP_update(0);
        mod.setP_create(1);
        mod.setP_read(1);
        mod.setEmail("mod@gmail.com");
        this.userRepository.save(mod);

        System.out.println("Data loaded!");
    }
}
