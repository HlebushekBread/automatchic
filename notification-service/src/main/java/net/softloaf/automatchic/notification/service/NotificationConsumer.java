package net.softloaf.automatchic.notification.service;

import lombok.extern.slf4j.Slf4j;
import net.softloaf.automatchic.common.config.KafkaConfig;
import net.softloaf.automatchic.common.dto.EmailNotificationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationConsumer {

    @KafkaListener(topics = KafkaConfig.NOTIFICATION_TOPIC, groupId = "${spring.application.name}-group")
    public void consume(EmailNotificationEvent event) {
        log.info("Отправка письма на: {} (ссылка: {})", event.getEmail(), event.getLink());
    }
}
