package rs.raf.demo.controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.DTO.UserDTO;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.User;
import rs.raf.demo.services.UserService;
import rs.raf.demo.utils.JwtUtil;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> newUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @Valid @RequestBody User user) {
        String jwt = auth.split(" ")[1];
        Claims perm = jwtUtil.extractAllClaims(jwt);

        try {
            if (perm.get("p_create").equals(0)) return ResponseEntity.status(403).build();
            this.userService.create(user);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return new ResponseEntity<>("A user with this email or username already exists.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/all")
    public List<User> all() {
        return this.userService.getAllUsers();
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody UserDTO dto) {
        String jwt = auth.split(" ")[1];
        Claims perm = jwtUtil.extractAllClaims(jwt);

        try {
            if (perm.get("p_update").equals(0)) return ResponseEntity.status(403).build();

            User u = this.userService.findById(dto.getUserId()); // get user
            if (u == null) return new ResponseEntity<>("User with specified ID does not exist", HttpStatus.BAD_REQUEST);

            u.setEmail(dto.getEmail());
            u.setP_create(dto.getP_create());
            u.setP_update(dto.getP_update());
            u.setP_delete(dto.getP_delete());
            u.setUsername(dto.getUsername());

            this.userService.update(u);
            return ResponseEntity.ok().build();

        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return new ResponseEntity<>("A user with provided email or username already exists.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody UserDTO dto) {
        String jwt = auth.split(" ")[1];
        Claims perm = jwtUtil.extractAllClaims(jwt);

        try {
            if (perm.get("p_delete").equals(0)) return ResponseEntity.status(403).build();

            if (jwtUtil.extractUsername(jwt).equals(dto.getUsername()))
                return new ResponseEntity<>("Cannot remove yourself!", HttpStatus.BAD_REQUEST);

            // todo see if can be implemented better: only checks ID from DTO, could be trouble
            User u = this.userService.findById(dto.getUserId()); // get user
            if (u == null) return new ResponseEntity<>("User with specified ID does not exist", HttpStatus.BAD_REQUEST);

            this.userService.delete(u);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("USER CONTROLLER::DELETE: FAILED", HttpStatus.BAD_REQUEST);
        }
    }

    /// Returns machines that the currently logged in user created
    /// todo update it to fetch macines by userId requestparam?
    @GetMapping(value = "/getMachines")
    public List<Machine> allMachines(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        String jwt = auth.split(" ")[1];
        try {
            User sender = this.userService.findByUsername(jwtUtil.extractUsername(jwt));
            return this.userService.getMachines(sender);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
//    public User me() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        return this.userService.findByUsername(username);
//    }

    //    @GetMapping(value = "/all")
//    public Page<User> all(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
//        return this.userService.paginate(page, size);
//    }
}