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

        for (int i = 1; i < 6; i++) {
            User user = new User();
            String t = "user".concat(String.valueOf(i));
            user.setUsername(t);
            user.setPassword(this.passwordEncoder.encode(t));
            user.setPerm_read(1);
            user.setEmail(t.concat("@gmail.com"));
            this.userRepository.save(user);
        }


        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(this.passwordEncoder.encode("admin"));
        admin.setPerm_delete(1);
        admin.setPerm_update(1);
        admin.setPerm_create(1);
        admin.setPerm_read(1);
        admin.setEmail("admin@gmail.com");
        this.userRepository.save(admin);

        User mod = new User();
        mod.setUsername("mod");
        mod.setPassword(this.passwordEncoder.encode("mod"));
        mod.setPerm_delete(1);
        mod.setPerm_update(0);
        mod.setPerm_create(1);
        mod.setPerm_read(1);
        mod.setEmail("mod@gmail.com");
        this.userRepository.save(mod);

        User updater = new User();
        updater.setUsername("updater");
        updater.setPassword(this.passwordEncoder.encode("updater"));
        updater.setPerm_delete(0);
        updater.setPerm_update(1);
        updater.setPerm_create(0);
        updater.setPerm_read(1);
        updater.setEmail("updater@gmail.com");
        this.userRepository.save(updater);

        User deleter = new User();
        deleter.setUsername("deleter");
        deleter.setPassword(this.passwordEncoder.encode("deleter"));
        deleter.setPerm_delete(1);
        deleter.setPerm_update(0);
        deleter.setPerm_create(0);
        deleter.setPerm_read(1);
        deleter.setEmail("deleter@gmail.com");
        this.userRepository.save(deleter);

        System.out.println("Data loaded!");
    }
}
