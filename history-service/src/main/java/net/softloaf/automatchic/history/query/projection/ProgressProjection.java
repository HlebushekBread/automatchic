package net.softloaf.automatchic.history.query.projection;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.common.enums.EventType;
import net.softloaf.automatchic.history.event.*;
import net.softloaf.automatchic.history.query.ProgressHistoryEntry;
import net.softloaf.automatchic.history.query.ProgressHistoryRepository;
import net.softloaf.automatchic.history.query.ProgressView;
import net.softloaf.automatchic.history.query.ProgressViewRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@RequiredArgsConstructor
@Component
public class ProgressProjection {
    private final ProgressViewRepository progressViewRepository;
    private final ProgressHistoryRepository progressHistoryRepository;

    @Transactional
    @EventHandler
    public void on(ProgressCreatedEvent e) {
        ProgressView view = new ProgressView(e);
        progressViewRepository.save(view);
        progressHistoryRepository.save(new ProgressHistoryEntry(view, EventType.CREATE, e.timestamp()));
    }

    @Transactional
    @EventHandler
    public void on(ScoreUpdatedEvent e) {
        ProgressView view = progressViewRepository.findById(e.subjectId()).orElseThrow(EntityNotFoundException::new);
        view.setTotalScore(view.getTotalScore() + e.scoreDelta());
        view.setTotalWeight(view.getTotalWeight() + e.weightDelta());
        progressViewRepository.save(view);

        progressHistoryRepository.save(new ProgressHistoryEntry(view, EventType.SCORE_UPDATE, e.timestamp()));
    }

    @Transactional
    @EventHandler
    public void on(TargetGradeUpdatedEvent e) {
        ProgressView view = progressViewRepository.findById(e.subjectId()).orElseThrow(EntityNotFoundException::new);
        view.setTargetGrade(e.targetGrade());
        progressViewRepository.save(view);

        progressHistoryRepository.save(new ProgressHistoryEntry(view, EventType.TARGET_GRADE_UPDATE, e.timestamp()));
    }

    @Transactional
    @EventHandler
    public void on(GradingsUpdatedEvent e) {
        ProgressView view = progressViewRepository.findById(e.subjectId()).orElseThrow(EntityNotFoundException::new);
        view.setGradingMax(e.gradingMax());
        view.setGrading5(e.grading5());
        view.setGrading4(e.grading4());
        view.setGrading3(e.grading3());
        view.setGradingMin(e.gradingMin());
        progressViewRepository.save(view);

        progressHistoryRepository.save(new ProgressHistoryEntry(view, EventType.GRADINGS_UPDATE, e.timestamp()));
    }

    @Transactional
    @EventHandler
    public void on(GradingTypeUpdatedEvent e) {
        ProgressView view = progressViewRepository.findById(e.subjectId()).orElseThrow(EntityNotFoundException::new);
        view.setGradingType(e.gradingType());
        progressViewRepository.save(view);

        progressHistoryRepository.save(new ProgressHistoryEntry(view, EventType.GRADING_TYPE_UPDATE, e.timestamp()));
    }

    @Transactional
    @EventHandler
    public void on(EvaluationTypeUpdatedEvent e) {
        ProgressView view = progressViewRepository.findById(e.subjectId()).orElseThrow(EntityNotFoundException::new);
        view.setEvaluationType(e.evaluationType());
        progressViewRepository.save(view);

        progressHistoryRepository.save(new ProgressHistoryEntry(view, EventType.EVALUATION_TYPE_UPDATE, e.timestamp()));
    }

    @Transactional
    @EventHandler
    public void on(ProgressDeletedEvent e) {
        progressViewRepository.deleteById(e.subjectId());
        //progressHistoryRepository.deleteAllBySubjectId(e.subjectId());
    }
}
