package net.softloaf.automatchic.history.query.model;

import jakarta.persistence.*;
import lombok.*;
import net.softloaf.automatchic.common.enums.EvaluationType;
import net.softloaf.automatchic.common.enums.GradingType;
import net.softloaf.automatchic.common.enums.EventType;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`progress_history`")
public class ProgressHistoryEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "score_delta")
    private Double scoreDelta;

    @Column(name = "weight_delta")
    private Double weightDelta;

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
}
