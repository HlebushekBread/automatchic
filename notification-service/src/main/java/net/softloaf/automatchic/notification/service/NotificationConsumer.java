package net.softloaf.automatchic.notification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.softloaf.automatchic.common.config.KafkaConfig;
import net.softloaf.automatchic.common.dto.EmailNotificationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationConsumer {
    private final JavaMailSender mailSender;

    @KafkaListener(topics = KafkaConfig.NOTIFICATION_TOPIC, groupId = "${spring.application.name}-group")
    public void consume(EmailNotificationEvent event) {
        log.info("Отправка письма на: {} | ссылка: {}", event.getEmail(), event.getLink());

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(event.getEmail());
            helper.setSubject(event.getSubject());

            String htmlContent = String.format(
                    "<h3>Добро пожаловать!</h3>" +
                            "<h4>Подтверждение почты для сайта softloaf.net</h4>" +
                            "<p>Для подтверждения регистрации нажмите на кнопку ниже:</p>" +
                            "<a href='%s' style='background: #373A40; color: white; padding: 10px 20px; text-decoration: none;'>Подтвердить почту</a>" +
                            "<p>Если вы не регистрировались, игнорируйте это письмо.</p>",
                    event.getLink()
            );

            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Письмо успешно отправлено на {}", event.getEmail());

        } catch (MessagingException e) {
            log.error("Ошибка при формировании письма: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Не удалось отправить письмо: {}", e.getMessage());
        }
    }
}
