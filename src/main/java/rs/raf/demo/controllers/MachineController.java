package rs.raf.demo.controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Machine;
import rs.raf.demo.services.MachineService;
import rs.raf.demo.services.UserService;
import rs.raf.demo.utils.JwtUtil;

import java.util.List;

@RestController
@RequestMapping("/machines")
@CrossOrigin
public class MachineController {

    private final JwtUtil jwtUtil;
    private final MachineService machineService;
    private final UserService userService;

    @Autowired
    public MachineController(MachineService machineService, JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.machineService = machineService;
        this.userService = userService;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> newMachine(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        String jwt = auth.split(" ")[1];
        Claims perm = jwtUtil.extractAllClaims(jwt);

        try {
            if (perm.get("pm_create").equals(0)) return ResponseEntity.status(403).build();
            Machine mach = new Machine();
            mach.setCreatedBy(this.userService.getUsernameFromJWT(jwt));
            mach.setCreatedById(this.userService.getUsernameFromJWT(jwt).getUserId());

            this.machineService.create(mach);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("MACHINE CONTROLLER:: CREATE FAILED", HttpStatus.BAD_REQUEST);
         }
    }

    @GetMapping(value = "/all")
    public List<Machine> all() {
        return this.machineService.getAllMachines();
    }
}