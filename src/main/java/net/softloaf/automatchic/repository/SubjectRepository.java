package net.softloaf.automatchic.repository;

import net.softloaf.automatchic.model.Publicity;
import net.softloaf.automatchic.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByPublicity(Publicity publicity);
    List<Subject> findAllByUserId(long userId);
    long countByUserId(long userId);
}
