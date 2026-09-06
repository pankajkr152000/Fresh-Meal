package com.foodies.freshmeal.common.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * ============================================================================
 * Service : EmailServiceImpl
 * ============================================================================
 *
 * <p>
 * Default FreshMeal email delivery implementation based on Spring's
 * {@link JavaMailSender}.
 * </p>
 *
 * <p>
 * This service isolates the application from the underlying SMTP or mail
 * provider implementation. Business modules interact with
 * {@link IEmailService} rather than directly using {@link JavaMailSender}.
 * </p>
 *
 * <h3>Responsibilities</h3>
 *
 * <ul>
 * <li>Validate basic email delivery input.</li>
 * <li>Create the outgoing email message.</li>
 * <li>Delegate delivery to {@link JavaMailSender}.</li>
 * </ul>
 *
 * <h3>Provider Independence</h3>
 *
 * <p>
 * The rest of FreshMeal depends only on {@link IEmailService}. The underlying
 * SMTP configuration or future email provider can therefore be changed
 * without requiring changes to authentication or other business modules.
 * </p>
 *
 * <h3>Message Responsibility</h3>
 *
 * <p>
 * This implementation currently sends plain-text messages. Authentication
 * specific subjects and message content should be constructed by the
 * corresponding application service or a future email-template component,
 * rather than being embedded into this infrastructure service.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Service
public class EmailServiceImpl implements IEmailService {

    // =========================================================================
    // Dependencies
    // =========================================================================

    /**
     * Spring email sender.
     */
    private final JavaMailSender mailSender;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Creates the email service.
     *
     * @param mailSender configured Spring mail sender.
     */
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // =========================================================================
    // Email Delivery
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendEmail(
            String recipient,
            String subject,
            String body) {

        validateInput(recipient, subject, body);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    // =========================================================================
    // Validation
    // =========================================================================

    /**
     * Validates the minimum information required to send an email.
     *
     * @param recipient recipient email address.
     * @param subject   email subject.
     * @param body      email body.
     *
     * @throws IllegalArgumentException when any required value is blank.
     */
    private void validateInput(
            String recipient,
            String subject,
            String body) {

        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException(
                    "Email recipient must not be empty.");
        }

        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException(
                    "Email subject must not be empty.");
        }

        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException(
                    "Email body must not be empty.");
        }
    }
}