package net.softloaf.automatchic.history.command.model;

import net.softloaf.automatchic.common.enums.EvaluationType;
import net.softloaf.automatchic.common.enums.GradingType;
import net.softloaf.automatchic.history.command.*;
import net.softloaf.automatchic.history.event.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class ProgressAggregate {
    @AggregateIdentifier
    private Long subjectId;
    private Double totalScore;
    private Double totalWeight;
    private GradingType gradingType;
    private EvaluationType evaluationType;
    private Integer targetGrade;
    private Double gradingMax;
    private Double grading5;
    private Double grading4;
    private Double grading3;
    private Double gradingMin;

    protected ProgressAggregate() {
    }

    @CommandHandler
    public ProgressAggregate(CreateProgressCommand cmd) {
        if(cmd.totalScore() < 0) {
            throw new IllegalStateException("Score must stay not negative");
        }
        if(cmd.totalWeight() < 0) {
            throw new IllegalStateException("Weight must stay not negative");
        }
        AggregateLifecycle.apply(new ProgressCreatedEvent(
                cmd.subjectId(),
                cmd.totalScore(),
                cmd.totalWeight(),
                cmd.gradingType(),
                cmd.evaluationType(),
                cmd.targetGrade(),
                cmd.gradingMax(),
                cmd.grading5(),
                cmd.grading4(),
                cmd.grading3(),
                cmd.gradingMin(),
                cmd.timestamp()
        ));
    }

    @CommandHandler
    public void handle(UpdateScoreCommand cmd) {
        if(totalScore + cmd.scoreDelta() < 0) {
            throw new IllegalStateException("Score must stay not negative");
        }
        if(totalWeight + cmd.weightDelta() < 0) {
            throw new IllegalStateException("Weight must stay not negative");
        }
        AggregateLifecycle.apply(new ScoreUpdatedEvent(
                cmd.subjectId(),
                cmd.scoreDelta(),
                cmd.weightDelta(),
                cmd.timestamp()
        ));
    }

    @CommandHandler
    public void handle(UpdateTargetGradeCommand cmd) {
        if(cmd.targetGrade() < 0 || cmd.targetGrade() > 4) {
            throw new IllegalStateException("Target grade must be between 0 and 4");
        }
        AggregateLifecycle.apply(new TargetGradeUpdatedEvent(
                cmd.subjectId(),
                cmd.targetGrade(),
                cmd.timestamp()
        ));
    }

    @CommandHandler
    public void handle(UpdateGradingsCommand cmd) {
        if(cmd.gradingMin() > cmd.grading3() || cmd.grading3() > cmd.grading4() || cmd.grading4() > cmd.grading5() || cmd.grading5() > cmd.gradingMax()) {
            throw new IllegalStateException("Incorrect gradings");
        }
        AggregateLifecycle.apply(new GradingsUpdatedEvent(
                cmd.subjectId(),
                cmd.gradingMax(),
                cmd.grading5(),
                cmd.grading4(),
                cmd.grading3(),
                cmd.gradingMin(),
                cmd.timestamp()
        ));
    }

    @CommandHandler
    public void handle(UpdateGradingTypeCommand cmd) {
        AggregateLifecycle.apply(new GradingTypeUpdatedEvent(
                cmd.subjectId(),
                cmd.gradingType(),
                cmd.timestamp()
        ));
    }

    @CommandHandler
    public void handle(UpdateEvaluationTypeCommand cmd) {
        AggregateLifecycle.apply(new EvaluationTypeUpdatedEvent(
                cmd.subjectId(),
                cmd.evaluationType(),
                cmd.timestamp()
        ));
    }

    @CommandHandler
    public void handle(DeleteProgressCommand cmd) {
        AggregateLifecycle.apply(new ProgressDeletedEvent(
                cmd.subjectId(),
                cmd.timestamp()
        ));
    }

    @EventSourcingHandler
    public void on(ProgressCreatedEvent e) {
        this.subjectId = e.subjectId();
        this.totalScore = e.totalScore();
        this.totalWeight = e.totalWeight();
        this.gradingType = e.gradingType();
        this.evaluationType = e.evaluationType();
        this.targetGrade = e.targetGrade();
        this.gradingMax = e.gradingMax();
        this.grading5 = e.grading5();
        this.grading4 = e.grading4();
        this.grading3 = e.grading3();
        this.gradingMin = e.gradingMin();
    }

    @EventSourcingHandler
    public void on(ScoreUpdatedEvent e) {
        this.totalScore += e.scoreDelta();
        this.totalWeight += e.weightDelta();
    }

    @EventSourcingHandler
    public void on(TargetGradeUpdatedEvent e) {
        this.targetGrade = e.targetGrade();
    }

    @EventSourcingHandler
    public void on(GradingsUpdatedEvent e) {
        this.gradingMax = e.gradingMax();
        this.grading5 = e.grading5();
        this.grading4 = e.grading4();
        this.grading3 = e.grading3();
        this.gradingMin = e.gradingMin();
    }

    @EventSourcingHandler
    public void on(GradingTypeUpdatedEvent e) {
        this.gradingType = e.gradingType();
    }

    @EventSourcingHandler
    public void on(EvaluationTypeUpdatedEvent e) {
        this.evaluationType = e.evaluationType();
    }

    @EventSourcingHandler
    public void on(ProgressDeletedEvent e) {
        AggregateLifecycle.markDeleted();
    }
}
