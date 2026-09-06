package com.foodies.freshmeal.authentication.valueObject;

import java.io.Serializable;

/**
 * =================================================================================================
 * OTP VERIFICATION RESULT
 * =================================================================================================
 *
 * <p>
 * Represents the result of an email verification OTP validation operation.
 * </p>
 *
 * <p>
 * The result intentionally contains only the information required by the
 * authentication layer. OTP values, hashes, and other sensitive information
 * are never exposed.
 * </p>
 *
 * <h3>Business Meaning</h3>
 * <ul>
 * <li><b>verified</b> indicates whether the supplied OTP was valid.</li>
 * <li><b>attemptsRemaining</b> indicates how many verification attempts remain
 * for the active OTP.</li>
 * </ul>
 *
 * <p>
 * After successful verification, the OTP is consumed and
 * <code>attemptsRemaining</code> is returned as zero.
 * </p>
 *
 * =================================================================================================
 */
public record OtpVerificationResult(
        boolean verified,
        int attemptsRemaining) implements Serializable {

    private static final long serialVersionUID = 1L;
}