package net.softloaf.automatchic.history.event;

import org.axonframework.serialization.Revision;

import java.time.Instant;

//@Revision("1")
public record GradingsUpdatedEvent(
        Long subjectId,
        Double gradingMax,
        Double grading5,
        Double grading4,
        Double grading3,
        Double gradingMin,
        Instant timestamp
) {
}
