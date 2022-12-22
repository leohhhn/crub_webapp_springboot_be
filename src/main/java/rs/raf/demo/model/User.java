package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
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
    @ColumnDefault(value = "1")
    private Integer perm_read = 1;
    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer perm_update = 0;
    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer perm_delete = 0;
    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer perm_create = 0;

    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer perm_mach_create = 0;
    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer perm_mach_destroy = 0;
    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer perm_mach_start = 0;
    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer perm_mach_stop = 0;
    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer perm_mach_restart = 0;
    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer perm_mach_search = 0;


    // GETTERS & SETTERS
    public Integer getPerm_mach_create() {
        return perm_mach_create;
    }

    public void setPerm_mach_create(Integer perm_mach_create) {
        this.perm_mach_create = perm_mach_create;
    }

    public Integer getPerm_mach_destroy() {
        return perm_mach_destroy;
    }

    public void setPerm_mach_destroy(Integer perm_mach_destroy) {
        this.perm_mach_destroy = perm_mach_destroy;
    }

    public Integer getPerm_mach_start() {
        return perm_mach_start;
    }

    public void setPerm_mach_start(Integer perm_mach_start) {
        this.perm_mach_start = perm_mach_start;
    }

    public Integer getPerm_mach_stop() {
        return perm_mach_stop;
    }

    public void setPerm_mach_stop(Integer perm_mach_stop) {
        this.perm_mach_stop = perm_mach_stop;
    }

    public Integer getPerm_mach_restart() {
        return perm_mach_restart;
    }

    public void setPerm_mach_restart(Integer perm_mach_restart) {
        this.perm_mach_restart = perm_mach_restart;
    }

    public Integer getPerm_mach_search() {
        return perm_mach_search;
    }

    public void setPerm_mach_search(Integer perm_mach_search) {
        this.perm_mach_search = perm_mach_search;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPerm_read() {
        return perm_read;
    }

    public void setPerm_read(Integer perm_read) {
        this.perm_read = perm_read;
    }

    public Integer getPerm_update() {
        return perm_update;
    }

    public void setPerm_update(Integer perm_update) {
        this.perm_update = perm_update;
    }

    public Integer getPerm_delete() {
        return perm_delete;
    }

    public void setPerm_delete(Integer perm_delete) {
        this.perm_delete = perm_delete;
    }

    public Integer getPerm_create() {
        return perm_create;
    }

    public void setPerm_create(Integer perm_create) {
        this.perm_create = perm_create;
    }
}
