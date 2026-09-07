package com.foodies.freshmeal.authentication.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodies.freshmeal.authentication.constants.AuthenticationApiConstants;
import com.foodies.freshmeal.authentication.constants.AuthenticationMessageConstants;
import com.foodies.freshmeal.authentication.controller.IAuthenticationController;
import com.foodies.freshmeal.authentication.dto.ChangePasswordInputDTO;
import com.foodies.freshmeal.authentication.dto.ChangePasswordRequest;
import com.foodies.freshmeal.authentication.dto.ForgotPasswordInputDTO;
import com.foodies.freshmeal.authentication.dto.ForgotPasswordRequest;
import com.foodies.freshmeal.authentication.dto.LoginInputDTO;
import com.foodies.freshmeal.authentication.dto.LoginRequest;
import com.foodies.freshmeal.authentication.dto.LoginResponse;
import com.foodies.freshmeal.authentication.dto.LogoutInputDTO;
import com.foodies.freshmeal.authentication.dto.LogoutRequest;
import com.foodies.freshmeal.authentication.dto.RefreshTokenInputDTO;
import com.foodies.freshmeal.authentication.dto.RefreshTokenRequest;
import com.foodies.freshmeal.authentication.dto.RegisterInputDTO;
import com.foodies.freshmeal.authentication.dto.RegisterRequest;
import com.foodies.freshmeal.authentication.dto.RegisterResponse;
import com.foodies.freshmeal.authentication.dto.ResendEmailOtpInputDTO;
import com.foodies.freshmeal.authentication.dto.ResendEmailOtpRequest;
import com.foodies.freshmeal.authentication.dto.ResetPasswordInputDTO;
import com.foodies.freshmeal.authentication.dto.ResetPasswordRequest;
import com.foodies.freshmeal.authentication.dto.TokenResponse;
import com.foodies.freshmeal.authentication.dto.VerifyEmailOtpInputDTO;
import com.foodies.freshmeal.authentication.dto.VerifyEmailOtpRequest;
import com.foodies.freshmeal.authentication.dto.VerifyEmailOtpResponse;
import com.foodies.freshmeal.authentication.service.IAuthenticationService;
import com.foodies.freshmeal.common.audit.annotation.AuditApi;
import com.foodies.freshmeal.common.builder.ApiResponseBuilder;
import com.foodies.freshmeal.common.constants.ActionType;
import com.foodies.freshmeal.common.constants.ApiBaseConstants;
import com.foodies.freshmeal.common.constants.MethodType;
import com.foodies.freshmeal.common.constants.ModuleType;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;

import jakarta.validation.Valid;

