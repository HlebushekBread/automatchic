package net.softloaf.automatchic.history.event;

import net.softloaf.automatchic.common.enums.EvaluationType;
import org.axonframework.serialization.Revision;

import java.time.Instant;

//@Revision("1")
public record EvaluationTypeUpdatedEvent(
        Long subjectId,
        EvaluationType evaluationType,
        Instant timestamp
) {
}
