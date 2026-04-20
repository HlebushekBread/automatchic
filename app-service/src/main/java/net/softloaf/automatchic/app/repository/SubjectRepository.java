package net.softloaf.automatchic.app.repository;

import net.softloaf.automatchic.common.enums.GradingType;
import net.softloaf.automatchic.app.model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByUserId(long userId);
    long countByUserId(long userId);

    @Query("SELECT s FROM Subject s " +
            "JOIN FETCH s.user " +
            "WHERE s.publicity = 'PUBLIC' " +
            "AND (s.searchString LIKE CONCAT('%', :query, '%') " +
            "OR s.user.searchString LIKE CONCAT('%', :query, '%')) " +
            "AND (:gradingType IS NULL OR s.gradingType = :gradingType)")
    Page<Subject> findPublicSubjects(String query, GradingType gradingType, Pageable pageable);
}