/**
 * ============================================================================
 * Controller : AuthenticationController
 * ============================================================================
 *
 * Provides REST API endpoints for FreshMeal authentication operations.
 *
 * <p>
 * This controller acts as the HTTP boundary for authentication workflows and
 * delegates all business processing to {@link IAuthenticationService}.
 * </p>
 *
 * <h3>Controller Responsibilities</h3>
 *
 * <ul>
 * <li>Accept and validate authentication API requests.</li>
 * <li>Construct FreshMeal service input objects.</li>
 * <li>Provide the current {@link IServiceContext} to the service layer.</li>
 * <li>Delegate authentication operations to
 * {@link IAuthenticationService}.</li>
 * <li>Map service output into standard {@link ApiResponse} objects.</li>
 * </ul>
 *
 * <h3>Security Boundary</h3>
 *
 * <p>
 * The controller does not perform password comparison, JWT generation,
 * refresh-token validation, OTP verification, password-reset validation, or
 * direct repository operations. These responsibilities remain within the
 * authentication and security layers.
 * </p>
 *
 * <h3>Authentication Endpoints</h3>
 *
 * <pre>
 * POST /api/auth/register
 * POST /api/auth/verify-email-otp
 * POST /api/auth/resend-email-otp
 * POST /api/auth/login
 * POST /api/auth/refresh
 * POST /api/auth/logout
 * POST /api/auth/change-password
 * POST /api/auth/forgot-password
 * POST /api/auth/reset-password
 * </pre>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@RestController
@RequestMapping(ApiBaseConstants.AUTHENTICATION_BASE_URL)
public class AuthenticationController implements IAuthenticationController {

    // =========================================================================
    // Dependencies
    // =========================================================================

    /**
     * Authentication business service.
     */
    private final IAuthenticationService authenticationService;

    /**
     * Current FreshMeal service context.
     */
    private final IServiceContext serviceContext;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Creates the authentication controller.
     *
     * @param authenticationService authentication business service.
     * @param serviceContext        current FreshMeal service context.
     */
    public AuthenticationController(IAuthenticationService authenticationService, IServiceContext serviceContext) {

        this.authenticationService = authenticationService;
        this.serviceContext = serviceContext;
    }

    // =========================================================================
    // Registration
    // =========================================================================

    /**
     * Registers a new FreshMeal user account.
     *
     * <p>
     * The registration service creates the account and initiates the email
     * verification workflow.
     * </p>
     *
     * @param request registration request.
     * @return registration API response.
     */
    @Override
    @AuditApi(action = ActionType.REGISTER, module = ModuleType.AUTHENTICATION, method = MethodType.CREATE)
    @PostMapping(AuthenticationApiConstants.REGISTER)
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {

        RegisterInputDTO inputDTO = new RegisterInputDTO();

        inputDTO.setRegisterRequest(request);

        IServiceInput<RegisterInputDTO> input = new ServiceInput<>();

        input.setInput(inputDTO);
        input.setServiceContext(serviceContext);

        var output = authenticationService.register(input);

        return ApiResponseBuilder.created(AuthenticationMessageConstants.REGISTRATION_SUCCESSFUL, output.getOutput());
    }

    // =========================================================================
    // Email Verification
    // =========================================================================

    /**
     * Verifies a user's email address using the supplied OTP.
     *
     * @param request email verification OTP request.
     * @return email verification API response.
     */
    @Override
    @AuditApi(action = ActionType.VERIFY_EMAIL_OTP, module = ModuleType.AUTHENTICATION, method = MethodType.UPDATE)
    @PostMapping(AuthenticationApiConstants.VERIFY_EMAIL_OTP)
    public ResponseEntity<ApiResponse<VerifyEmailOtpResponse>> verifyEmailOtp(
            @Valid @RequestBody VerifyEmailOtpRequest request) {

        VerifyEmailOtpInputDTO inputDTO = new VerifyEmailOtpInputDTO();

        inputDTO.setVerifyEmailOtpRequest(request);

        IServiceInput<VerifyEmailOtpInputDTO> input = new ServiceInput<>();

        input.setInput(inputDTO);
        input.setServiceContext(serviceContext);

        var output = authenticationService.verifyEmailOtp(input);

        return ApiResponseBuilder.success(AuthenticationMessageConstants.EMAIL_VERIFICATION_SUCCESSFUL,
                output.getOutput());
    }

    /**
     * Resends an email verification OTP.
     *
     * @param request email OTP resend request.
     * @return registration/verification API response.
     */
    @Override
    @AuditApi(action = ActionType.RESEND_EMAIL_OTP, module = ModuleType.AUTHENTICATION, method = MethodType.CREATE)
    @PostMapping(AuthenticationApiConstants.RESEND_EMAIL_OTP)
    public ResponseEntity<ApiResponse<RegisterResponse>> resendEmailOtp(
            @Valid @RequestBody ResendEmailOtpRequest request) {

        ResendEmailOtpInputDTO inputDTO = new ResendEmailOtpInputDTO();

        inputDTO.setResendEmailOtpRequest(request);

        IServiceInput<ResendEmailOtpInputDTO> input = new ServiceInput<>();

        input.setInput(inputDTO);
        input.setServiceContext(serviceContext);

        var output = authenticationService.resendEmailOtp(input);

        return ApiResponseBuilder.success(AuthenticationMessageConstants.REGISTRATION_SUCCESSFUL, output.getOutput());
    }

    // =========================================================================
    // Login
    // =========================================================================

    /**
     * Authenticates a FreshMeal user and issues authentication tokens.
     *
     * @param request login request.
     * @return login API response containing authentication tokens.
     */
    @Override
    @AuditApi(action = ActionType.LOGIN, module = ModuleType.AUTHENTICATION, method = MethodType.CREATE)
    @PostMapping(AuthenticationApiConstants.LOGIN)
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {

        LoginInputDTO inputDTO = new LoginInputDTO();

        inputDTO.setLoginRequest(request);

        IServiceInput<LoginInputDTO> input = new ServiceInput<>();

        input.setInput(inputDTO);
        input.setServiceContext(serviceContext);

        var output = authenticationService.login(input);

        return ApiResponseBuilder.success(AuthenticationMessageConstants.LOGIN_SUCCESSFUL, output.getOutput());
    }

    // =========================================================================
    // Token Management
    // =========================================================================

    /**
     * Refreshes authentication tokens using a valid refresh token.
     *
     * @param request refresh-token request.
     * @return newly issued token information.
     */
    @Override
    @AuditApi(action = ActionType.REFRESH_TOKEN, module = ModuleType.AUTHENTICATION, method = MethodType.CREATE)
    @PostMapping(AuthenticationApiConstants.REFRESH_TOKEN)
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {

        RefreshTokenInputDTO inputDTO = new RefreshTokenInputDTO();

        inputDTO.setRefreshTokenRequest(request);

        IServiceInput<RefreshTokenInputDTO> input = new ServiceInput<>();

        input.setInput(inputDTO);
        input.setServiceContext(serviceContext);

        var output = authenticationService.refreshToken(input);

        return ApiResponseBuilder.success(AuthenticationMessageConstants.TOKEN_REFRESHED, output.getOutput());
    }

    // =========================================================================
    // Logout
    // =========================================================================

    /**
     * Logs out the currently authenticated user.
     *
     * @param request logout request.
     * @return logout API response.
     */
    @Override
    @AuditApi(action = ActionType.LOGOUT, module = ModuleType.AUTHENTICATION, method = MethodType.UPDATE)
    @PostMapping(AuthenticationApiConstants.LOGOUT)
    public ResponseEntity<ApiResponse<Boolean>> logout(@Valid @RequestBody LogoutRequest request) {

        LogoutInputDTO inputDTO = new LogoutInputDTO();

        inputDTO.setLogoutRequest(request);

        IServiceInput<LogoutInputDTO> input = new ServiceInput<>();

        input.setInput(inputDTO);
        input.setServiceContext(serviceContext);

        var output = authenticationService.logout(input);

        return ApiResponseBuilder.success(AuthenticationMessageConstants.LOGOUT_SUCCESSFUL, output.getOutput());
    }

    // =========================================================================
    // Password Management
    // =========================================================================

    /**
     * Changes the password of the currently authenticated user.
     *
     * @param request change-password request.
     * @return password-change API response.
     */
    @Override
    @AuditApi(action = ActionType.CHANGE_PASSWORD, module = ModuleType.AUTHENTICATION, method = MethodType.UPDATE)
    @PostMapping(AuthenticationApiConstants.CHANGE_PASSWORD)
    public ResponseEntity<ApiResponse<Boolean>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {

        ChangePasswordInputDTO inputDTO = new ChangePasswordInputDTO();

        inputDTO.setChangePasswordRequest(request);

        IServiceInput<ChangePasswordInputDTO> input = new ServiceInput<>();

        input.setInput(inputDTO);
        input.setServiceContext(serviceContext);

        var output = authenticationService.changePassword(input);

        return ApiResponseBuilder.success(AuthenticationMessageConstants.PASSWORD_CHANGED, output.getOutput());
    }

    /**
     * Initiates password recovery for the supplied email address.
     *
     * <p>
     * The response must remain generic and must not disclose whether an account
     * exists for the supplied email address.
     * </p>
     *
     * @param request forgot-password request.
     * @return generic password-recovery API response.
     */
    @Override
    @AuditApi(action = ActionType.FORGOT_PASSWORD, module = ModuleType.AUTHENTICATION, method = MethodType.CREATE)
    @PostMapping(AuthenticationApiConstants.FORGOT_PASSWORD)
    public ResponseEntity<ApiResponse<Boolean>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {

        ForgotPasswordInputDTO inputDTO = new ForgotPasswordInputDTO();

        inputDTO.setForgotPasswordRequest(request);

        IServiceInput<ForgotPasswordInputDTO> input = new ServiceInput<>();

        input.setInput(inputDTO);
        input.setServiceContext(serviceContext);

        var output = authenticationService.forgotPassword(input);

        return ApiResponseBuilder.success(AuthenticationMessageConstants.PASSWORD_RECOVERY_REQUESTED,
                output.getOutput());
    }

    /**
     * Resets a user's password using a valid password-reset token.
     *
     * @param request password-reset request.
     * @return password-reset API response.
     */
    @Override
    @AuditApi(action = ActionType.RESET_PASSWORD, module = ModuleType.AUTHENTICATION, method = MethodType.UPDATE)
    @PostMapping(AuthenticationApiConstants.RESET_PASSWORD)
    public ResponseEntity<ApiResponse<Boolean>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {

        ResetPasswordInputDTO inputDTO = new ResetPasswordInputDTO();

        inputDTO.setResetPasswordRequest(request);

        IServiceInput<ResetPasswordInputDTO> input = new ServiceInput<>();

        input.setInput(inputDTO);
        input.setServiceContext(serviceContext);

        var output = authenticationService.resetPassword(input);

        return ApiResponseBuilder.success(AuthenticationMessageConstants.PASSWORD_RESET_SUCCESSFUL, output.getOutput());
    }
}
