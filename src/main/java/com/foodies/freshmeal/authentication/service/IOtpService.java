package com.foodies.freshmeal.authentication.service;

import com.foodies.freshmeal.authentication.valueObject.OtpGenerationResult;
import com.foodies.freshmeal.authentication.valueObject.OtpVerificationResult;
import com.foodies.freshmeal.common.io.service.IServiceContext;

/**
 * =================================================================================================
 * OTP SERVICE
 * =================================================================================================
 *
 * <p>
 * Defines OTP generation, verification, and lifecycle operations used by the
 * authentication module.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Generate secure email verification OTPs.</li>
 * <li>Persist only hashed OTP values.</li>
 * <li>Enforce OTP expiration.</li>
 * <li>Enforce maximum verification attempts.</li>
 * <li>Enforce resend cooldown.</li>
 * <li>Enforce the one-hour lockout after maximum attempts are exhausted.</li>
 * <li>Revoke previously active OTPs when a new OTP is generated.</li>
 * <li>Track successful OTP dispatch through {@code sentAt}.</li>
 * </ul>
 *
 * <p>
 * The service never persists or exposes the raw OTP beyond the transient
 * application flow required for email delivery.
 * </p>
 *
 * =================================================================================================
 */
public interface IOtpService {

	/**
	 * Generates a new email verification OTP.
	 *
	 * <p>
	 * The OTP is persisted only in hashed form. The raw OTP is returned as part of
	 * {@link OtpGenerationResult} so that the authentication layer can send it
	 * through {@code IEmailService}.
	 * </p>
	 *
	 * <p>
	 * The result also contains the persistent verification number of the generated
	 * OTP record. This identifier is subsequently used to mark the exact OTP record
	 * as successfully dispatched.
	 * </p>
	 *
	 * @param userNumber     user business identifier
	 * @param email          email address associated with the user
	 * @param serviceContext current service context
	 * @return generation result containing the verification number and raw OTP
	 */
	OtpGenerationResult generateEmailVerificationOtp(String userNumber, String email, IServiceContext serviceContext);

	/**
	 * Verifies an email verification OTP.
	 *
	 * <p>
	 * The implementation enforces expiration, maximum attempts, and the one-hour
	 * lockout period after all attempts are exhausted.
	 * </p>
	 *
	 * @param userNumber     user business identifier
	 * @param email          email address associated with the user
	 * @param otp            OTP supplied by the user
	 * @param serviceContext current service context
	 * @return verification result containing verification status and remaining
	 *         attempts
	 */
	OtpVerificationResult verifyEmailVerificationOtp(String userNumber, String email, String otp,
			IServiceContext serviceContext);

	/**
	 * Marks an OTP as successfully dispatched.
	 *
	 * <p>
	 * This operation updates {@code sentAt} only after the email service has
	 * successfully accepted the email for delivery.
	 * </p>
	 *
	 * <p>
	 * The persistent verification number is used to identify the exact OTP record
	 * generated for the email delivery operation. The raw OTP is intentionally not
	 * required for this operation.
	 * </p>
	 *
	 * @param verificationNumber persistent identifier of the generated OTP record
	 * @param serviceContext     current service context
	 */
	void markEmailVerificationOtpSent(String verificationNumber, IServiceContext serviceContext);

	/**
	 * Revokes the currently active email verification OTP for a user.
	 *
	 * @param userNumber     user business identifier
	 * @param serviceContext current service context
	 */
	void revokeActiveEmailVerificationOtp(String userNumber, IServiceContext serviceContext);
}