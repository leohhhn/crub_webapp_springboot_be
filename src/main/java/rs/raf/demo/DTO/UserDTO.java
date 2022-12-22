package rs.raf.demo.DTO;

import javax.validation.constraints.Email;

public class UserDTO {

    /* example DTO to be sent from FE
    *
        {
            "userId": 3,
            "username": "user2",
            "email": "user2@email.com",
            "p_create": "0",
            "p_read": "1",
            "p_update": "0",
            "p_delete": "1"
            "pm_create": "1",
            "pm_destroy": "1",
            "pm_start": "1"
            "pm_stop": "1",
            "pm_restart": "1",
            "pm_search": "1"
        }
    * */

    private Long userId;
    private String username;
    @Email
    private String email;
    private Integer p_update;
    private Integer p_delete;
    private Integer p_create;

    private Integer pm_create;
    private Integer pm_destroy;
    private Integer pm_start;
    private Integer pm_stop;
    private Integer pm_restart;
    private Integer pm_search;

    public String getUsername() {
        return username;
    }

    public Integer getPm_create() {
        return pm_create;
    }

    public void setPm_create(Integer pm_create) {
        this.pm_create = pm_create;
    }

    public Integer getPm_destroy() {
        return pm_destroy;
    }

    public void setPm_destroy(Integer pm_destroy) {
        this.pm_destroy = pm_destroy;
    }

    public Integer getPm_start() {
        return pm_start;
    }

    public void setPm_start(Integer pm_start) {
        this.pm_start = pm_start;
    }

    public Integer getPm_stop() {
        return pm_stop;
    }

    public void setPm_stop(Integer pm_stop) {
        this.pm_stop = pm_stop;
    }

    public Integer getPm_restart() {
        return pm_restart;
    }

    public void setPm_restart(Integer pm_restart) {
        this.pm_restart = pm_restart;
    }

    public Integer getPm_search() {
        return pm_search;
    }

    public void setPm_search(Integer pm_search) {
        this.pm_search = pm_search;
    }

    public String getEmail() {
        return email;
    }

    public Integer getP_update() {
        return p_update;
    }

    public Integer getP_delete() {
        return p_delete;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getP_create() {
        return p_create;
    }
}
