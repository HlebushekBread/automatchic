package net.softloaf.automatchic.history.event;

import net.softloaf.automatchic.common.enums.GradingType;
import org.axonframework.serialization.Revision;

import java.time.Instant;

//@Revision("1")
public record GradingTypeUpdatedEvent(
        Long subjectId,
        GradingType gradingType,
        Instant timestamp
) {
}
