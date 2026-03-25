package net.softloaf.automatchic.repository;

import net.softloaf.automatchic.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    long countBySubjectId(long subjectId);
}
