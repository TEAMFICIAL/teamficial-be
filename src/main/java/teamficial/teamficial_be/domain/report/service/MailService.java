package teamficial.teamficial_be.domain.report.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Async
    public void sendReportEmail(String toEmail) {
        try {
            Context context = new Context();
            String body = templateEngine.process("report", context);
            sendHtmlEmail(toEmail, "[팀피셜] 신고가 접수되었습니다", body);

        } catch (Exception e) {
            log.error("Failed to send report email to", e);
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            ClassPathResource imageResource = new ClassPathResource("/static/report_email.jpeg");
            helper.addInline("report_email", imageResource);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
