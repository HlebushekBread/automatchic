package net.softloaf.automatchic.history.service.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.softloaf.automatchic.common.config.KafkaConfig;
import net.softloaf.automatchic.common.dto.ProgressUpdateEvent;
import net.softloaf.automatchic.history.command.*;
import net.softloaf.automatchic.history.event.ProgressDeletedEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProgressConsumer {

    private final CommandGateway commandGateway;

    @KafkaListener(
            topics = KafkaConfig.PROGRESS_TOPIC,
            groupId = "${spring.application.name}-group"
    )
    public void consumeProgressEvent(ProgressUpdateEvent event) {

        log.info("Получено событие: {}", event);

        try {
            switch (event.getEventType()) {
                case CREATE -> handleCreate(event);
                case SCORE_UPDATE -> handleScoreUpdate(event);
                case GRADING_TYPE_UPDATE -> handleGradingTypeUpdate(event);
                case EVALUATION_TYPE_UPDATE -> handleEvaluationTypeUpdate(event);
                case TARGET_GRADE_UPDATE -> handleTargetGradeUpdate(event);
                case GRADINGS_UPDATE -> handleGradingsUpdate(event);
                case DELETE -> handleProgressDelete(event);
                default -> log.warn("Неизвестный тип события: {}", event.getEventType());
            }

        } catch (Exception e) {
            log.error("Ошибка обработки события {}: {}", event, e.getMessage(), e);
            throw e;
        }
    }

    private void handleCreate(ProgressUpdateEvent event) {
        validate(
                event.getSubjectId(),
                event.getScoreDelta(),
                event.getTargetGrade(),
                event.getGradingType(),
                event.getEvaluationType(),
                event.getTargetGrade(),
                event.getGradingMax(),
                event.getGrading5(),
                event.getGrading4(),
                event.getGrading3(),
                event.getGradingMin(),
                event.getTimestamp()
        );

        commandGateway.sendAndWait(
                new CreateProgressCommand(
                        event.getSubjectId(),
                        0 + event.getScoreDelta(),
                        0 + event.getWeightDelta(),
                        event.getGradingType(),
                        event.getEvaluationType(),
                        event.getTargetGrade(),
                        event.getGradingMax(),
                        event.getGrading5(),
                        event.getGrading4(),
                        event.getGrading3(),
                        event.getGradingMin(),
                        event.getTimestamp()
                )
        );

        log.info("Создан Progress для subjectId={}", event.getSubjectId());
    }

    private void handleScoreUpdate(ProgressUpdateEvent event) {
        validate(
                event.getSubjectId(),
                event.getScoreDelta(),
                event.getWeightDelta(),
                event.getTimestamp()
        );

        commandGateway.sendAndWait(
                new UpdateScoreCommand(
                        event.getSubjectId(),
                        event.getScoreDelta(),
                        event.getWeightDelta(),
                        event.getTimestamp()
                )
        );
    }

    private void handleGradingTypeUpdate(ProgressUpdateEvent event) {
        validate(
                event.getSubjectId(),
                event.getGradingType(),
                event.getTimestamp()
        );

        commandGateway.sendAndWait(
                new UpdateGradingTypeCommand(
                        event.getSubjectId(),
                        event.getGradingType(),
                        event.getTimestamp()
                )
        );
    }

    private void handleEvaluationTypeUpdate(ProgressUpdateEvent event) {
        validate(
                event.getSubjectId(),
                event.getEvaluationType(),
                event.getTimestamp()
        );

        commandGateway.sendAndWait(
                new UpdateEvaluationTypeCommand(
                        event.getSubjectId(),
                        event.getEvaluationType(),
                        event.getTimestamp()
                )
        );
    }

    private void handleTargetGradeUpdate(ProgressUpdateEvent event) {
        validate(
                event.getSubjectId(),
                event.getTargetGrade(),
                event.getTimestamp()
        );

        commandGateway.sendAndWait(
                new UpdateTargetGradeCommand(
                        event.getSubjectId(),
                        event.getTargetGrade(),
                        event.getTimestamp()
                )
        );
    }

    private void handleGradingsUpdate(ProgressUpdateEvent event) {
        validate(
                event.getSubjectId(),
                event.getGradingMax(),
                event.getGrading5(),
                event.getGrading4(),
                event.getGrading3(),
                event.getGradingMin(),
                event.getTimestamp()
        );

        commandGateway.sendAndWait(
                new UpdateGradingsCommand(
                        event.getSubjectId(),
                        event.getGradingMax(),
                        event.getGrading5(),
                        event.getGrading4(),
                        event.getGrading3(),
                        event.getGradingMin(),
                        event.getTimestamp()
                )
        );
    }

    private void handleProgressDelete(ProgressUpdateEvent event) {
        validate(
                event.getSubjectId(),
                event.getTimestamp()
        );

        try {
            commandGateway.sendAndWait(
                    new DeleteProgressCommand(
                            event.getSubjectId(),
                            event.getTimestamp()
                    )
            );
        } catch (Exception e) {
            log.warn("Aggregate уже удалён или не найден: {}", event.getSubjectId());
        }
    }

    private void validate(Object... fields) {
        for (Object field : fields) {
            if (field == null) {
                throw new IllegalArgumentException("Kafka event содержит null поле");
            }
        }
    }
}
