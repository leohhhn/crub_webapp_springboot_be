package rs.raf.demo.controllers;

import com.sun.xml.bind.v2.runtime.output.SAXOutput;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.DTO.MachineDTO;
import rs.raf.demo.DTO.SearchMachineDTO;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.MachineStatus;
import rs.raf.demo.model.User;
import rs.raf.demo.requests.ScheduleRequest;
import rs.raf.demo.services.MachineService;
import rs.raf.demo.services.UserService;
import rs.raf.demo.utils.JwtUtil;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/machines")
@CrossOrigin
public class MachineController {

    private final JwtUtil jwtUtil;
    private final MachineService machineService;
    private final UserService userService;

    private TaskScheduler taskScheduler;

    @Autowired
    public MachineController(MachineService machineService, JwtUtil jwtUtil, UserService userService, TaskScheduler taskScheduler) {
        this.jwtUtil = jwtUtil;
        this.machineService = machineService;
        this.userService = userService;
        this.taskScheduler = taskScheduler;
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
            User caller = this.userService.findByUsername(jwtUtil.extractUsername(jwt));
            if (!jwtUtil.hasPermission(jwt, "pm_create")) return ResponseEntity.status(403).build();

            this.machineService.create(machineDTO, caller);
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
            if (!jwtUtil.hasPermission(jwt, "pm_destroy")) return ResponseEntity.status(403).build();
            User caller = this.userService.findByUsername(jwtUtil.extractUsername(jwt));
            Machine m = this.machineService.findById(machineDTO.getMachineId());

            if (!this.machineService.checkIfMachineIsUsers(m, caller)) return ResponseEntity.status(403).build();
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
        Machine m = this.machineService.findById(machineDTO.getMachineId());

        if (!jwtUtil.hasPermission(jwt, "pm_start")) return ResponseEntity.status(403).build();
        User caller = this.userService.findByUsername(jwtUtil.extractUsername(jwt));
        if (!this.machineService.checkIfMachineIsUsers(m, caller)) return ResponseEntity.status(403).build();

        if (m.getStatus() != MachineStatus.STOPPED)
            return new ResponseEntity<>("MACHINE CONTROLLER::STARTMACHINE: MACHINE ALREADY BOOTING", HttpStatus.BAD_REQUEST);

        this.machineService.startMachine(m);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "stop")
    public ResponseEntity<?> stopMachine(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody MachineDTO machineDTO) {
        String jwt = auth.split(" ")[1];

        User caller = this.userService.findByUsername(jwtUtil.extractUsername(jwt));
        Machine m = this.machineService.findById(machineDTO.getMachineId());

        if (!jwtUtil.hasPermission(jwt, "pm_stop")) return ResponseEntity.status(403).build();
        if (!this.machineService.checkIfMachineIsUsers(m, caller)) return ResponseEntity.status(403).build();

        if (m.getStatus() != MachineStatus.RUNNING) {
            return new ResponseEntity<>("MACHINE CONTROLLER::STOPMACHINE: Machine must be running to stop", HttpStatus.BAD_REQUEST);
        }

        this.machineService.stopMachine(m);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "restart")
    public ResponseEntity<?> restartMachine(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody MachineDTO machineDTO) {
        String jwt = auth.split(" ")[1];
        User caller = this.userService.findByUsername(jwtUtil.extractUsername(jwt));
        Machine m = this.machineService.findById(machineDTO.getMachineId());

        if (!jwtUtil.hasPermission(jwt, "pm_restart")) return ResponseEntity.status(403).build();
        if (!this.machineService.checkIfMachineIsUsers(m, caller)) return ResponseEntity.status(403).build();

        if (m.getStatus() != MachineStatus.RUNNING)
            return new ResponseEntity<>("MACHINE CONTROLLER::RESTART MACHINE: MACHINE NOT RUNNING", HttpStatus.BAD_REQUEST);

        this.machineService.restartMachine(m.getMachineId());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "search")
    public ResponseEntity<?> searchMachines(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody SearchMachineDTO searchMachineDTO) {
        String jwt = auth.split(" ")[1];
        User caller = this.userService.findByUsername(jwtUtil.extractUsername(jwt));
        try {
            if (!jwtUtil.hasPermission(jwt, "pm_search")) return ResponseEntity.status(403).build();
            return new ResponseEntity<>(this.machineService.searchMachines(searchMachineDTO, caller), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("MACHINE CONTROLLER::SEARCH: ERROR", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "schedule")
    public ResponseEntity<?> scheduleAction(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody ScheduleRequest scheduleRequest) {
        String jwt = auth.split(" ")[1];
        User caller = this.userService.findByUsername(jwtUtil.extractUsername(jwt));
        Machine m = this.machineService.findById(scheduleRequest.getMachineId());
        System.out.println(m);

        if (!this.machineService.checkIfMachineIsUsers(m, caller)) return ResponseEntity.status(403).build();

        switch (scheduleRequest.getAction()) {
            case "start":
                if (!jwtUtil.hasPermission(jwt, "pm_start")) return ResponseEntity.status(403).build();
                System.out.println("scheduling machine ".concat(scheduleRequest.getMachineId().toString()).concat(" to " + scheduleRequest.getAction()));
                System.out.println("at ".concat(scheduleRequest.getWhen().toString()));

                this.taskScheduler.schedule(() -> {
                    this.machineService.wStartMachine(m, caller);
                }, scheduleRequest.getWhen());

                return ResponseEntity.ok().build();

            case "stop":
                if (!jwtUtil.hasPermission(jwt, "pm_stop")) return ResponseEntity.status(403).build();
                System.out.println("scheduling machine ".concat(scheduleRequest.getMachineId().toString()).concat(" to " + scheduleRequest.getAction()));
                System.out.println("at: ".concat(scheduleRequest.getWhen().toString()));

                this.taskScheduler.schedule(() -> {
                    this.machineService.wStopMachine(m, caller);
                }, scheduleRequest.getWhen());

                return ResponseEntity.ok().build();

            case "restart":
                if (!jwtUtil.hasPermission(jwt, "pm_restart")) return ResponseEntity.status(403).build();
                System.out.println("scheduling machine ".concat(scheduleRequest.getMachineId().toString()).concat(" to " + scheduleRequest.getAction()));

                return ResponseEntity.ok().build();
            default:
                return new ResponseEntity<>("MACHINE CONTROLLER::SCHEDULE ACTION: NON-EXISTENT ACTION", HttpStatus.BAD_REQUEST);
        }
    }
}