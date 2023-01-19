package rs.raf.demo.controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
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
import java.util.concurrent.Future;

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

    @GetMapping(value = "/all")
    public List<Machine> all(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        String jwt = auth.split(" ")[1];
        User sender = this.userService.findByUsername(jwtUtil.extractUsername(jwt));
        return this.machineService.getAllMachines(sender.getUserId());
    }

    @PostMapping(value = "/find")
    public Machine findMachine(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody MachineDTO machineDTO) {
        // todo add permissions
        return this.machineService.findMachine(machineDTO);
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> newMachine(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody MachineDTO machineDTO) {
        String jwt = auth.split(" ")[1];

        try {
            if (jwtUtil.hasPermission(jwt, "pm_create")) return ResponseEntity.status(403).build();
            User creator = this.userService.findByUsername(jwtUtil.extractUsername(jwt));

            this.machineService.create(machineDTO, creator);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("MACHINE CONTROLLER:: CREATE FAILED", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/destroy")
    public ResponseEntity<?> destroyMachine(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody MachineDTO machineDTO) {
        String jwt = auth.split(" ")[1];

        try {
            if (jwtUtil.hasPermission(jwt, "pm_destroy"))return ResponseEntity.status(403).build();

            Machine m = this.machineService.findById(machineDTO.getMachineId());
            if (m == null) return new ResponseEntity<>("Machine doesn't exist", HttpStatus.BAD_REQUEST);

            this.machineService.delete(m);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("MACHINE CONTROLLER::DESTROY: FAILED", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "start")
    public ResponseEntity<?> startMachine(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody MachineDTO machineDTO) {
        String jwt = auth.split(" ")[1];

        if (jwtUtil.hasPermission(jwt, "pm_start")) return ResponseEntity.status(403).build();
        // todo add locks

        Machine m = this.machineService.findById(machineDTO.getMachineId());
        if (m.getStatus() != MachineStatus.STOPPED)
            return new ResponseEntity<>("MACHINE CONTROLLER::STARTMACHINE: MACHINE ALREADY BOOTING", HttpStatus.BAD_REQUEST);

        this.machineService.startMachine(m);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "stop")
    public ResponseEntity<?> stopMachine(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody MachineDTO machineDTO) {
        String jwt = auth.split(" ")[1];
        Claims perm = jwtUtil.extractAllClaims(jwt);
        if (jwtUtil.hasPermission(jwt, "pm_stop")) return ResponseEntity.status(403).build();

        Machine m = this.machineService.findById(machineDTO.getMachineId());
        if (m.getStatus() != MachineStatus.RUNNING)
            return new ResponseEntity<>("MACHINE CONTROLLER::STOPMACHINE: MACHINE ALREADY SHUTTING DOWN", HttpStatus.BAD_REQUEST);

        this.machineService.stopMachine(m);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "restartMachine")
    public ResponseEntity<?> restartMachine(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody MachineDTO machineDTO) {
        // todo implement restarting
        return null;
    }


}