package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    @NotBlank(message = "Username is mandatory")
    private String username;

    @Column
    @NotBlank(message = "Password is mandatory")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer p_create = 0;

    @Column(nullable = false)
    @ColumnDefault(value = "1")
    private Integer p_read = 1;

    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer p_update = 0;
    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer p_delete = 0;

    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer pm_create = 0;
    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer pm_destroy = 0;
    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer pm_start = 0;
    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer pm_stop = 0;
    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer pm_restart = 0;
    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer pm_search = 0;

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    List<Machine> machineList = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    List<Err> errorList = new ArrayList<>();

    // GETTERS & SETTERS
    public Integer getPm_create() {
        return pm_create;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPm_create(Integer perm_mach_create) {
        this.pm_create = perm_mach_create;
    }

    public Integer getPm_destroy() {
        return pm_destroy;
    }

    public void setPm_destroy(Integer perm_mach_destroy) {
        this.pm_destroy = perm_mach_destroy;
    }

    public Integer getPm_start() {
        return pm_start;
    }

    public void setPm_start(Integer perm_mach_start) {
        this.pm_start = perm_mach_start;
    }

    public Integer getPm_stop() {
        return pm_stop;
    }

    public void setPm_stop(Integer perm_mach_stop) {
        this.pm_stop = perm_mach_stop;
    }

    public Integer getPm_restart() {
        return pm_restart;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Machine> getMachineList() {
        return machineList;
    }

    public void setPm_restart(Integer perm_mach_restart) {
        this.pm_restart = perm_mach_restart;
    }

    public Integer getPm_search() {
        return pm_search;
    }

    public void setPm_search(Integer perm_mach_search) {
        this.pm_search = perm_mach_search;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getP_read() {
        return p_read;
    }

    public void setP_read(Integer perm_read) {
        this.p_read = perm_read;
    }

    public Integer getP_update() {
        return p_update;
    }

    public void setP_update(Integer perm_update) {
        this.p_update = perm_update;
    }

    public Integer getP_delete() {
        return p_delete;
    }

    public void setP_delete(Integer perm_delete) {
        this.p_delete = perm_delete;
    }

    public Integer getP_create() {
        return p_create;
    }

    public void setP_create(Integer perm_create) {
        this.p_create = perm_create;
    }

    public void setMachineList(List<Machine> machineList) {
        this.machineList = machineList;
    }

    public List<Err> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<Err> errorList) {
        this.errorList = errorList;
    }
}
