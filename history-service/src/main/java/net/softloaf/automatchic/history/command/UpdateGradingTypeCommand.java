package net.softloaf.automatchic.history.command;

import net.softloaf.automatchic.common.enums.GradingType;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.Instant;

public record UpdateGradingTypeCommand(
        @TargetAggregateIdentifier Long subjectId,
        GradingType gradingType,
        Instant timestamp
) {
}
