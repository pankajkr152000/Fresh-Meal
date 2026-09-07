
package com.foodies.freshmeal.authentication.controller;

import org.springframework.http.ResponseEntity;

import com.foodies.freshmeal.authentication.dto.ChangePasswordRequest;
import com.foodies.freshmeal.authentication.dto.ForgotPasswordRequest;
import com.foodies.freshmeal.authentication.dto.LoginRequest;
import com.foodies.freshmeal.authentication.dto.LoginResponse;
import com.foodies.freshmeal.authentication.dto.LogoutRequest;
import com.foodies.freshmeal.authentication.dto.RefreshTokenRequest;
import com.foodies.freshmeal.authentication.dto.RegisterRequest;
import com.foodies.freshmeal.authentication.dto.RegisterResponse;
import com.foodies.freshmeal.authentication.dto.ResendEmailOtpRequest;
import com.foodies.freshmeal.authentication.dto.ResetPasswordRequest;
import com.foodies.freshmeal.authentication.dto.TokenResponse;
import com.foodies.freshmeal.authentication.dto.VerifyEmailOtpRequest;
import com.foodies.freshmeal.authentication.dto.VerifyEmailOtpResponse;
import com.foodies.freshmeal.common.dto.ApiResponse;

/**
 * ============================================================================
 * Controller : IAuthenticationController
 * ============================================================================
 *
 * Defines the public HTTP API contract for FreshMeal authentication operations.
 *
 * <p>
 * This interface represents the API boundary between authentication clients and
 * the FreshMeal authentication service. It defines the complete set of
 * authentication-related endpoints without exposing implementation details.
 * </p>
 *
 * <h3>Supported Authentication Workflows</h3>
 *
 * <ul>
 * <li>User registration</li>
 * <li>Email verification using OTP</li>
 * <li>Email verification OTP resend</li>
 * <li>User login</li>
 * <li>Authentication token refresh</li>
 * <li>User logout</li>
 * <li>Password change</li>
 * <li>Password recovery</li>
 * <li>Password reset</li>
 * </ul>
 *
 * <h3>Security Boundary</h3>
 *
 * <p>
 * The controller is responsible for HTTP request handling, request validation,
 * service input construction, and API response mapping. It must not perform
 * password verification, OTP validation, JWT parsing, token generation,
 * repository operations, or authentication business rules directly.
 * </p>
 *
 * <h3>Authentication and Authorization</h3>
 *
 * <p>
 * Authentication establishes the identity of the caller. Authorization
 * decisions involving roles, permissions, and protected resources are handled
 * by the security/authorization layers and are not part of this controller
 * contract.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IAuthenticationController {

	// =========================================================================
	// Registration
	// =========================================================================

	/**
	 * Registers a new FreshMeal user account.
	 *
	 * <p>
	 * Registration creates the account in a pending verification state and
	 * initiates the email verification workflow.
	 * </p>
	 *
	 * @param request Registration request.
	 * @return Registration API response.
	 */
	ResponseEntity<ApiResponse<RegisterResponse>> register(RegisterRequest request);

	// =========================================================================
	// Email Verification
	// =========================================================================

	/**
	 * Verifies a user's email address using an email verification OTP.
	 *
	 * @param request Email OTP verification request.
	 * @return Email verification API response.
	 */
	ResponseEntity<ApiResponse<VerifyEmailOtpResponse>> verifyEmailOtp(VerifyEmailOtpRequest request);

	/**
	 * Requests a new email verification OTP for a pending account.
	 *
	 * @param request Email OTP resend request.
	 * @return Registration/verification API response.
	 */
	ResponseEntity<ApiResponse<RegisterResponse>> resendEmailOtp(ResendEmailOtpRequest request);

	// =========================================================================
	// Login
	// =========================================================================

	/**
	 * Authenticates a FreshMeal user using a username or email address and
	 * password.
	 *
	 * <p>
	 * Successful authentication returns an access token and a refresh token.
	 * </p>
	 *
	 * @param request Login request.
	 * @return Login API response containing authentication tokens.
	 */
	ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest request);

	// =========================================================================
	// Token Management
	// =========================================================================

	/**
	 * Refreshes authentication tokens using a valid refresh token.
	 *
	 * @param request Refresh-token request.
	 * @return Newly issued authentication token information.
	 */
	ResponseEntity<ApiResponse<TokenResponse>> refreshToken(RefreshTokenRequest request);

	// =========================================================================
	// Logout
	// =========================================================================

	/**
	 * Logs out the currently authenticated user.
	 *
	 * @param request Logout request.
	 * @return API response indicating whether logout was completed.
	 */
	ResponseEntity<ApiResponse<Boolean>> logout(LogoutRequest request);

	// =========================================================================
	// Password Management
	// =========================================================================

	/**
	 * Changes the password of the currently authenticated user.
	 *
	 * @param request Change-password request.
	 * @return API response indicating whether the password was changed.
	 */
	ResponseEntity<ApiResponse<Boolean>> changePassword(ChangePasswordRequest request);

	/**
	 * Initiates password recovery for the supplied email address.
	 *
	 * <p>
	 * The implementation must not reveal whether an account exists for the supplied
	 * email address.
	 * </p>
	 *
	 * @param request Forgot-password request.
	 * @return Generic password-recovery API response.
	 */
	ResponseEntity<ApiResponse<Boolean>> forgotPassword(ForgotPasswordRequest request);

	/**
	 * Resets a user's password using a valid password-reset token.
	 *
	 * @param request Password-reset request.
	 * @return API response indicating whether the password was reset.
	 */
	ResponseEntity<ApiResponse<Boolean>> resetPassword(ResetPasswordRequest request);
}
