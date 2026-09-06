package com.foodies.freshmeal.authentication.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.foodies.freshmeal.authentication.constants.AuthenticationErrorConstants;
import com.foodies.freshmeal.authentication.dto.ChangePasswordInputDTO;
import com.foodies.freshmeal.authentication.dto.ForgotPasswordInputDTO;
import com.foodies.freshmeal.authentication.dto.LoginHistoryInputDTO;
import com.foodies.freshmeal.authentication.dto.LoginHistoryNumberRequest;
import com.foodies.freshmeal.authentication.dto.LoginInputDTO;
import com.foodies.freshmeal.authentication.dto.LoginResponse;
import com.foodies.freshmeal.authentication.dto.LogoutInputDTO;
import com.foodies.freshmeal.authentication.dto.RefreshTokenInputDTO;
import com.foodies.freshmeal.authentication.dto.RegisterInputDTO;
import com.foodies.freshmeal.authentication.dto.RegisterRequest;
import com.foodies.freshmeal.authentication.dto.RegisterResponse;
import com.foodies.freshmeal.authentication.dto.ResendEmailOtpInputDTO;
import com.foodies.freshmeal.authentication.dto.ResendEmailOtpRequest;
import com.foodies.freshmeal.authentication.dto.ResetPasswordInputDTO;
import com.foodies.freshmeal.authentication.dto.TokenResponse;
import com.foodies.freshmeal.authentication.dto.VerifyEmailOtpInputDTO;
import com.foodies.freshmeal.authentication.dto.VerifyEmailOtpRequest;
import com.foodies.freshmeal.authentication.dto.VerifyEmailOtpResponse;
import com.foodies.freshmeal.authentication.entity.PasswordResetTokenEntity;
import com.foodies.freshmeal.authentication.repository.IPasswordResetTokenRepository;
import com.foodies.freshmeal.authentication.service.IAuthenticationService;
import com.foodies.freshmeal.authentication.service.IOtpService;
import com.foodies.freshmeal.authentication.token.ITokenRevocationService;
import com.foodies.freshmeal.authentication.token.ITokenService;
import com.foodies.freshmeal.authentication.valueObject.OtpGenerationResult;
import com.foodies.freshmeal.common.constants.RoleType;
import com.foodies.freshmeal.common.constants.SequenceConstants;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.email.IEmailService;
import com.foodies.freshmeal.common.enums.EntityName;
import com.foodies.freshmeal.common.exception.BusinessException;
import com.foodies.freshmeal.common.exception.ResourceNotFoundException;
import com.foodies.freshmeal.common.factory.EntityFactory;
import com.foodies.freshmeal.common.io.DataContext;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.common.io.service.impl.ServiceOutput;
import com.foodies.freshmeal.common.sequence.service.IDatabaseSequenceService;
import com.foodies.freshmeal.common.util.FreshMealUtilities;
import com.foodies.freshmeal.common.valueObject.EmailAddress;
import com.foodies.freshmeal.user.constants.LoginStatus;
import com.foodies.freshmeal.user.constants.UserErrorConstants;
import com.foodies.freshmeal.user.dto.EmailRequest;
import com.foodies.freshmeal.user.dto.UpdatePasswordInputDTO;
import com.foodies.freshmeal.user.dto.UserInputDTO;
import com.foodies.freshmeal.user.dto.UserNumberRequest;
import com.foodies.freshmeal.user.dto.UserRequest;
import com.foodies.freshmeal.user.dto.UsernameRequest;
import com.foodies.freshmeal.user.entity.LoginHistoryEntity;
import com.foodies.freshmeal.user.entity.UserEntity;
import com.foodies.freshmeal.user.entity.UserProfile;
import com.foodies.freshmeal.user.service.ILoginHistoryService;
import com.foodies.freshmeal.user.service.IUserService;

