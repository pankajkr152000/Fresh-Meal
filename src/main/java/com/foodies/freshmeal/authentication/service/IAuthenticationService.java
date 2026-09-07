package com.foodies.freshmeal.authentication.service;

import com.foodies.freshmeal.authentication.dto.ChangePasswordInputDTO;
import com.foodies.freshmeal.authentication.dto.ForgotPasswordInputDTO;
import com.foodies.freshmeal.authentication.dto.LoginInputDTO;
import com.foodies.freshmeal.authentication.dto.LoginResponse;
import com.foodies.freshmeal.authentication.dto.LogoutInputDTO;
import com.foodies.freshmeal.authentication.dto.RefreshTokenInputDTO;
import com.foodies.freshmeal.authentication.dto.RegisterInputDTO;
import com.foodies.freshmeal.authentication.dto.RegisterResponse;
import com.foodies.freshmeal.authentication.dto.ResendEmailOtpInputDTO;
import com.foodies.freshmeal.authentication.dto.ResetPasswordInputDTO;
import com.foodies.freshmeal.authentication.dto.TokenResponse;
import com.foodies.freshmeal.authentication.dto.VerifyEmailOtpInputDTO;
import com.foodies.freshmeal.authentication.dto.VerifyEmailOtpResponse;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;

/**
 * ============================================================================
 * Authentication Service
 * ============================================================================
 *
 * Defines the business operations responsible for FreshMeal authentication and
 * authentication-related account security workflows.
 *
 * <p>
 * Authentication establishes and maintains the authenticated identity of a
 * user. Authorization concerns such as roles, permissions, and resource access
 * decisions are intentionally outside this contract.
 * </p>
 *
 * <h3>Supported Authentication Workflows</h3>
 *
 * <ul>
 * <li>User registration</li>
 * <li>Email OTP verification</li>
 * <li>Email OTP resend</li>
 * <li>User login</li>
 * <li>Authentication token refresh</li>
 * <li>User logout</li>
 * <li>Password change</li>
 * <li>Password recovery</li>
 * <li>Password reset</li>
 * </ul>
 *
 * <h3>Security Boundaries</h3>
 *
 * <ul>
 * <li>{@code UserEntity} remains the source of truth for user identity.</li>
 * <li>Email verification OTPs are separate from password-reset tokens.</li>
 * <li>Raw passwords and OTPs must never be persisted.</li>
 * <li>Authentication tokens are issued only through successful
 * authentication/token-refresh workflows.</li>
 * <li>Token generation and validation implementation details remain inside the
 * token/security components.</li>
 * <li>Email delivery remains an infrastructure responsibility rather than
 * becoming part of the public authentication service contract.</li>
 * </ul>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IAuthenticationService {

	/**
	 * Registers a new FreshMeal user and initiates email verification.
	 *
	 * <p>
	 * A newly registered account remains unverified until the email OTP
	 * verification workflow is successfully completed.
	 * </p>
	 *
	 * @param input Registration service input.
	 * @return Registration response.
	 */
	IServiceOutput<RegisterResponse> register(IServiceInput<RegisterInputDTO> input);

	/**
	 * Verifies a user's email address using the issued email verification OTP.
	 *
	 * <p>
	 * A successful verification consumes the OTP and updates the user's
	 * verification state.
	 * </p>
	 *
	 * @param input Email OTP verification service input.
	 * @return Email verification response.
	 */
	IServiceOutput<VerifyEmailOtpResponse> verifyEmailOtp(IServiceInput<VerifyEmailOtpInputDTO> input);

	/**
	 * Resends an email verification OTP for a pending account verification
	 * workflow.
	 *
	 * <p>
	 * Resend frequency and maximum resend limits are enforced by the Authentication
	 * business rules.
	 * </p>
	 *
	 * @param input Email OTP resend service input.
	 * @return Registration/verification response.
	 */
	IServiceOutput<RegisterResponse> resendEmailOtp(IServiceInput<ResendEmailOtpInputDTO> input);

	/**
	 * Authenticates a user and issues authentication tokens.
	 *
	 * @param input Login service input.
	 * @return Login response containing authentication information.
	 */
	IServiceOutput<LoginResponse> login(IServiceInput<LoginInputDTO> input);

	/**
	 * Refreshes authentication tokens using a valid refresh token.
	 *
	 * @param input Refresh-token service input.
	 * @return Newly issued token information.
	 */
	IServiceOutput<TokenResponse> refreshToken(IServiceInput<RefreshTokenInputDTO> input);

	/**
	 * Logs out the currently authenticated user.
	 *
	 * <p>
	 * Logout invalidates the applicable authentication state according to the
	 * configured token lifecycle.
	 * </p>
	 *
	 * @param input Logout service input.
	 * @return {@code true} when logout is successfully completed.
	 */
	IServiceOutput<Boolean> logout(IServiceInput<LogoutInputDTO> input);

	/**
	 * Changes the password of the currently authenticated user.
	 *
	 * <p>
	 * The current password must be successfully verified before the new password is
	 * accepted.
	 * </p>
	 *
	 * @param input Change-password service input.
	 * @return {@code true} when the password is successfully changed.
	 */
	IServiceOutput<Boolean> changePassword(IServiceInput<ChangePasswordInputDTO> input);

	/**
	 * Initiates password recovery for the supplied recovery identifier.
	 *
	 * <p>
	 * The implementation must not expose whether a matching account exists.
	 * </p>
	 *
	 * @param input Forgot-password service input.
	 * @return {@code true} when the recovery request is accepted.
	 */
	IServiceOutput<Boolean> forgotPassword(IServiceInput<ForgotPasswordInputDTO> input);

	/**
	 * Resets a user's password using a valid password-reset token.
	 *
	 * @param input Reset-password service input.
	 * @return {@code true} when the password is successfully reset.
	 */
	IServiceOutput<Boolean> resetPassword(IServiceInput<ResetPasswordInputDTO> input);
}