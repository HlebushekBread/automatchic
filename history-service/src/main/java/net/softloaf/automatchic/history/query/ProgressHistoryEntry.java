package net.softloaf.automatchic.history.query;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.softloaf.automatchic.common.enums.EvaluationType;
import net.softloaf.automatchic.common.enums.GradingType;
import net.softloaf.automatchic.common.enums.EventType;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "`progress_history`")
public class ProgressHistoryEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "total_score")
    private Double totalScore;

    @Column(name = "total_weight")
    private Double totalWeight;

    @Enumerated(EnumType.STRING)
    @Column(name = "grading_type")
    private GradingType gradingType;

    @Enumerated(EnumType.STRING)
    @Column(name = "evaluation_type")
    private EvaluationType evaluationType;

    @Column(name = "target_grade")
    private Integer targetGrade;

    @Column(name = "grading_max")
    private Double gradingMax;

    @Column(name = "grading_5")
    private Double grading5;

    @Column(name = "grading_4")
    private Double grading4;

    @Column(name = "grading_3")
    private Double grading3;

    @Column(name = "grading_min")
    private Double gradingMin;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private EventType eventType;

    @Column(name = "timestamp")
    private Instant timestamp;

    public ProgressHistoryEntry(ProgressView view, EventType eventType, Instant timestamp) {
        this.subjectId = view.getSubjectId();
        this.totalScore = view.getTotalScore();
        this.totalWeight = view.getTotalWeight();
        this.gradingType = view.getGradingType();
        this.evaluationType = view.getEvaluationType();
        this.targetGrade = view.getTargetGrade();
        this.gradingMax = view.getGradingMax();
        this.grading5 = view.getGrading5();
        this.grading4 = view.getGrading4();
        this.grading3 = view.getGrading3();
        this.gradingMin = view.getGradingMin();
        this.eventType = eventType;
        this.timestamp = timestamp;
    }
}
