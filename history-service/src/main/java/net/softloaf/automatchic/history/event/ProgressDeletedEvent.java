package net.softloaf.automatchic.history.event;

import java.time.Instant;

public record ProgressDeletedEvent(
        Long subjectId,
        Instant timestamp
) {
}
