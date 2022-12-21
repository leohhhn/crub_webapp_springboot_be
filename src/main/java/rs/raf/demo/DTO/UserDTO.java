package rs.raf.demo.DTO;

import javax.validation.constraints.Email;

public class UserDTO {

    /* example DTO to be sent from FE
    *
        {
            "userId": 3,
            "username": "user2",
            "email": "user2@email.com",
            "perm_update": "0",
            "perm_read": "1",
            "perm_create": "0",
            "perm_delete": "1"
        }
    * */

    private Long userId;
    private String username;
    @Email
    private String email;
    private Integer perm_update;
    private Integer perm_delete;
    private Integer perm_create;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Integer getPerm_update() {
        return perm_update;
    }

    public Integer getPerm_delete() {
        return perm_delete;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getPerm_create() {
        return perm_create;
    }
}
