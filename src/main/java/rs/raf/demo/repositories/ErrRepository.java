package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Err;
import rs.raf.demo.model.User;

import java.util.List;

@Repository
public interface ErrRepository extends JpaRepository<Err, Long> {
    public List<Err> findErrsByCreatedBy(User createdBy);
}
