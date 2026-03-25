package net.breadlab.automatchic.repository;

import net.breadlab.automatchic.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    long countBySubjectId(long subjectId);
}
