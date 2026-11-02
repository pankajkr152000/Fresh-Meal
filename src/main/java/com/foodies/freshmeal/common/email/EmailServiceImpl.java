
package com.foodies.freshmeal.common.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * ============================================================================
 * Service : EmailServiceImpl
 * ============================================================================
 *
 * Default FreshMeal email delivery implementation based on Spring's
 * JavaMailSender.
 *
 * Responsibilities: - Deliver plain-text email messages. - Deliver HTML email
 * messages. - Validate email delivery inputs. - Keep mail-provider
 * implementation details within the infrastructure layer.
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
	public EmailServiceImpl(final JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	// =========================================================================
	// Plain-text Email Delivery
	// =========================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendEmail(final String recipient, final String subject, final String body) {

		validateInput(recipient, subject, body);

		final SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(recipient);
		message.setSubject(subject);
		message.setText(body);

		mailSender.send(message);
	}

	// =========================================================================
	// HTML Email Delivery
	// =========================================================================

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * Sends an HTML email using MIME message support.
	 * </p>
	 *
	 * @throws RuntimeException when message construction or delivery fails.
	 */
	@Override
	public void sendHtmlEmail(final String recipient, final String subject, final String htmlBody) {

		validateInput(recipient, subject, htmlBody);

		try {

			final MimeMessage mimeMessage = mailSender.createMimeMessage();

			final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

			messageHelper.setTo(recipient);
			messageHelper.setSubject(subject);
			messageHelper.setText(htmlBody, true);

			mailSender.send(mimeMessage);

		} catch (final MessagingException exception) {

			throw new IllegalStateException("Unable to construct the HTML email message.", exception);
		}
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
	private void validateInput(final String recipient, final String subject, final String body) {

		if (recipient == null || recipient.isBlank()) {
			throw new IllegalArgumentException("Email recipient must not be empty.");
		}

		if (subject == null || subject.isBlank()) {
			throw new IllegalArgumentException("Email subject must not be empty.");
		}

		if (body == null || body.isBlank()) {
			throw new IllegalArgumentException("Email body must not be empty.");
		}
	}
}
