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

    @KafkaListener(topics = KafkaConfig.EMAIL_CONFIRMATION_TOPIC, groupId = "${spring.application.name}-group")
    public void consumeEmailConfirmation(EmailNotificationEvent event) {
        String html = String.format(
                "<h3>Добро пожаловать!</h3>" +
                        "<h4>Подтверждение почты для сайта softloaf.net</h4>" +
                        "<p>Для подтверждения регистрации нажмите на кнопку ниже:</p>" +
                        "<a href='%s' style='background: #373A40; color: white; padding: 10px 20px; text-decoration: none;'>Подтвердить почту</a>" +
                        "<p>Если вы не регистрировались, игнорируйте это письмо.</p>",
                event.getLink());

        sendHtmlEmail(event.getEmail(), event.getSubject(), html);
    }

    @KafkaListener(topics = KafkaConfig.PASSWORD_RESET_TOPIC, groupId = "${spring.application.name}-group")
    public void consumePasswordReset(EmailNotificationEvent event) {
        String html = String.format(
                "<h3>Сброс пароля</h3>" +
                        "<h4>Сброс пароля учетной записи на softloaf.net</h4>" +
                        "<p>Для сброса пароля нажмите на кнопку ниже:</p>" +
                        "<a href='%s' style='background: #373A40; color: white; padding: 10px 20px; text-decoration: none;'>Сбросить пароль</a>" +
                        "<p>Если вы не меняли пароль, игнорируйте это письмо.</p>",
                event.getLink());

        sendHtmlEmail(event.getEmail(), event.getSubject(), html);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@softloaf.net");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Письмо ({}) успешно отправлено на {}", subject, to);
        } catch (Exception e) {
            log.error("Ошибка при отправке письма ({}): {}", subject, e.getMessage());
        }
    }
}
