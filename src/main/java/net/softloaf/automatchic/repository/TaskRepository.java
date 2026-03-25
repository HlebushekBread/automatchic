package net.softloaf.automatchic.repository;

import net.softloaf.automatchic.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    long countBySubjectId(long subjectId);
}
