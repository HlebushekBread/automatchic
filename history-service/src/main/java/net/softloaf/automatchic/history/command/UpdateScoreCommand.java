package net.softloaf.automatchic.history.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.Instant;

public record UpdateScoreCommand(
        @TargetAggregateIdentifier Long subjectId,
        Double scoreDelta,
        Double weightDelta,
        Instant timestamp
) {
}
