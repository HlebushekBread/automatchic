package net.softloaf.automatchic.history.query.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.softloaf.automatchic.common.enums.EvaluationType;
import net.softloaf.automatchic.common.enums.GradingType;
import net.softloaf.automatchic.history.event.ProgressCreatedEvent;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "`progress_view`")
public class ProgressView {
    @Id
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

    public ProgressView(ProgressCreatedEvent e) {
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
}
