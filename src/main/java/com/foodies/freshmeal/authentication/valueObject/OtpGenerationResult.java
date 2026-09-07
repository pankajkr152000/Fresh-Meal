package com.foodies.freshmeal.authentication.valueObject;

import java.io.Serializable;

/**
 * =================================================================================================
 * OTP GENERATION RESULT
 * =================================================================================================
 *
 * <p>
 * Represents the result of generating an email verification OTP.
 * </p>
 *
 * <p>
 * The raw OTP is returned only to the application layer so it can be delivered
 * through the configured email service. It must <b>never</b> be persisted,
 * logged, or included in an API response.
 * </p>
 *
 * <h3>Contains</h3>
 * <ul>
 * <li><b>verificationNumber</b> - persistent identifier of the OTP record.</li>
 * <li><b>rawOtp</b> - one-time plaintext OTP used only for email delivery.</li>
 * </ul>
 *
 * @param verificationNumber persistent OTP verification identifier
 * @param rawOtp             plaintext OTP for one-time email delivery
 */
public record OtpGenerationResult(String verificationNumber, String rawOtp) implements Serializable {

	private static final long serialVersionUID = 1L;
}