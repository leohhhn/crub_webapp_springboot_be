package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long machineId;

    @Column(nullable = false)
//    @ColumnDefault(value = "0")
    private MachineStatus status = MachineStatus.RUNNING;
    // todo figure out how enums are written in hibernate. ??

    @ManyToOne
    @JsonIgnore
    private User createdBy;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    boolean active = false;
    private Long createdById;

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public Long getMachineId() {
        return machineId;
    }

    public MachineStatus getStatus() {
        return status;
    }

    public void setStatus(MachineStatus status) {
        this.status = status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
