package net.softloaf.automatchic.history.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.Instant;

public record UpdateGradingsCommand(
        @TargetAggregateIdentifier Long subjectId,
        Double gradingMax,
        Double grading5,
        Double grading4,
        Double grading3,
        Double gradingMin,
        Instant timestamp
) {
}
