package net.softloaf.automatchic.history.query.service;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.common.dto.response.ProgressChartDataResponse;
import net.softloaf.automatchic.common.dto.response.ProgressSnapshotResponse;
import net.softloaf.automatchic.history.event.*;
import org.axonframework.eventhandling.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.DomainEventStream;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProgressQueryService {
    private final EventStore eventStore;

    public List<ProgressSnapshotResponse> buildTimeline(Long subjectId) {

        DomainEventStream stream = eventStore.readEvents(subjectId.toString());

        ProgressSnapshotResponse snapshot = new ProgressSnapshotResponse();

        List<ProgressSnapshotResponse> result = new ArrayList<>();

        while (stream.hasNext()) {
            DomainEventMessage<?> msg = stream.next();
            Object event = msg.getPayload();

            if(event instanceof ProgressCreatedEvent e) {
                snapshot.setTotalScore(e.totalScore());
                snapshot.setTotalWeight(e.totalWeight());
                snapshot.setGradingType(e.gradingType().toString());
                snapshot.setEvaluationType(e.evaluationType().toString());
                snapshot.setTargetGrade(e.targetGrade());
                snapshot.setGradingMax(e.gradingMax());
                snapshot.setGrading5(e.grading5());
                snapshot.setGrading4(e.grading4());
                snapshot.setGrading3(e.grading3());
                snapshot.setGradingMin(e.gradingMin());
                snapshot.setTimestamp(e.timestamp());
            } else if(event instanceof ScoreUpdatedEvent e) {
                snapshot.setTotalScore(snapshot.getTotalScore() + e.scoreDelta());
                snapshot.setTotalWeight(snapshot.getTotalWeight() + e.weightDelta());
                snapshot.setTimestamp(e.timestamp());
            } else if(event instanceof TargetGradeUpdatedEvent e){
                snapshot.setTargetGrade(e.targetGrade());
                snapshot.setTimestamp(e.timestamp());
            } else if(event instanceof GradingsUpdatedEvent e){
                snapshot.setGradingMax(e.gradingMax());
                snapshot.setGrading5(e.grading5());
                snapshot.setGrading4(e.grading4());
                snapshot.setGrading3(e.grading3());
                snapshot.setGradingMin(e.gradingMin());
                snapshot.setTimestamp(e.timestamp());
            } else if(event instanceof GradingTypeUpdatedEvent e){
                snapshot.setGradingType(e.gradingType().toString());
                snapshot.setTimestamp(e.timestamp());
            } else if(event instanceof EvaluationTypeUpdatedEvent e){
                snapshot.setEvaluationType(e.evaluationType().toString());
                snapshot.setTimestamp(e.timestamp());
            } else if(event instanceof ProgressDeletedEvent e){
                snapshot.setTimestamp(e.timestamp());
                result.add(new ProgressSnapshotResponse(snapshot));
                break;
            }

            result.add(new ProgressSnapshotResponse(snapshot));
        }

        return result;
    }

    public List<ProgressChartDataResponse> buildChartPoints(Long subjectId, Duration interval) {

        List<ProgressSnapshotResponse> timeline = buildTimeline(subjectId);

        if (timeline.isEmpty()) {
            return List.of();
        }

        timeline.sort(Comparator.comparing(ProgressSnapshotResponse::getTimestamp));

        List<ProgressChartDataResponse> result = new ArrayList<>();

        Instant first = timeline.getFirst().getTimestamp();
        ZonedDateTime zdt = first.atZone(ZoneId.systemDefault());

        long intervalSeconds = interval.getSeconds();
        long epochSeconds = zdt.toEpochSecond();

        long remainder = epochSeconds % intervalSeconds;

        ZonedDateTime currentPoint = zdt
                .minusSeconds(remainder)
                .withNano(0);

        Instant now = Instant.now();

        int index = 0;
        ProgressSnapshotResponse currentSnapshot = timeline.getFirst();

        while (!currentPoint.toInstant().isAfter(now)) {

            Instant pointInstant = currentPoint.toInstant();

            while (index < timeline.size() &&
                    !timeline.get(index).getTimestamp().isAfter(pointInstant)) {

                currentSnapshot = timeline.get(index);
                index++;
            }

            result.add(new ProgressChartDataResponse(pointInstant, currentSnapshot));

            currentPoint = currentPoint.plus(interval);
        }

        if (!result.isEmpty()) {
            ProgressChartDataResponse lastPoint = result.getLast();
            Instant nextInstant = lastPoint.getTimestampX().plus(interval);

            result.add(new ProgressChartDataResponse(nextInstant, lastPoint.getSnapshot()));
        }

        return result;
    }
}
