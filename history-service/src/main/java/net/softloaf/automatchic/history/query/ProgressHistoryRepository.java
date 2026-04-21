package net.softloaf.automatchic.history.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressHistoryRepository extends JpaRepository<ProgressHistoryEntry, Long> {
    List<ProgressHistoryEntry> findAllBySubjectIdOrderByTimestampAsc(long subjectId);
    void deleteAllBySubjectId(long subjectId);
}
