package rs.raf.demo.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Err;
import rs.raf.demo.model.User;
import rs.raf.demo.services.ErrService;
import rs.raf.demo.services.UserService;
import rs.raf.demo.utils.JwtUtil;

import java.util.List;

@RestController
@RequestMapping("/errs")
@CrossOrigin
public class ErrController {

    private final ErrService errService;
    private UserService userService;
    private final JwtUtil jwtUtil;

    public ErrController(ErrService errService, UserService userService, JwtUtil jwtUtil) {
        this.errService = errService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<Err> getAllErrs(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        String jwt = auth.split(" ")[1];
        try {
            User sender = this.userService.findByUsername(jwtUtil.extractUsername(jwt));
            return this.errService.getAllErrs(sender);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
