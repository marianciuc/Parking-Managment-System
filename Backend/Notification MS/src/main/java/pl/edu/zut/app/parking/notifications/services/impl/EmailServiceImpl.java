package pl.edu.zut.app.parking.notifications.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import pl.edu.zut.app.parking.notifications.kafka.messages.FailureDepositMessage;
import pl.edu.zut.app.parking.notifications.kafka.messages.LowBalanceMessage;
import pl.edu.zut.app.parking.notifications.kafka.messages.SessionPaymentMessage;
import pl.edu.zut.app.parking.notifications.kafka.messages.SuccesfulDepositMessage;
import pl.edu.zut.app.parking.notifications.services.EmailService;

import org.thymeleaf.context.Context;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    private static final String FROM_ADDRESS = "noreply@mailhog.local";

    @Override
    public void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, false);
            helper.setFrom(FROM_ADDRESS);
            mailSender.send(message);
            log.info("Email sent to {}", to);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            helper.setFrom(FROM_ADDRESS);
            mailSender.send(message);
            log.info("HTML email sent to {}", to);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    @Override
    public void sendEmailVerification(String email, String verificationCode) {
        try {
            String subject = "Email verification";
            Context context = new Context(java.util.Locale.getDefault());
            context.setVariable("code", verificationCode);
            sendHtmlEmail(email, subject, templateEngine.process("email-verification-code.html", context));
        } catch (Exception e) {
            log.error("Failed to send email verification", e);
        }
    }

    @Override
    public void sendPasswordRecoveryCode(String email, String key) {
        try {
            String subject = "Password recovery";
            Context context = new Context(java.util.Locale.getDefault());
            context.setVariable("code", key);
            sendHtmlEmail(email, subject, templateEngine.process("password-recovery-code.html", context));
        } catch (Exception e) {
            log.error("Failed to send password recovery email", e);
        }
    }

    @Override
    public void sendEmailSessionPayment(SessionPaymentMessage message) {

    }

    @Override
    public void sendEmailLowBalanceMessage(LowBalanceMessage message) {

    }

    @Override
    public void sendEmailSuccessfulDeposit(SuccesfulDepositMessage message) {

    }

    @Override
    public void sendEmailFailureDeposit(FailureDepositMessage message) {

    }
}
