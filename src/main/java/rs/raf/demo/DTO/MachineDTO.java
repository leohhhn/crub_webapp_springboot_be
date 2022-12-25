package rs.raf.demo.DTO;

import javax.validation.constraints.Email;

public class MachineDTO {
    private Long machineId;
    private String name;

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
