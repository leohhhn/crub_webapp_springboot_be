package rs.raf.demo.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import rs.raf.demo.model.MachineStatus;

import java.util.Date;
import java.util.List;


public class SearchMachineDTO {
    private String name;
    private List<MachineStatus> statuses;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Europe/Belgrade")
    // example string: 24-01-2023
    private Date dateFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Europe/Belgrade")
    // example string: 24-01-2023
    private Date dateTo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MachineStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<MachineStatus> statuses) {
        this.statuses = statuses;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
}
