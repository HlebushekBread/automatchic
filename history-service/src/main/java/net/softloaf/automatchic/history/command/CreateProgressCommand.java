package net.softloaf.automatchic.history.command;

import net.softloaf.automatchic.common.enums.EvaluationType;
import net.softloaf.automatchic.common.enums.GradingType;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.Instant;

public record CreateProgressCommand(
        @TargetAggregateIdentifier Long subjectId,
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
