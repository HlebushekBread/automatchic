package net.softloaf.automatchic.app.repository;

import net.softloaf.automatchic.app.model.Publicity;
import net.softloaf.automatchic.app.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByPublicity(Publicity publicity);
    List<Subject> findAllByUserId(long userId);
    long countByUserId(long userId);
}
