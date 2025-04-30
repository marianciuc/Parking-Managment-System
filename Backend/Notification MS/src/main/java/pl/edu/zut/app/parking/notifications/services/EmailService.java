package pl.edu.zut.app.parking.notifications.services;

import pl.edu.zut.app.parking.notifications.kafka.messages.FailureDepositMessage;
import pl.edu.zut.app.parking.notifications.kafka.messages.LowBalanceMessage;
import pl.edu.zut.app.parking.notifications.kafka.messages.SessionPaymentMessage;
import pl.edu.zut.app.parking.notifications.kafka.messages.SuccesfulDepositMessage;

/**
 * The EmailService interface defines methods for sending various types of email messages,
 * such as plain text emails, HTML emails, email verifications, and password recovery codes.
 * This service is responsible for facilitating email communication.
 */
public interface EmailService {

    /**
     * Sends a plain text email to the specified recipient.
     *
     * @param to the recipient's email address
     * @param subject the subject of the email
     * @param text the body content of the email in plain text
     */
    void sendEmail(String to, String subject, String text);


    /**
     * Sends an HTML-formatted email to the specified recipient.
     *
     * @param to the recipient's email address
     * @param subject the subject of the email
     * @param html the body content of the email in HTML format
     */
    void sendHtmlEmail(String to, String subject, String html);


    /**
     * Sends an email containing a verification code to the specified email address.
     *
     * @param email the recipient's email address where the verification email will be sent
     * @param verificationCode the code to be included in the email for verification purposes
     */
    void sendEmailVerification(String email, String verificationCode);

    /**
     * Sends an email containing a password recovery code to the specified email address.
     *
     * @param email the recipient's email address where the password recovery email will be sent
     * @param key the key or recovery code to be included in the email for password recovery purposes
     */
    void sendPasswordRecoveryCode(String email, String key);

    void sendEmailSessionPayment(SessionPaymentMessage message);

    void sendEmailLowBalanceMessage(LowBalanceMessage message);

    void sendEmailSuccessfulDeposit(SuccesfulDepositMessage message);

    void sendEmailFailureDeposit(FailureDepositMessage message);
}
