package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Err;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.ErrRepository;
import java.util.List;

@Service
public class ErrService {

    private final ErrRepository errRepository;

    @Autowired
    public ErrService( ErrRepository errRepository) {
        this.errRepository = errRepository;
    }

    public void create(Err e) {
        this.errRepository.save(e);
    }

    public void update(Err e) {
        this.errRepository.save(e);
    }

    public void delete(Err e) {
        this.errRepository.delete(e);
    }

    public List<Err> getAllErrs(User u) {
        return this.errRepository.findErrsByCreatedBy(u);
    }
}
