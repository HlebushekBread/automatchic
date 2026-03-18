package net.breadlab.automatchic.repository;

import net.breadlab.automatchic.model.Publicity;
import net.breadlab.automatchic.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByPublicity(Publicity publicity);
    List<Subject> findAllByUserId(long userId);
}