/**
 * ============================================================================
 * Service : AuthenticationServiceImpl
 * ============================================================================
 *
 * <p>
 * Provides FreshMeal authentication business operations.
 * </p>
 *
 * <p>
 * This service coordinates authentication infrastructure, user registration,
 * email verification, and token operations. Credential verification is
 * delegated to Spring Security, while user persistence remains owned by the
 * User module.
 * </p>
 *
 * <h3>Authentication Responsibility</h3>
 * <p>
 * This service orchestrates authentication workflows but does not directly
 * query user repositories or parse JWTs.
 * </p>
 *
 * <h3>Registration Responsibility</h3>
 * <p>
 * The Authentication module owns registration-specific concerns such as
 * password validation, password encoding, and email verification orchestration.
 * The User module owns creation and persistence of the user entity.
 * </p>
 *
 * <h3>Login Flow</h3>
 * 
 * <pre>
 * LoginRequest
 *      |
 *      v
 * AuthenticationManager
 *      |
 *      v
 * DaoAuthenticationProvider
 *      |
 *      +---- UserDetailsService
 *      |
 *      +---- PasswordEncoder
 *      |
 *      v
 * Authenticated UserProfile
 *      |
 *      v
 * JwtTokenService
 *      |
 *      v
 * LoginResponse
 * </pre>
 *
 * <h3>Registration Flow</h3>
 * 
 * <pre>
 * RegisterRequest
 *      |
 *      v
 * Validate Passwords
 *      |
 *      v
 * PasswordEncoder
 *      |
 *      v
 * IUserService.registerUser()
 *      |
 *      v
 * UserEntity
 *      |
 *      v
 * IOtpService
 *      |
 *      +---- Generate OTP
 *      |
 *      +---- IEmailService
 *      |
 *      +---- Mark OTP as sent
 *      |
 *      v
 * RegisterResponse
 * </pre>
 * 
 * userNumber → who
 * sessionId → which login session
 * jti → which exact token
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

    // =========================================================================
    // Dependencies
    // =========================================================================

    /**
     * Spring Security authentication manager.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * FreshMeal JWT token service.
     */
    private final ITokenService tokenService;

    /**
     * FreshMeal user service responsible for user-domain persistence.
     */
    private final IUserService userService;

    /**
     * Password encoder used to encode registration passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * OTP service responsible for email verification OTP lifecycle.
     */
    private final IOtpService otpService;

    /**
     * Email service responsible for email delivery.
     */
    private final IEmailService emailService;

    /**
     * Service responsible for authentication login-history records.
     */
    private final ILoginHistoryService loginHistoryService;

    /**
     * Server-side JWT token and authentication-session revocation service.
     */
    private final ITokenRevocationService tokenRevocationService;

    final IPasswordResetTokenRepository passwordResetTokenRepository;
    final IDatabaseSequenceService databaseSequenceService;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Creates the authentication service.
     *
     * @param authenticationManager  Spring Security authentication manager
     * @param tokenService           FreshMeal JWT token service
     * @param userService            FreshMeal user service
     * @param passwordEncoder        password encoder
     * @param otpService             email verification OTP service
     * @param emailService           email delivery service
     * @param loginHistoryService    login history service
     * @param tokenRevocationService server-side JWT token and
     *                               authentication-session revocation service
     */
    public AuthenticationServiceImpl(
            final AuthenticationManager authenticationManager,
            final ITokenService tokenService,
            final IUserService userService,
            final PasswordEncoder passwordEncoder,
            final IOtpService otpService,
            final IEmailService emailService,
            final ILoginHistoryService loginHistoryService,
            final ITokenRevocationService tokenRevocationService,
            final IPasswordResetTokenRepository passwordResetTokenRepository,
            final IDatabaseSequenceService databaseSequenceService) {

        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.emailService = emailService;
        this.loginHistoryService = loginHistoryService;
        this.tokenRevocationService = tokenRevocationService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.databaseSequenceService = databaseSequenceService;
    }

    // =========================================================================
    // Login
    // =========================================================================

    /**
     * Authenticates a FreshMeal user and generates an authentication token
     * pair.
     *
     * <p>
     * The login identifier may represent either a username or an email
     * address. The configured {@code UserDetailsService} resolves the
     * identifier while Spring Security performs password verification.
     * </p>
     *
     * @param input login service input
     * @return authenticated token response
     */
    @Override
    public IServiceOutput<LoginResponse> login(
            final IServiceInput<LoginInputDTO> input) {

        final LoginInputDTO inputDTO = input.getInput();

        final String identifier = inputDTO.getLoginRequest().getIdentifier();

        final String password = inputDTO.getLoginRequest().getPassword();

        final UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .unauthenticated(
                        identifier,
                        password);

        final Authentication authentication;

        try {

            authentication = authenticationManager.authenticate(
                    authenticationToken);

        } catch (AuthenticationException exception) {

            recordFailedLogin(identifier, exception, input);

            throw exception;
        }

        final Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserProfile userProfile)) {

            throw new IllegalStateException(
                    "Authenticated principal must be a UserProfile.");
        }
        final String sessionId = UUID.randomUUID().toString();

        final String accessToken = tokenService.generateAccessToken(userProfile, sessionId);

        final String refreshToken = tokenService.generateRefreshToken(userProfile, sessionId);

        final TokenResponse tokenResponse = new TokenResponse();

        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setRefreshToken(refreshToken);
        tokenResponse.setLoginSessionId(sessionId);
        tokenResponse.setTokenType("Bearer");
        tokenResponse.setExpiresIn(tokenService.getAccessTokenExpirationSeconds());

        recordSuccessfulLogin(userProfile, sessionId, input);

        final LoginResponse loginResponse = new LoginResponse();

        loginResponse.setToken(tokenResponse);

        return new ServiceOutput<>(loginResponse);
    }

    // =========================================================================
    // Registration
    // =========================================================================

    /**
     * Registers a new FreshMeal user and initiates email verification.
     *
     * <p>
     * Password validation and encoding are performed by the Authentication
     * module. The encoded password is then passed to the User module, which
     * owns user creation and persistence.
     * </p>
     *
     * <p>
     * After the user is persisted, a verification OTP is generated and sent
     * through {@code IEmailService}. The OTP is marked as successfully sent
     * only after the email service accepts the email for delivery.
     * </p>
     *
     * <p>
     * A self-registered account remains disabled and email-unverified until
     * the email verification workflow is successfully completed.
     * </p>
     *
     * @param input registration service input
     * @return registration response containing verification status
     */
    @Override
    public IServiceOutput<RegisterResponse> register(
            final IServiceInput<RegisterInputDTO> input) {

        final RegisterRequest registerRequest = input.getInput()
                .getRegisterRequest();

        validateRegistrationPasswords(
                registerRequest);

        final String encodedPassword = passwordEncoder.encode(
                registerRequest.getPassword());

        final UserRequest userRequest = new UserRequest();

        userRequest.setUsername(
                registerRequest.getUsername());

        userRequest.setFirstName(
                registerRequest.getFirstName());

        userRequest.setLastName(
                registerRequest.getLastName());

        userRequest.setEmail(
                registerRequest.getEmail());

        userRequest.setPhoneNumber(
                registerRequest.getPhoneNumber());

        final UserInputDTO userInputDTO = new UserInputDTO();

        userInputDTO.setUserRequest(
                userRequest);

        final ServiceInput<UserInputDTO> userServiceInput = new ServiceInput<>();

        userServiceInput.setInput(
                userInputDTO);

        userServiceInput.setServiceContext(
                input.getServiceContext());

        userServiceInput.setDataContext(
                input.getDataContext());

        final UserEntity userEntity = userService.registerUser(
                userServiceInput,
                encodedPassword)
                .getOutput();

        final OtpGenerationResult otpGenerationResult = otpService.generateEmailVerificationOtp(
                userEntity.getUserNumber(),
                userEntity.getEmail().getValue(),
                input.getServiceContext());

        final String email = userEntity.getEmail().getValue();

        final String emailBody = buildVerificationEmailBody(
                userEntity.getFirstName(),
                otpGenerationResult.rawOtp());

        /*
         * sentAt is intentionally updated only after the email service
         * successfully accepts the email for delivery.
         */
        emailService.sendEmail(
                email,
                "FreshMeal Email Verification",
                emailBody);

        otpService.markEmailVerificationOtpSent(
                otpGenerationResult.verificationNumber(),
                input.getServiceContext());

        final RegisterResponse response = new RegisterResponse();

        response.setEmail(email);
        response.setVerificationRequired(true);

        return new ServiceOutput<>(response);
    }

    /**
     * Validates registration password fields.
     *
     * @param registerRequest registration request
     */
    private void validateRegistrationPasswords(
            final RegisterRequest registerRequest) {

        if (registerRequest.getPassword() == null
                || registerRequest.getPassword().isBlank()) {

            throw new IllegalArgumentException(
                    "Password is required.");
        }

        if (registerRequest.getConfirmPassword() == null
                || registerRequest.getConfirmPassword().isBlank()) {

            throw new IllegalArgumentException(
                    "Password confirmation is required.");
        }

        if (!registerRequest.getPassword()
                .equals(registerRequest.getConfirmPassword())) {

            throw new IllegalArgumentException(
                    "Password and confirm password must match.");
        }
    }

    /**
     * =================================================================================================
     * VERIFY EMAIL OTP
     * =================================================================================================
     *
     * <p>
     * Verifies the email verification OTP submitted by a newly registered user.
     * </p>
     *
     * <p>
     * A successful verification activates the user account through
     * {@link IUserService#activateUser(IServiceInput)} and sends the user a
     * welcome email.
     * </p>
     *
     * <h3>Verification Flow</h3>
     * <ul>
     * <li>Find the user using the normalized email address.</li>
     * <li>Verify the submitted OTP through {@link IOtpService}.</li>
     * <li>Activate the user through {@link IUserService}.</li>
     * <li>Send the welcome email.</li>
     * <li>Return the email verification result.</li>
     * </ul>
     *
     * <p>
     * OTP validation, expiration, attempt limits and lockout remain the
     * responsibility of the OTP service.
     * </p>
     *
     * @param input service input containing email and verification OTP
     * @return email verification response
     */
    @Override
    public IServiceOutput<VerifyEmailOtpResponse> verifyEmailOtp(final IServiceInput<VerifyEmailOtpInputDTO> input) {

        final VerifyEmailOtpInputDTO verifyInput = input.getInput();

        final VerifyEmailOtpRequest request = verifyInput.getVerifyEmailOtpRequest();

        /*
         * Normalize the email before performing the user lookup.
         *
         * Email normalization belongs to the Authentication workflow boundary;
         * the EmailAddress value object remains responsible only for representing
         * an email address.
         */
        final String email = FreshMealUtilities.normalizeEmail(request.getEmail().getValue());

        /*
         * Locate the user associated with the verification request.
         */
        final EmailRequest emailRequest = new EmailRequest();

        emailRequest.setEmail(EmailAddress.toEmailAddress(email));

        final IServiceInput<EmailRequest> userServiceInput = new ServiceInput<>();

        userServiceInput.setInput(emailRequest);
        userServiceInput.setServiceContext(input.getServiceContext());

        final UserEntity userEntity = userService
                .loadUserByEmail(userServiceInput)
                .getOutput();

        /*
         * Verify the OTP using the user's business identifier.
         *
         * OTP expiration, used-state, attempt limits and lockout are handled
         * entirely by the OTP service.
         */
        otpService.verifyEmailVerificationOtp(
                userEntity.getUserNumber(),
                email,
                request.getOtp(),
                input.getServiceContext());

        /*
         * The OTP has been successfully verified.
         *
         * Delegate account activation to the User module rather than modifying
         * UserEntity directly from AuthenticationService.
         */
        final UserNumberRequest userNumberRequest = new UserNumberRequest();

        userNumberRequest.setUserNumber(userEntity.getUserNumber());

        final IServiceInput<UserNumberRequest> activationInput = new ServiceInput<>();

        activationInput.setInput(userNumberRequest);
        activationInput.setServiceContext(input.getServiceContext());

        userService.activateUser(activationInput);

        /*
         * Send the welcome email only after successful account activation.
         */
        final String firstName = FreshMealUtilities.hasText(
                userEntity.getFirstName())
                        ? userEntity.getFirstName()
                        : userEntity.getUsername();

        final String welcomeEmailBody = buildWelcomeEmailBody(firstName);

        emailService.sendEmail(
                email,
                "Welcome to FreshMeal",
                welcomeEmailBody);

        /*
         * Build the authentication response.
         *
         * No JWT or automatic login is performed here.
         */
        final VerifyEmailOtpResponse response = new VerifyEmailOtpResponse();

        response.setEmail(email);

        response.setEmailVerified(true);

        return new ServiceOutput<>(response);
    }

    @Override
    public IServiceOutput<RegisterResponse> resendEmailOtp(
            final IServiceInput<ResendEmailOtpInputDTO> input) {

        final ResendEmailOtpInputDTO resendInput = input.getInput();

        final ResendEmailOtpRequest request = resendInput.getResendEmailOtpRequest();

        /*
         * Normalize the email before performing the user lookup.
         */
        final String email = FreshMealUtilities.normalizeEmail(
                request.getEmail().getValue());

        /*
         * Find the user associated with the email address.
         */
        final EmailRequest emailRequest = new EmailRequest();

        emailRequest.setEmail(
                EmailAddress.toEmailAddress(email));

        final IServiceInput<EmailRequest> userServiceInput = new ServiceInput<>();

        userServiceInput.setInput(emailRequest);
        userServiceInput.setServiceContext(
                input.getServiceContext());

        final UserEntity userEntity = userService
                .loadUserByEmail(userServiceInput)
                .getOutput();

        /*
         * An already verified account must not receive another
         * email-verification OTP.
         */
        if (userEntity.isEmailVerified()) {

            throw new BusinessException(AuthenticationErrorConstants.EMAIL_VERIFICATION_FAILED);
        }

        /*
         * Generate a new OTP.
         *
         * IOtpService owns the resend cooldown, active OTP replacement,
         * expiration and attempt-limit policies.
         */
        final OtpGenerationResult otpGenerationResult = otpService.generateEmailVerificationOtp(
                userEntity.getUserNumber(),
                email,
                input.getServiceContext());

        /*
         * Send the newly generated OTP to the user's email address.
         */
        final String firstName = FreshMealUtilities.hasText(
                userEntity.getFirstName())
                        ? userEntity.getFirstName()
                        : userEntity.getUsername();

        final String emailBody = buildVerificationEmailBody(
                firstName,
                otpGenerationResult.rawOtp());

        emailService.sendEmail(
                email,
                "FreshMeal Email Verification",
                emailBody);

        /*
         * Mark the exact OTP as successfully sent.
         *
         * sentAt must only be populated after the email dispatch succeeds.
         */
        otpService.markEmailVerificationOtpSent(
                otpGenerationResult.verificationNumber(),
                input.getServiceContext());

        /*
         * Return the same registration-oriented response structure.
         *
         * The raw OTP is never returned to the client.
         */
        final RegisterResponse response = new RegisterResponse();

        response.setEmail(email);

        response.setVerificationRequired(true);

        return new ServiceOutput<>(
                response);
    }

    @Override
    public IServiceOutput<Boolean> logout(
            final IServiceInput<LogoutInputDTO> input) {

        final IServiceContext serviceContext = input.getServiceContext();

        final Object tokenAttribute = serviceContext.getAttribute(
                DataContext.CURRENT_ACCESS_TOKEN);

        if (!(tokenAttribute instanceof String token)
                || token.isBlank()) {

            SecurityContextHolder.clearContext();

            return new ServiceOutput<>(Boolean.TRUE);
        }

        try {

            /*
             * The logout endpoint is an authenticated access-token operation.
             * Validate the token again at the business boundary rather than
             * trusting only the presence of a DataContext attribute.
             */
            if (!tokenService.isAccessTokenValid(token)) {

                SecurityContextHolder.clearContext();

                return new ServiceOutput<>(Boolean.TRUE);
            }

            final String sessionId = tokenService.getSessionId(token);

            final String userNumber = tokenService.getUserNumber(token);

            if (!hasText(sessionId)
                    || !hasText(userNumber)) {

                SecurityContextHolder.clearContext();

                return new ServiceOutput<>(Boolean.TRUE);
            }

            /*
             * Revoke the complete authentication session.
             *
             * This invalidates both the access token and refresh token associated
             * with this login session.
             */
            tokenRevocationService.revokeSession(
                    sessionId,
                    userNumber,
                    serviceContext);

            /*
             * Close the LoginHistory record associated with this exact session.
             */
            LoginHistoryEntity loginHistory = loginHistoryService
                    .loadLoginHistoryBySessionId(
                            new ServiceInput<>(
                                    sessionId,
                                    serviceContext))
                    .getOutput();

            if (loginHistory != null && loginHistory.getLoginHistoryNumber() != null) {

                LoginHistoryNumberRequest historyRequest = new LoginHistoryNumberRequest();

                historyRequest.setLoginHistoryNumber(
                        loginHistory.getLoginHistoryNumber());

                loginHistoryService.recordLogout(
                        new ServiceInput<>(
                                historyRequest,
                                serviceContext));
            }

            /*
             * Remove authentication from the current request.
             */
            SecurityContextHolder.clearContext();

            return new ServiceOutput<>(Boolean.TRUE);

        } catch (RuntimeException exception) {

            /*
             * Always clear the current security context when logout processing
             * exits unexpectedly.
             */
            SecurityContextHolder.clearContext();

            throw exception;
        }
    }

    // =========================================================================
    // Refresh Token
    // =========================================================================

    /**
     * Refreshes an authenticated user's token pair.
     *
     * <p>
     * The supplied token must be a valid, unexpired refresh token. Both the
     * individual refresh token and its associated authentication session are
     * checked for revocation before new tokens are issued.
     * </p>
     *
     * <p>
     * Refresh-token rotation is used so that the previously supplied refresh
     * token cannot be reused after a successful refresh operation.
     * </p>
     *
     * <p>
     * The authentication {@code sessionId} remains unchanged during rotation,
     * while both newly generated JWTs receive new token identifiers
     * ({@code jti} values).
     * </p>
     *
     * <h3>Security Model</h3>
     * 
     * <pre>
     * userNumber -> identifies the user
     * sessionId  -> identifies the authentication session
     * jti        -> identifies the exact JWT
     * </pre>
     *
     * @param input refresh-token service input
     * @return newly generated access and refresh tokens
     */
    @Override
    public IServiceOutput<TokenResponse> refreshToken(
            final IServiceInput<RefreshTokenInputDTO> input) {

        final RefreshTokenInputDTO inputDTO = input.getInput();

        final String refreshToken = inputDTO.getRefreshTokenRequest()
                .getRefreshToken();

        /*
         * The DTO is normally validated at the controller boundary. This
         * defensive check ensures the service never attempts to parse an empty
         * token.
         */
        if (refreshToken == null || refreshToken.isBlank()) {

            throw new BusinessException(
                    AuthenticationErrorConstants.REFRESH_TOKEN_REQUIRED);
        }

        /*
         * Validate the JWT signature, expiration and token purpose.
         *
         * isRefreshTokenValid() accepts only JWTs whose tokenType claim is
         * REFRESH.
         */
        if (!tokenService.isRefreshTokenValid(refreshToken)) {

            throw new BusinessException(
                    AuthenticationErrorConstants.INVALID_REFRESH_TOKEN);
        }

        /*
         * Check whether this exact refresh token has already been revoked.
         *
         * This is what makes refresh-token rotation effective. Once the old
         * refresh token is revoked, replaying it will fail.
         */
        if (tokenRevocationService.isTokenRevoked(refreshToken)) {

            throw new BusinessException(
                    AuthenticationErrorConstants.REFRESH_TOKEN_REVOKED);
        }

        final String userNumber = tokenService.getUserNumber(refreshToken);

        final String sessionId = tokenService.getSessionId(refreshToken);

        /*
         * A refresh token without a valid authentication identity or session
         * cannot participate in the refresh workflow.
         */
        if (userNumber == null || userNumber.isBlank()
                || sessionId == null || sessionId.isBlank()) {

            throw new BusinessException(
                    AuthenticationErrorConstants.INVALID_REFRESH_TOKEN);
        }

        /*
         * A logout operation revokes the entire authentication session.
         *
         * Therefore a refresh token belonging to a revoked session must never
         * be allowed to create another access token.
         */
        if (tokenRevocationService.isSessionRevoked(sessionId)) {

            throw new BusinessException(
                    AuthenticationErrorConstants.REFRESH_TOKEN_REVOKED);
        }

        /*
         * Load the current active user.
         *
         * UserService remains responsible for user-domain persistence and active
         * user resolution.
         */
        final UserNumberRequest userNumberRequest = new UserNumberRequest();

        userNumberRequest.setUserNumber(userNumber);

        final IServiceInput<UserNumberRequest> userServiceInput = new ServiceInput<>(
                userNumberRequest,
                input.getServiceContext());

        final UserEntity userEntity = userService
                .loadUserByUserNumber(userServiceInput)
                .getOutput();

        /*
         * Re-check account state.
         *
         * A previously issued refresh token must not bypass a later account
         * disable/lock/verification state change.
         */
        if (!userEntity.isEnabled()) {

            throw new BusinessException(
                    AuthenticationErrorConstants.ACCOUNT_DISABLED);
        }

        if (!userEntity.isAccountNonLocked()) {

            throw new BusinessException(
                    AuthenticationErrorConstants.ACCOUNT_LOCKED);
        }

        if (!userEntity.isAccountNonExpired()) {

            throw new BusinessException(
                    AuthenticationErrorConstants.ACCOUNT_EXPIRED);
        }

        if (!userEntity.isCredentialsNonExpired()) {

            throw new BusinessException(
                    AuthenticationErrorConstants.CREDENTIALS_EXPIRED);
        }

        /*
         * Reconstruct the same security principal used during normal
         * authentication.
         */
        final UserProfile userProfile = new UserProfile(
                userEntity,
                userEntity.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getValue()))
                        .collect(Collectors.toList()));

        /*
         * IMPORTANT:
         *
         * Revoke the old refresh token before issuing its replacement.
         *
         * The session remains valid. Only this exact refresh-token jti is
         * invalidated.
         */
        tokenRevocationService.revokeToken(
                refreshToken,
                input.getServiceContext());

        /*
         * Rotate the token pair while preserving the authentication session.
         */
        final String newAccessToken = tokenService.generateAccessToken(
                userProfile,
                sessionId);

        final String newRefreshToken = tokenService.generateRefreshToken(
                userProfile,
                sessionId);

        final TokenResponse tokenResponse = new TokenResponse();

        tokenResponse.setAccessToken(newAccessToken);
        tokenResponse.setRefreshToken(newRefreshToken);
        tokenResponse.setLoginSessionId(sessionId);
        tokenResponse.setTokenType("Bearer");
        tokenResponse.setExpiresIn(
                tokenService.getAccessTokenExpirationSeconds());

        return new ServiceOutput<>(tokenResponse);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<Boolean> changePassword(
            final IServiceInput<ChangePasswordInputDTO> input) {

        final ChangePasswordInputDTO inputDTO = input.getInput();

        final String currentPassword = inputDTO.getChangePasswordRequest().getCurrentPassword();

        final String newPassword = inputDTO.getChangePasswordRequest().getNewPassword();

        if (currentPassword == null || currentPassword.isBlank()) {
            throw new BusinessException(
                    AuthenticationErrorConstants.NEW_PASSWORD_REQUIRED);
        }

        if (newPassword == null || newPassword.isBlank()) {
            throw new BusinessException(
                    AuthenticationErrorConstants.NEW_PASSWORD_REQUIRED);
        }

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal() instanceof UserProfile userProfile)) {

            throw new BusinessException(
                    AuthenticationErrorConstants.AUTHENTICATION_REQUIRED);
        }

        final String userNumber = userProfile.getUserNumber();

        if (!hasText(userNumber)) {
            throw new BusinessException(
                    AuthenticationErrorConstants.AUTHENTICATION_REQUIRED);
        }

        final UserNumberRequest userNumberRequest = new UserNumberRequest();

        userNumberRequest.setUserNumber(userNumber);

        final IServiceInput<UserNumberRequest> userServiceInput = new ServiceInput<>(
                userNumberRequest,
                input.getServiceContext());

        final UserEntity userEntity = userService.loadUserByUserNumber(userServiceInput)
                .getOutput();

        if (userEntity == null) {
            throw new BusinessException(
                    UserErrorConstants.USER_NOT_FOUND);
        }

        /*
         * Verify the current password against the encoded password stored
         * for the authenticated user.
         */
        if (!passwordEncoder.matches(
                currentPassword,
                userEntity.getPassword())) {

            throw new BusinessException(
                    AuthenticationErrorConstants.INVALID_NEW_PASSWORD);
        }

        /*
         * Prevent changing the password to the same password.
         */
        if (passwordEncoder.matches(
                newPassword,
                userEntity.getPassword())) {

            throw new BusinessException(
                    AuthenticationErrorConstants.NEW_PASSWORD_SAME_AS_OLD_PASSWORD);
        }

        /*
         * Apply the existing FreshMeal password validation rules.
         */
        validatePassword(newPassword);

        final String encodedPassword = passwordEncoder.encode(newPassword);

        final UpdatePasswordInputDTO updatePasswordInput = new UpdatePasswordInputDTO(
                userNumber,
                encodedPassword);

        userService.updatePassword(
                new ServiceInput<>(
                        updatePasswordInput,
                        input.getServiceContext()));

        /*
         * Password change invalidates every existing authentication session.
         */
        final IServiceOutput<List<LoginHistoryEntity>> activeSessions = loginHistoryService.loadActiveLoginHistories(
                new ServiceInput<>(
                        userNumberRequest,
                        input.getServiceContext()));

        if (activeSessions.getOutput() != null) {

            activeSessions.getOutput().forEach(loginHistory -> {

                final String sessionId = loginHistory.getSessionId();

                if (hasText(sessionId)) {

                    tokenRevocationService.revokeSession(
                            sessionId,
                            userNumber,
                            input.getServiceContext());
                }
            });
        }

        SecurityContextHolder.clearContext();

        return new ServiceOutput<>(Boolean.TRUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<Boolean> forgotPassword(
            final IServiceInput<ForgotPasswordInputDTO> input) {

        final ForgotPasswordInputDTO inputDTO = input.getInput();

        final String requestedEmail = inputDTO.getForgotPasswordRequest().getEmail().getValue();

        /*
         * Always normalize the email before performing any lookup.
         */
        final String email = FreshMealUtilities.normalizeEmail(requestedEmail);

        /*
         * Password recovery must never disclose whether an account exists.
         *
         * If the email does not belong to a FreshMeal account, simply return the
         * same generic response as a successful recovery request.
         */
        final EmailRequest emailRequest = new EmailRequest();

        emailRequest.setEmail(
                EmailAddress.toEmailAddress(email));

        final IServiceInput<EmailRequest> userServiceInput = new ServiceInput<>(
                emailRequest,
                input.getServiceContext());

        final UserEntity userEntity;

        try {

            userEntity = userService
                    .loadUserByEmail(userServiceInput)
                    .getOutput();

        } catch (ResourceNotFoundException exception) {

            return new ServiceOutput<>(Boolean.TRUE);
        }

        /*
         * Only active and usable accounts should receive password-reset
         * instructions.
         *
         * We still return the same generic response for non-usable accounts.
         */
        if (!userEntity.isEnabled()
                || !userEntity.isAccountNonLocked()
                || !userEntity.isAccountNonExpired()
                || !userEntity.isCredentialsNonExpired()) {

            return new ServiceOutput<>(Boolean.TRUE);
        }

        /*
         * Invalidate all previously active reset tokens for this user.
         *
         * A new password-recovery request therefore invalidates older recovery
         * credentials.
         */
        final Query activeResetTokenQuery = Query.query(
                new Criteria().andOperator(
                        Criteria.where("userNumber")
                                .is(userEntity.getUserNumber()),
                        Criteria.where("used")
                                .is(false),
                        Criteria.where("revoked")
                                .is(false)));

        final List<PasswordResetTokenEntity> activeTokens = passwordResetTokenRepository
                .findAll(activeResetTokenQuery);

        final LocalDateTime now = AppCalendar.getBusinessLocalDateTime();

        for (final PasswordResetTokenEntity resetToken : activeTokens) {

            resetToken.setRevoked(true);
            resetToken.setRevokedAt(now);
            resetToken.setUpdatedAt(now);

            if (input.getServiceContext().getUserProfile() != null) {
                resetToken.setUpdatedBy(
                        input.getServiceContext()
                                .getUserProfile()
                                .getUserNumber());
            } else {
                resetToken.setUpdatedBy(
                        RoleType.ADMIN.getLabel());
            }

            passwordResetTokenRepository.save(resetToken);
        }

        /*
         * Generate a cryptographically secure random reset token.
         *
         * The raw token exists only in memory and is used to construct the email.
         */
        final String rawResetToken = generatePasswordResetToken();

        /*
         * Store only the SHA-256 hash of the reset token.
         */
        final String tokenHash = hashPasswordResetToken(rawResetToken);

        /*
         * Generate the business-facing reset number using the existing FreshMeal
         * database sequence infrastructure.
         */
        final long sequence = databaseSequenceService.generateSequence(
                input.getServiceContext(),
                SequenceConstants.PASSWORD_RESET_TOKEN_SEQUENCE);

        final String resetNumber = String.format(
                SequenceConstants.PASSWORD_RESET_TOKEN_NUMBER_PATTERN,
                sequence);

        final PasswordResetTokenEntity resetToken = (PasswordResetTokenEntity) EntityFactory
                .createEntity(EntityName.PASSWORD_RESET_TOKEN_ENTITY);

        resetToken.setResetNumber(resetNumber);
        resetToken.setUserNumber(userEntity.getUserNumber());
        resetToken.setEmail(email);
        resetToken.setTokenHash(tokenHash);

        /*
         * Password reset tokens are intentionally short-lived.
         */
        resetToken.setExpiresAt(
                now.plusMinutes(15));

        /*
         * sentAt remains null until email delivery succeeds.
         */
        resetToken.setSentAt(null);

        resetToken.setUsed(false);
        resetToken.setUsedAt(null);
        resetToken.setRevoked(false);
        resetToken.setRevokedAt(null);

        resetToken.setCreatedAt(now);
        resetToken.setUpdatedAt(now);

        if (input.getServiceContext().getUserProfile() != null) {
            resetToken.setCreatedBy(
                    input.getServiceContext()
                            .getUserProfile()
                            .getUserNumber());

            resetToken.setUpdatedBy(
                    input.getServiceContext()
                            .getUserProfile()
                            .getUserNumber());
        } else {
            resetToken.setCreatedBy(
                    RoleType.ADMIN.getLabel());

            resetToken.setUpdatedBy(
                    RoleType.ADMIN.getLabel());
        }

        passwordResetTokenRepository.save(resetToken);

        /*
         * Build the reset email.
         *
         * The raw token is never persisted or logged.
         */
        final String firstName = FreshMealUtilities.hasText(userEntity.getFirstName())
                ? userEntity.getFirstName()
                : userEntity.getUsername();

        final String emailBody = buildPasswordResetEmailBody(
                firstName,
                rawResetToken);

        /*
         * Mark sentAt only after successful email dispatch.
         */
        emailService.sendEmail(
                email,
                "FreshMeal Password Reset",
                emailBody);

        resetToken.setSentAt(
                AppCalendar.getBusinessLocalDateTime());

        resetToken.setUpdatedAt(
                AppCalendar.getBusinessLocalDateTime());

        passwordResetTokenRepository.save(resetToken);

        /*
         * Always return a generic success response.
         */
        return new ServiceOutput<>(Boolean.TRUE);
    }

    @Override
    public IServiceOutput<Boolean> resetPassword(
            final IServiceInput<ResetPasswordInputDTO> input) {

        throw new UnsupportedOperationException(
                "Unimplemented method 'resetPassword'");
    }

    /**
     * Builds the welcome email sent after successful email verification.
     *
     * @param firstName user's first name
     * @return welcome email body
     */
    private String buildWelcomeEmailBody(final String firstName) {

        return String.format(
                """
                        Hello %s,

                        Welcome to FreshMeal!

                        Your email address has been successfully verified and your
                        FreshMeal account is now active.

                        You can now log in and start using FreshMeal.

                        Regards,
                        FreshMeal Team
                        """,
                firstName);
    }

    /**
     * Builds the email body used for email verification.
     *
     * <p>
     * The raw OTP is intentionally used only while constructing the transient
     * email body. It must never be persisted or logged.
     * </p>
     *
     * @param firstName user's first name
     * @param otp       generated raw OTP
     * @return email body
     */
    private String buildVerificationEmailBody(final String firstName, final String otp) {

        final String recipientName = firstName == null
                || firstName.isBlank()
                        ? "there"
                        : firstName;

        return """
                Hello %s,

                Welcome to FreshMeal!

                Your email verification OTP is:

                %s

                This OTP is valid for 10 minutes.

                If you did not create a FreshMeal account,
                please ignore this email.

                Regards,
                FreshMeal Team
                """.formatted(
                recipientName,
                otp);
    }

    /**
     * ============================================================================
     * LOGIN HISTORY
     * ============================================================================
     */

    /**
     * Records a successful authentication event.
     *
     * <p>
     * The login history record uses the same authentication-session identifier
     * that was embedded into the access and refresh tokens during login.
     * </p>
     *
     * <p>
     * No additional JWT is generated while recording login history. The
     * authentication operation must have exactly one session identifier and one
     * access/refresh token pair.
     * </p>
     *
     * @param userProfile authenticated user profile
     * @param sessionId   authentication-session identifier
     * @param input       original authentication service input
     */
    private void recordSuccessfulLogin(
            final UserProfile userProfile,
            final String sessionId,
            final IServiceInput<LoginInputDTO> input) {

        final LoginHistoryInputDTO historyInput = new LoginHistoryInputDTO();

        historyInput.setUserNumber(
                userProfile.getUserNumber());

        historyInput.setLoginStatus(
                LoginStatus.SUCCESS);

        historyInput.setLoginTime(
                AppCalendar.getBusinessLocalDateTime());

        historyInput.setSessionId(sessionId);

        /*
         * Request metadata is already available through the request-scoped
         * DataContext.
         */
        historyInput.setIpAddress(
                (String) input.getServiceContext()
                        .getAttribute(DataContext.IP_ADDRESS));

        historyInput.setUserAgent(
                (String) input.getServiceContext()
                        .getAttribute(DataContext.USER_AGENT));

        historyInput.setLoginServerName(null);

        final IServiceInput<LoginHistoryInputDTO> historyServiceInput = new ServiceInput<>(
                historyInput,
                input.getServiceContext());

        loginHistoryService.createLoginHistory(
                historyServiceInput);
    }

    /**
     * Records a failed authentication event when the supplied identifier can be
     * safely associated with an existing user.
     *
     * <p>
     * Authentication itself remains completely controlled by Spring Security.
     * This method exists only for audit recording and must never replace or alter
     * the original authentication exception.
     * </p>
     *
     * @param identifier authentication identifier supplied by the client
     * @param exception  authentication failure
     * @param input      original authentication service input
     */
    private void recordFailedLogin(
            final String identifier,
            final AuthenticationException exception,
            final IServiceInput<LoginInputDTO> input) {

        final LoginStatus loginStatus = resolveLoginStatus(exception);

        /*
         * At this stage the identifier may represent:
         *
         * 1. an existing username,
         * 2. an existing email address, or
         * 3. an identifier that does not belong to any account.
         *
         * We only create user-linked history when the account can be resolved.
         */
        try {

            final UserEntity userEntity;

            if (identifier != null && identifier.contains("@")) {

                final EmailRequest emailRequest = new EmailRequest();

                emailRequest.setEmail(
                        EmailAddress.toEmailAddress(
                                FreshMealUtilities.normalizeEmail(identifier)));

                final IServiceInput<EmailRequest> userInput = new ServiceInput<>(
                        emailRequest,
                        input.getServiceContext());

                userEntity = userService.loadUserByEmail(userInput).getOutput();

            } else {

                final UsernameRequest usernameRequest = new UsernameRequest();

                usernameRequest.setUsername(identifier);

                final IServiceInput<UsernameRequest> userInput = new ServiceInput<>(
                        usernameRequest,
                        input.getServiceContext());

                userEntity = userService.loadUserByUsername(userInput).getOutput();
            }

            if (userEntity == null) {
                return;
            }

            final LoginHistoryInputDTO historyInput = new LoginHistoryInputDTO();

            historyInput.setUserNumber(
                    userEntity.getUserNumber());

            historyInput.setLoginStatus(loginStatus);

            historyInput.setLoginTime(
                    AppCalendar.getBusinessLocalDateTime());

            historyInput.setSessionId(null);
            historyInput.setIpAddress(null);
            historyInput.setUserAgent(null);
            historyInput.setLoginServerName(null);

            final IServiceInput<LoginHistoryInputDTO> historyServiceInput = new ServiceInput<>(
                    historyInput,
                    input.getServiceContext());

            loginHistoryService.createLoginHistory(
                    historyServiceInput);

        } catch (RuntimeException ignored) {

            /*
             * Login auditing must never replace the original authentication
             * failure. The caller rethrows the original AuthenticationException.
             */
        }
    }

    /**
     * Resolves the domain login status represented by a Spring Security
     * authentication exception.
     *
     * @param exception Spring Security authentication exception
     * @return corresponding FreshMeal login status
     */
    private LoginStatus resolveLoginStatus(
            final AuthenticationException exception) {

        if (exception instanceof LockedException) {

            return LoginStatus.LOCKED;
        }

        if (exception instanceof DisabledException) {

            return LoginStatus.DISABLED;
        }

        if (exception instanceof AccountExpiredException) {

            return LoginStatus.ACCOUNT_EXPIRED;
        }

        if (exception instanceof CredentialsExpiredException) {

            return LoginStatus.CREDENTIALS_EXPIRED;
        }

        if (exception instanceof BadCredentialsException) {

            return LoginStatus.FAILED;
        }

        /*
         * Authentication failures that do not map to a more specific
         * account-state category are treated as failed authentication.
         */
        return LoginStatus.FAILED;
    }

    /**
     * Validates the authentication-session identifier.
     *
     * @param sessionId authentication-session identifier.
     *
     * @throws IllegalArgumentException when the session identifier is empty.
     */
    private void validateSessionId(String sessionId) {

        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException(
                    "Session ID must not be empty.");
        }
    }

    private boolean hasText(String str) {
        return str != null && !str.isBlank();
    }

    /**
     * =========================================================================
     * Password Validation
     * =========================================================================
     */

    /**
     * Validates the password supplied during authentication-sensitive
     * operations.
     *
     * <p>
     * Passwords must satisfy the minimum security requirements before they are
     * encoded and persisted.
     * </p>
     *
     * @param password raw password to validate
     */
    private void validatePassword(final String password) {

        if (password == null || password.isBlank()) {
            throw new BusinessException(
                    AuthenticationErrorConstants.NEW_PASSWORD_REQUIRED);
        }

        if (password.length() < 6) {
            throw new BusinessException(
                    AuthenticationErrorConstants.PASSWORD_TOO_SHORT);
        }

        if (password.length() > 16) {
            throw new BusinessException(
                    AuthenticationErrorConstants.PASSWORD_TOO_LONG);
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new BusinessException(
                    AuthenticationErrorConstants.PASSWORD_UPPERCASE_REQUIRED);
        }

        if (!password.matches(".*[a-z].*")) {
            throw new BusinessException(
                    AuthenticationErrorConstants.PASSWORD_LOWERCASE_REQUIRED);
        }

        if (!password.matches(".*\\d.*")) {
            throw new BusinessException(
                    AuthenticationErrorConstants.PASSWORD_DIGIT_REQUIRED);
        }

        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            throw new BusinessException(
                    AuthenticationErrorConstants.PASSWORD_SPECIAL_CHARACTER_REQUIRED);
        }
    }

    /**
     * Generates a cryptographically secure password-reset token.
     *
     * <p>
     * The generated token is intentionally returned only to the caller so that
     * it can be included in the password-reset email. Only its hash is persisted.
     * </p>
     *
     * @return cryptographically secure reset token
     */
    private String generatePasswordResetToken() {

        final byte[] tokenBytes = new byte[32];

        new SecureRandom().nextBytes(tokenBytes);

        return HexFormat.of().formatHex(tokenBytes);
    }

    /**
     * Creates the SHA-256 hash used to persist a password-reset token.
     *
     * @param rawToken raw reset token
     * @return hexadecimal SHA-256 hash
     */
    private String hashPasswordResetToken(final String rawToken) {

        try {

            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            final byte[] digest = messageDigest.digest(
                    rawToken.getBytes(
                            java.nio.charset.StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(digest);

        } catch (NoSuchAlgorithmException exception) {

            throw new IllegalStateException(
                    "SHA-256 algorithm is not available.",
                    exception);
        }
    }

    /**
     * Builds the password-reset email body.
     *
     * <p>
     * The raw reset token is used only for email delivery and must never be
     * persisted or logged.
     * </p>
     *
     * @param firstName  user's first name
     * @param resetToken raw password-reset token
     * @return password-reset email body
     */
    private String buildPasswordResetEmailBody(
            final String firstName,
            final String resetToken) {

        final String recipientName = FreshMealUtilities.hasText(firstName)
                ? firstName
                : "there";

        return """
                Hello %s,

                We received a request to reset your FreshMeal password.

                Your password reset token is:

                %s

                This token is valid for 15 minutes and can be used only once.

                If you did not request a password reset, please ignore this email.
                Your password will not be changed unless the reset process is
                successfully completed.

                Regards,
                FreshMeal Team
                """.formatted(
                recipientName,
                resetToken);
    }
}
