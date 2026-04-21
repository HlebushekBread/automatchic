package net.softloaf.automatchic.app.service.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.softloaf.automatchic.common.config.KafkaConfig;
import net.softloaf.automatchic.common.enums.EvaluationType;
import net.softloaf.automatchic.common.enums.EventType;
import net.softloaf.automatchic.common.enums.GradingType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import net.softloaf.automatchic.common.dto.event.ProgressUpdateEvent;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProgressProducer {
    private final KafkaTemplate<String, ProgressUpdateEvent> kafkaTemplate;

    public void sendCreateProgressEvent(
            Long subjectId,
            Double scoreDelta,
            Double weightDelta,
            GradingType gradingType,
            EvaluationType evaluationType,
            Integer targetGrade,
            Double gradingMax,
            Double grading5,
            Double grading4,
            Double grading3,
            Double gradingMin
    ) {
        ProgressUpdateEvent event = ProgressUpdateEvent.builder().
                subjectId(subjectId).
                scoreDelta(scoreDelta).
                weightDelta(weightDelta).
                gradingType(gradingType).
                evaluationType(evaluationType).
                targetGrade(targetGrade).
                gradingMax(gradingMax).
                grading5(grading5).
                grading4(grading4).
                grading3(grading3).
                gradingMin(gradingMin).
                eventType(EventType.CREATE).
                timestamp(Instant.now()).
                build();
        kafkaTemplate.send(KafkaConfig.PROGRESS_TOPIC, subjectId.toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Kafka send failed", ex);
                    }
                });
    }

    public void sendUpdateScoreEvent(Long subjectId, Double scoreDelta, Double weightDelta) {
        ProgressUpdateEvent event = ProgressUpdateEvent.builder().
                subjectId(subjectId).
                scoreDelta(scoreDelta).
                weightDelta(weightDelta).
                eventType(EventType.SCORE_UPDATE).
                timestamp(Instant.now()).
                build();
        kafkaTemplate.send(KafkaConfig.PROGRESS_TOPIC, subjectId.toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Kafka send failed", ex);
                    }
                });
    }

    public void sendUpdateGradingTypeEvent(Long subjectId, GradingType gradingType) {
        ProgressUpdateEvent event = ProgressUpdateEvent.builder().
                subjectId(subjectId).
                gradingType(gradingType).
                eventType(EventType.GRADING_TYPE_UPDATE).
                timestamp(Instant.now()).
                build();
        kafkaTemplate.send(KafkaConfig.PROGRESS_TOPIC, subjectId.toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Kafka send failed", ex);
                    }
                });
    }

    public void sendUpdateEvaluationTypeEvent(Long subjectId, EvaluationType evaluationType) {
        ProgressUpdateEvent event = ProgressUpdateEvent.builder().
                subjectId(subjectId).
                evaluationType(evaluationType).
                eventType(EventType.EVALUATION_TYPE_UPDATE).
                timestamp(Instant.now()).
                build();
        kafkaTemplate.send(KafkaConfig.PROGRESS_TOPIC, subjectId.toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Kafka send failed", ex);
                    }
                });
    }

    public void sendUpdateTargetGradeEvent(Long subjectId, Integer targetGrade) {
        ProgressUpdateEvent event = ProgressUpdateEvent.builder().
                subjectId(subjectId).
                targetGrade(targetGrade).
                eventType(EventType.TARGET_GRADE_UPDATE).
                timestamp(Instant.now()).
                build();
        kafkaTemplate.send(KafkaConfig.PROGRESS_TOPIC, subjectId.toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Kafka send failed", ex);
                    }
                });
    }

    public void sendUpdateGradingsEvent(
            Long subjectId,
            Double gradingMax,
            Double grading5,
            Double grading4,
            Double grading3,
            Double gradingMin
    ) {
        ProgressUpdateEvent event = ProgressUpdateEvent.builder().
                subjectId(subjectId).
                gradingMax(gradingMax).
                grading5(grading5).
                grading4(grading4).
                grading3(grading3).
                gradingMin(gradingMin).
                eventType(EventType.GRADINGS_UPDATE).
                timestamp(Instant.now()).
                build();
        kafkaTemplate.send(KafkaConfig.PROGRESS_TOPIC, subjectId.toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Kafka send failed", ex);
                    }
                });
    }

    public void sendDeleteProgressEvent(Long subjectId) {
        ProgressUpdateEvent event = ProgressUpdateEvent.builder().
                subjectId(subjectId).
                eventType(EventType.DELETE).
                timestamp(Instant.now()).
                build();
        kafkaTemplate.send(KafkaConfig.PROGRESS_TOPIC, subjectId.toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Kafka send failed", ex);
                    }
                });
    }
}
