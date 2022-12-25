package rs.raf.demo.controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.DTO.MachineDTO;
import rs.raf.demo.DTO.UserDTO;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.MachineStatus;
import rs.raf.demo.model.User;
import rs.raf.demo.services.MachineService;
import rs.raf.demo.services.UserService;
import rs.raf.demo.utils.JwtUtil;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Date;
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
    public ResponseEntity<?> newMachine(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody MachineDTO machineDTO) {
        String jwt = auth.split(" ")[1];
        Claims perm = jwtUtil.extractAllClaims(jwt);

        try {
            if (perm.get("pm_create").equals(0)) return ResponseEntity.status(403).build();

            User creator = this.userService.findByUsername(jwtUtil.extractUsername(jwt));
            Machine m = new Machine();

            m.setActive(true);
            m.setName(machineDTO.getName());
            m.setCreatedBy(creator);
            m.setCreatedOn(Date.from(Instant.now()));
            m.setStatus(MachineStatus.STOPPED);
            m.setCreatedById(creator.getUserId());

            this.machineService.create(m);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("MACHINE CONTROLLER:: CREATE FAILED", HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping(value = "/destroy")
    public ResponseEntity<?> destroyMachine(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody MachineDTO machineDTO) {
        String jwt = auth.split(" ")[1];
        Claims perm = jwtUtil.extractAllClaims(jwt);

        System.out.println("RECEIVED DESTROY REQUEST");
        try {
            if (perm.get("pm_destroy").equals(0)) return ResponseEntity.status(403).build();


            Machine m = this.machineService.findById(machineDTO.getMachineId()); // get user
            if (m == null) return new ResponseEntity<>("Machine doesn't exist", HttpStatus.BAD_REQUEST);

            this.machineService.delete(m);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("MACHINE CONTROLLER::DESTROY: FAILED", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/all")
    public List<Machine> all(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        String jwt = auth.split(" ")[1];
        Claims perm = jwtUtil.extractAllClaims(jwt);
        User sender = this.userService.findByUsername(jwtUtil.extractUsername(jwt));

        return this.machineService.getAllMachines(sender.getUserId());
    }
}