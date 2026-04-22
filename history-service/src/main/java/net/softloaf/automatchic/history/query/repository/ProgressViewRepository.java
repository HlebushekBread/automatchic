package net.softloaf.automatchic.history.query.repository;

import net.softloaf.automatchic.history.query.model.ProgressView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressViewRepository extends JpaRepository<ProgressView, Long> {
}
