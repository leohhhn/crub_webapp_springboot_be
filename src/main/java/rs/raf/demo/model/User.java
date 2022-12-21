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
