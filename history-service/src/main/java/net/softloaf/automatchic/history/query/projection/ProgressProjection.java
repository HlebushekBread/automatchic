package net.softloaf.automatchic.history.query.projection;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.common.enums.EventType;
import net.softloaf.automatchic.history.event.*;
import net.softloaf.automatchic.history.query.model.ProgressHistoryEntry;
import net.softloaf.automatchic.history.query.repository.ProgressHistoryRepository;
import net.softloaf.automatchic.history.query.model.ProgressView;
import net.softloaf.automatchic.history.query.repository.ProgressViewRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

        progressHistoryRepository.save(
                ProgressHistoryEntry.
                        builder().
                        subjectId(e.subjectId()).
                        scoreDelta(e.totalScore()).
                        weightDelta(e.totalWeight()).
                        gradingType(e.gradingType()).
                        evaluationType(e.evaluationType()).
                        targetGrade(e.targetGrade()).
                        gradingMax(e.gradingMax()).
                        grading5(e.grading5()).
                        grading4(e.grading4()).
                        grading3(e.grading3()).
                        gradingMin(e.gradingMin()).
                        eventType(EventType.CREATE).
                        timestamp(e.timestamp()).
                        build()
        );
    }

    @Transactional
    @EventHandler
    public void on(ScoreUpdatedEvent e) {
        ProgressView view = progressViewRepository.findById(e.subjectId()).orElseThrow(EntityNotFoundException::new);
        view.setTotalScore(view.getTotalScore() + e.scoreDelta());
        view.setTotalWeight(view.getTotalWeight() + e.weightDelta());
        progressViewRepository.save(view);

        progressHistoryRepository.save(
                ProgressHistoryEntry.
                        builder().
                        subjectId(e.subjectId()).
                        scoreDelta(e.scoreDelta()).
                        weightDelta(e.weightDelta()).
                        eventType(EventType.SCORE_UPDATE).
                        timestamp(e.timestamp()).
                        build()
        );
    }

    @Transactional
    @EventHandler
    public void on(TargetGradeUpdatedEvent e) {
        ProgressView view = progressViewRepository.findById(e.subjectId()).orElseThrow(EntityNotFoundException::new);
        view.setTargetGrade(e.targetGrade());
        progressViewRepository.save(view);

        progressHistoryRepository.save(
                ProgressHistoryEntry.
                        builder().
                        subjectId(e.subjectId()).
                        targetGrade(e.targetGrade()).
                        eventType(EventType.TARGET_GRADE_UPDATE).
                        timestamp(e.timestamp()).
                        build()
        );
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

        progressHistoryRepository.save(
                ProgressHistoryEntry.
                        builder().
                        subjectId(e.subjectId()).
                        gradingMax(e.gradingMax()).
                        grading5(e.grading5()).
                        grading4(e.grading4()).
                        grading3(e.grading3()).
                        gradingMin(e.gradingMin()).
                        eventType(EventType.GRADINGS_UPDATE).
                        timestamp(e.timestamp()).
                        build()
        );
    }

    @Transactional
    @EventHandler
    public void on(GradingTypeUpdatedEvent e) {
        ProgressView view = progressViewRepository.findById(e.subjectId()).orElseThrow(EntityNotFoundException::new);
        view.setGradingType(e.gradingType());
        progressViewRepository.save(view);

        progressHistoryRepository.save(
                ProgressHistoryEntry.
                        builder().
                        subjectId(e.subjectId()).
                        gradingType(e.gradingType()).
                        eventType(EventType.GRADING_TYPE_UPDATE).
                        timestamp(e.timestamp()).
                        build()
        );
    }

    @Transactional
    @EventHandler
    public void on(EvaluationTypeUpdatedEvent e) {
        ProgressView view = progressViewRepository.findById(e.subjectId()).orElseThrow(EntityNotFoundException::new);
        view.setEvaluationType(e.evaluationType());
        progressViewRepository.save(view);

        progressHistoryRepository.save(
                ProgressHistoryEntry.
                        builder().
                        subjectId(e.subjectId()).
                        evaluationType(e.evaluationType()).
                        eventType(EventType.EVALUATION_TYPE_UPDATE).
                        timestamp(e.timestamp()).
                        build()
        );
    }

    @Transactional
    @EventHandler
    public void on(ProgressDeletedEvent e) {
        progressViewRepository.deleteById(e.subjectId());
        //progressHistoryRepository.deleteAllBySubjectId(e.subjectId());
        progressHistoryRepository.save(
                ProgressHistoryEntry.
                        builder().
                        subjectId(e.subjectId()).
                        eventType(EventType.DELETE).
                        timestamp(e.timestamp()).
                        build()
        );
    }
}
