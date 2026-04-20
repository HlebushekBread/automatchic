package net.softloaf.automatchic.history.command;

import net.softloaf.automatchic.common.enums.EvaluationType;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.Instant;

public record DeleteProgressCommand(
        @TargetAggregateIdentifier Long subjectId,
        Instant timestamp
) {
}
