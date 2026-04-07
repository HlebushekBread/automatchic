package net.softloaf.automatchic.common.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String EMAIL_CONFIRMATION_TOPIC = "email-confirmation-topic";
    public static final String PASSWORD_RESET_TOPIC = "password-reset-topic";

    @Bean
    public NewTopic emailConfirmationTopic() {
        return TopicBuilder.name(EMAIL_CONFIRMATION_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic passwordResetTopic() {
        return TopicBuilder.name(PASSWORD_RESET_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
