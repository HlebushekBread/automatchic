package net.softloaf.automatchic.history.event;

import net.softloaf.automatchic.common.enums.EvaluationType;
import net.softloaf.automatchic.common.enums.GradingType;

import java.time.Instant;

//@Revision("1")
public record ProgressCreatedEvent(
        Long subjectId,
        Double totalScore,
        Double totalWeight,
        GradingType gradingType,
        EvaluationType evaluationType,
        Integer targetGrade,
        Double gradingMax,
        Double grading5,
        Double grading4,
        Double grading3,
        Double gradingMin,
        Instant timestamp
) {
}
