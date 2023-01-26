package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.utils.JwtUtil;

import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User myUser = this.findByUsername(username);
        if (myUser == null) {
            throw new UsernameNotFoundException("User name " + username + " not found");
        }
        return new org.springframework.security.core.userdetails.User(myUser.getUsername(), myUser.getPassword(), new ArrayList<>());
    }

    public void create(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
    }

    public void update(User user) {
        this.userRepository.save(user);
    }

    public void delete(User user) {
        this.userRepository.delete(user);
    }

    public Page<User> paginate(Integer page, Integer size) {
        return this.userRepository.findAll(PageRequest.of(page, size));
    }

    public void loggedIn(String username) {
        User user = this.userRepository.findByUsername(username);
        try {
            this.userRepository.save(user);
        } catch (ObjectOptimisticLockingFailureException exception) {
            this.loggedIn(username);
        }
    }

    public User findById(Long userId) {
        return this.userRepository.findByUserId(userId);
    }

    public User findByEmail(@Email String email) {
        return this.userRepository.findByEmail(email);
    }

    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public List<Machine> getMachines(User sender) {
        return sender.getMachineList();
    }

    public User getUsernameFromJWT(String jwt) {
        return this.userRepository.findByUsername(jwtUtil.extractUsername(jwt));
    }
}
