package net.softloaf.automatchic.app.service.producer;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.common.config.KafkaConfig;
import net.softloaf.automatchic.common.dto.EmailNotificationEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducer {
    private final KafkaTemplate<String, EmailNotificationEvent> kafkaTemplate;

    @Value("${frontend.url}")
    private String frontendUrl;

    public void sendEmailConfirmationEmail(String email, String token) {
        EmailNotificationEvent event = EmailNotificationEvent.builder()
                .email(email)
                .subject("Подтверждение регистрации")
                .link(frontendUrl + "/confirm?token=" + token)
                .build();

        kafkaTemplate.send(KafkaConfig.EMAIL_CONFIRMATION_TOPIC, event);
    }

    public void sendPasswordResetEmail(String email, String token) {
        EmailNotificationEvent event = EmailNotificationEvent.builder()
                .email(email)
                .subject("Сброс пароля")
                .link(frontendUrl + "/reset?token=" + token)
                .build();

        kafkaTemplate.send(KafkaConfig.PASSWORD_RESET_TOPIC, event);
    }
}
