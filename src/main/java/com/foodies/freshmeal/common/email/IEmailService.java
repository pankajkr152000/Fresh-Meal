package com.foodies.freshmeal.common.email;

/**
 * ============================================================================
 * Interface : IEmailService
 * ============================================================================
 *
 * <p>
 * Defines the common email-delivery contract used by the FreshMeal
 * application.
 * </p>
 *
 * <p>
 * The email service provides a reusable infrastructure boundary between
 * FreshMeal business modules and the underlying email delivery mechanism.
 * Business modules should depend on this interface rather than directly
 * interacting with JavaMail or a specific mail provider.
 * </p>
 *
 * <h3>Responsibilities</h3>
 *
 * <ul>
 * <li>Accept email delivery requests from application services.</li>
 * <li>Delegate message delivery to the configured email provider.</li>
 * <li>Keep SMTP/provider implementation details outside business modules.</li>
 * </ul>
 *
 * <h3>Security Boundary</h3>
 *
 * <p>
 * The email service is responsible for message delivery only. It does not
 * authenticate users, generate authentication tokens, generate OTPs, or
 * manage password-reset tokens.
 * </p>
 *
 * <h3>Usage</h3>
 *
 * <p>
 * Authentication, order, restaurant, delivery, and other application modules
 * may depend on this interface whenever an email notification is required.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IEmailService {

    /**
     * Sends an email message.
     *
     * <p>
     * The implementation is responsible for constructing and delivering the
     * underlying mail message through the configured email provider.
     * </p>
     *
     * @param recipient recipient email address.
     * @param subject   email subject.
     * @param body      email body.
     *
     * @throws IllegalArgumentException when required email information is
     *                                  missing.
     * @throws RuntimeException         when the email cannot be delivered.
     */
    void sendEmail(
            String recipient,
            String subject,
            String body);
}