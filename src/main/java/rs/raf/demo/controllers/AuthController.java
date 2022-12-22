package rs.raf.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.User;
import rs.raf.demo.requests.LoginRequest;
import rs.raf.demo.responses.LoginResponse;
import rs.raf.demo.services.UserService;
import rs.raf.demo.utils.JwtUtil;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }
        this.userService.loggedIn(loginRequest.getUsername());
        User loggedInUser = this.userService.findByUsername(loginRequest.getUsername());

        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(
                loginRequest.getUsername(),
                loggedInUser.getPerm_read(),
                loggedInUser.getPerm_create(),
                loggedInUser.getPerm_update(),
                loggedInUser.getPerm_delete(),
                loggedInUser.getPerm_mach_create(),
                loggedInUser.getPerm_mach_destroy(),
                loggedInUser.getPerm_mach_start(),
                loggedInUser.getPerm_mach_stop(),
                loggedInUser.getPerm_mach_restart(),
                loggedInUser.getPerm_mach_search()
        )));
    }
}
