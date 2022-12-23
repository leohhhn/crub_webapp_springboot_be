package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Machine;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
    public Machine findMachineByMachineId(Long machineId);
    public Machine findMachinesByCreatedById(Long createdById);
}
