package net.softloaf.automatchic.history.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressViewRepository extends JpaRepository<ProgressView, Long> {
}
