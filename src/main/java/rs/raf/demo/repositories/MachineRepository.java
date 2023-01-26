package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.MachineStatus;

import java.util.Date;
import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
    public Machine findMachineByMachineId(Long machineId);
    public List<Machine> findMachinesByCreatedById(Long createdById);
    public List<Machine> findMachinesByNameContainingIgnoreCase(String name);
    public List<Machine> findMachinesByCreatedOnAfter(Date createdOn);
    public List<Machine> findMachinesByCreatedOnBefore(Date createdOn);
    public List<Machine> findMachinesByStatus(MachineStatus status);


}
