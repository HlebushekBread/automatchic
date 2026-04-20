package net.softloaf.automatchic.history.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.Instant;

public record UpdateTargetGradeCommand(
        @TargetAggregateIdentifier Long subjectId,
        Integer targetGrade,
        Instant timestamp
) {
}
