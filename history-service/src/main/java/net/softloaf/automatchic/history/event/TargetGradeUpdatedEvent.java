package net.softloaf.automatchic.history.event;

import org.axonframework.serialization.Revision;

import java.time.Instant;

//@Revision("1")
public record TargetGradeUpdatedEvent(
        Long subjectId,
        Integer targetGrade,
        Instant timestamp
) {
}
