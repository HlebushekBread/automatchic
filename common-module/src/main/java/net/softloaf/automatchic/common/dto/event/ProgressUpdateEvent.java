package net.softloaf.automatchic.common.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.softloaf.automatchic.common.enums.EvaluationType;
import net.softloaf.automatchic.common.enums.EventType;
import net.softloaf.automatchic.common.enums.GradingType;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgressUpdateEvent {
    private Long subjectId;

    private Double scoreDelta;
    private Double weightDelta;

    private GradingType gradingType;
    private EvaluationType evaluationType;

    private Integer targetGrade;

    private Double gradingMax;
    private Double grading5;
    private Double grading4;
    private Double grading3;
    private Double gradingMin;

    private EventType eventType;
    private Instant timestamp;
}
