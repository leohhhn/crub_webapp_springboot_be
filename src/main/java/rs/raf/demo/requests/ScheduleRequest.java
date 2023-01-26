package rs.raf.demo.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ScheduleRequest {
    private Long machineId;
    private String action;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm", timezone = "Europe/Belgrade") // example string: 24-01-2023 22:45
    private Date when;
}
