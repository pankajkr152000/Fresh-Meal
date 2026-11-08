package com.foodies.freshmeal.authentication.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.foodies.freshmeal.authentication.constants.AuthenticationErrorConstants;
import com.foodies.freshmeal.authentication.entity.EmailVerificationOtpEntity;
import com.foodies.freshmeal.authentication.repository.IEmailVerificationOtpRepository;
import com.foodies.freshmeal.authentication.service.IOtpService;
import com.foodies.freshmeal.authentication.valueObject.OtpGenerationResult;
import com.foodies.freshmeal.authentication.valueObject.OtpVerificationResult;
import com.foodies.freshmeal.common.constants.RepositoryConstants;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.exception.BusinessException;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.sequence.service.IDatabaseSequenceService;
import com.foodies.freshmeal.common.util.FreshMealUtilities;

/**
 * =================================================================================================
 * OTP SERVICE IMPLEMENTATION
 * =================================================================================================
 *
 * <p>
 * Provides secure email verification OTP generation, verification, and
 * lifecycle management for the FreshMeal Authentication module.
 * </p>
 *
 * <h3>Security Rules</h3>
 * <ul>
 * <li>OTP length: <b>6 digits</b>.</li>
 * <li>OTP validity: <b>10 minutes</b>.</li>
 * <li>Maximum verification attempts: <b>5</b>.</li>
 * <li>Resend cooldown: <b>60 seconds</b>.</li>
 * <li>Maximum-attempt lockout: <b>1 hour</b>.</li>
 * <li>Only the hashed OTP is persisted.</li>
 * <li>The raw OTP exists only during the generation and email-delivery
 * workflow.</li>
 * <li>A successfully verified OTP can never be reused.</li>
 * <li>Generating a new OTP revokes the previously active OTP.</li>
 * </ul>
 *
 * <h3>Attempt Handling</h3>
 * <p>
 * Every incorrect OTP increments the attempt count. The number of remaining
 * attempts is returned to the caller until the maximum number of attempts is
 * reached.
 * </p>
 *
 * <p>
 * Once the maximum number of attempts is exhausted, the OTP enters a
 * <b>one-hour lockout</b>. The exhausted OTP cannot become valid again after
 * the cooldown. A new OTP must be requested after the lockout period.
 * </p>
 *
 * <h3>Resend Handling</h3>
 * <p>
 * Resend cooldown is calculated using {@code sentAt}, not {@code createdAt}.
 * This ensures that the cooldown starts only after the OTP has been
 * successfully dispatched through the email service.
 * </p>
 *
 * =================================================================================================
 */
@Service
public class OtpServiceImpl implements IOtpService {

    /**
     * Length of the generated OTP.
     */
    private static final int OTP_LENGTH = 6;

    /**
     * Smallest possible six-digit OTP.
     */
    private static final int OTP_MINIMUM = 100_000;

    /**
     * Upper exclusive bound for OTP generation.
     */
    private static final int OTP_BOUND = 1_000_000;

    /**
     * OTP validity period in minutes.
     */
    private static final int OTP_EXPIRATION_MINUTES = 10;

    /**
     * Maximum number of incorrect OTP verification attempts.
     */
    private static final int OTP_MAX_ATTEMPTS = 5;

    /**
     * Minimum interval between successfully sent OTPs.
     */
    private static final int RESEND_COOLDOWN_SECONDS = 60;

    /**
     * Lockout duration after maximum verification attempts are exhausted.
     */
    private static final int MAX_ATTEMPT_COOLDOWN_HOURS = 1;

    /**
     * Sequence name used for email verification OTP records.
     */
    private static final String OTP_SEQUENCE_NAME = "EMAIL_VERIFICATION_OTP";

    /**
     * Prefix used for OTP verification business numbers.
     */
    private static final String OTP_NUMBER_PREFIX = "EVOTP-";

    private final SecureRandom secureRandom = new SecureRandom();

    private final IEmailVerificationOtpRepository otpRepository;

    private final IDatabaseSequenceService sequenceService;

    private final PasswordEncoder passwordEncoder;

    /**
     * Creates the OTP service.
     *
     * @param otpRepository   repository for email verification OTP records
     * @param sequenceService database sequence service
     * @param passwordEncoder password encoder used to hash and verify OTPs
     */
    public OtpServiceImpl(final IEmailVerificationOtpRepository otpRepository,
            final IDatabaseSequenceService sequenceService, final PasswordEncoder passwordEncoder) {

        this.otpRepository = otpRepository;
        this.sequenceService = sequenceService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * A newly generated OTP is persisted with {@code sentAt = null}. The
     * authentication service is responsible for sending the OTP through
     * {@code IEmailService} and subsequently calling
     * {@link #markEmailVerificationOtpSent(String, IServiceContext)} after
     * successful dispatch.
     * </p>
     */
    @Override
    public OtpGenerationResult generateEmailVerificationOtp(final String userNumber, final String email,
            final IServiceContext serviceContext) {

        validateGenerationInput(userNumber, email);

        final String normalizedEmail = FreshMealUtilities.normalizeEmail(email);

        final LocalDateTime now = LocalDateTime.now();

        final EmailVerificationOtpEntity activeOtp = findActiveOtp(userNumber, normalizedEmail);

        validateOtpGenerationAllowed(activeOtp, now);

        /*
         * The previous active OTP is invalidated before creating the new one.
         */
        revokeActiveEmailVerificationOtp(userNumber, serviceContext);

        final String otp = generateOtp();

        final long sequence = sequenceService.generateSequence(serviceContext, OTP_SEQUENCE_NAME);

        final String verificationNumber = OTP_NUMBER_PREFIX + sequence;

        final EmailVerificationOtpEntity otpEntity = (EmailVerificationOtpEntity) EmailVerificationOtpEntity.create();

        otpEntity.setVerificationNumber(verificationNumber);

        otpEntity.setUserNumber(userNumber);

        otpEntity.setEmail(normalizedEmail);

        /*
         * Never persist the raw OTP.
         */
        otpEntity.setOtpHash(passwordEncoder.encode(otp));

        /*
         * The OTP validity period starts when the OTP is generated. sentAt remains null
         * until email dispatch succeeds.
         */
        otpEntity.setSentAt(null);

        otpEntity.setExpiresAt(now.plusMinutes(OTP_EXPIRATION_MINUTES));

        otpEntity.setAttemptCount(0);

        otpEntity.setMaxAttempts(OTP_MAX_ATTEMPTS);

        otpEntity.setAttemptsExceededAt(null);

        otpEntity.setUsed(false);

        otpEntity.setUsedAt(null);

        otpEntity.setRevoked(false);

        otpEntity.setRevokedAt(null);

        otpRepository.save(otpEntity);

        /*
         * Return the persistent identifier together with the raw OTP.
         *
         * rawOtp is returned only for transient email delivery and must never be
         * persisted, logged, or exposed through an API response.
         */
        return new OtpGenerationResult(verificationNumber, otp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OtpVerificationResult verifyEmailVerificationOtp(final String userNumber, final String email,
            final String otp, final IServiceContext serviceContext) {

        validateVerificationInput(userNumber, email, otp);

        final String normalizedEmail = FreshMealUtilities.normalizeEmail(email);

        final LocalDateTime now = LocalDateTime.now();

        final EmailVerificationOtpEntity otpEntity = findActiveOtp(userNumber, normalizedEmail);

        if (otpEntity == null) {

            throw new BusinessException(AuthenticationErrorConstants.INVALID_EMAIL_OTP);
        }

        /*
         * A used OTP must never be accepted again.
         */
        if (otpEntity.isUsed()) {

            throw new BusinessException(AuthenticationErrorConstants.EMAIL_OTP_ALREADY_USED);
        }

        /*
         * Check maximum-attempt lockout before comparing the supplied OTP.
         */
        if (isAttemptsExceeded(otpEntity)) {

            if (!isMaxAttemptCooldownExpired(otpEntity, now)) {

                throw new BusinessException(AuthenticationErrorConstants.EMAIL_OTP_LOCKED);
            }

            /*
             * The one-hour cooldown does not revive the old OTP. The caller must request a
             * new OTP.
             */
            throw new BusinessException(AuthenticationErrorConstants.EMAIL_OTP_EXPIRED);
        }

        /*
         * OTP expiration is independent of the attempt lockout.
         */
        if (isOtpExpired(otpEntity, now)) {

            throw new BusinessException(AuthenticationErrorConstants.EMAIL_OTP_EXPIRED);
        }

        /*
         * Compare the supplied raw OTP against the persisted hash.
         */
        if (!passwordEncoder.matches(otp, otpEntity.getOtpHash())) {

            handleInvalidOtpAttempt(otpEntity, now);

            throw new BusinessException(AuthenticationErrorConstants.EMAIL_OTP_INCORRECT);
        }

        /*
         * Correct OTP — permanently consume it.
         */
        otpEntity.setUsed(true);

        otpEntity.setUsedAt(now);

        otpRepository.save(otpEntity);

        return new OtpVerificationResult(true, 0);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * This method must be called only after the email service successfully accepts
     * the email for dispatch.
     * </p>
     */
    @Override
    public void markEmailVerificationOtpSent(final String verificationNumber, final IServiceContext serviceContext) {

        if (verificationNumber == null || verificationNumber.isBlank()) {

            throw new BusinessException(AuthenticationErrorConstants.EMAIL_VERIFICATION_FAILED);
        }

        final EmailVerificationOtpEntity otpEntity = otpRepository
                .markEmailVerificationOtpSent(verificationNumber, AppCalendar.getBusinessLocalDateTime(),
                        serviceContext.getUserProfile() != null
                                ? serviceContext.getUserProfile().getUserNumber()
                                : RepositoryConstants.SYSTEM_USER)
                .orElseThrow(() -> new BusinessException(AuthenticationErrorConstants.EMAIL_VERIFICATION_FAILED));
        ;

        /*
         * sentAt is populated only after successful email dispatch.
         */
        otpEntity.setSentAt(LocalDateTime.now());

        // otpRepository.save(otpEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void revokeActiveEmailVerificationOtp(final String userNumber, final IServiceContext serviceContext) {

        if (userNumber == null || userNumber.isBlank()) {

            return;
        }

        final LocalDateTime now = LocalDateTime.now();

        final Query query = new Query(new Criteria().andOperator(Criteria.where("userNumber").is(userNumber),
                Criteria.where("used").is(false), Criteria.where("revoked").is(false)));

        final List<EmailVerificationOtpEntity> activeOtps = otpRepository.findAll(query);

        for (final EmailVerificationOtpEntity otpEntity : activeOtps) {

            otpEntity.setRevoked(true);

            otpEntity.setRevokedAt(now);

            otpRepository.save(otpEntity);
        }
    }

    /**
     * Validates whether generation of another OTP is currently allowed.
     *
     * @param activeOtp currently active OTP, if any
     * @param now       current business time
     */
    private void validateOtpGenerationAllowed(final EmailVerificationOtpEntity activeOtp, final LocalDateTime now) {

        if (activeOtp == null) {
            return;
        }

        /*
         * Maximum-attempt lockout has priority over resend cooldown.
         */
        if (isAttemptsExceeded(activeOtp) && !isMaxAttemptCooldownExpired(activeOtp, now)) {

            throw new BusinessException(AuthenticationErrorConstants.EMAIL_OTP_LOCKED);
        }

        /*
         * sentAt is populated only after successful email dispatch.
         */
        if (activeOtp.getSentAt() == null) {
            return;
        }

        final LocalDateTime resendAllowedAt = activeOtp.getSentAt().plusSeconds(RESEND_COOLDOWN_SECONDS);

        if (now.isBefore(resendAllowedAt)) {

            throw new BusinessException(AuthenticationErrorConstants.EMAIL_OTP_RESEND_COOLDOWN);
        }
    }

    /**
     * Handles an incorrect OTP attempt.
     *
     * @param otpEntity active OTP
     * @param now       current business time
     * @return verification result containing the remaining attempts
     */
    private OtpVerificationResult handleInvalidOtpAttempt(final EmailVerificationOtpEntity otpEntity,
            final LocalDateTime now) {

        otpEntity.setAttemptCount(otpEntity.getAttemptCount() + 1);

        final int attemptsRemaining = Math.max(0, otpEntity.getMaxAttempts() - otpEntity.getAttemptCount());

        /*
         * The fifth failed attempt activates the one-hour lockout.
         */
        if (otpEntity.getAttemptCount() >= otpEntity.getMaxAttempts()) {

            otpEntity.setAttemptsExceededAt(now);

            otpRepository.save(otpEntity);

            throw new BusinessException(AuthenticationErrorConstants.EMAIL_OTP_ATTEMPTS_EXCEEDED);
        }

        otpRepository.save(otpEntity);

        return new OtpVerificationResult(false, attemptsRemaining);
    }

    /**
     * Finds the active OTP for a user and email address.
     *
     * @param userNumber user business identifier
     * @param email      normalized email address
     * @return active OTP or {@code null} when none exists
     */
    private EmailVerificationOtpEntity findActiveOtp(
            final String userNumber,
            final String email) {

        final Query query = new Query(
                new Criteria()
                        .andOperator(
                                Criteria.where("userNumber")
                                        .is(userNumber),
                                Criteria.where("email")
                                        .is(email),
                                Criteria.where("used")
                                        .is(false),
                                Criteria.where("revoked")
                                        .is(false)));

        return otpRepository.findOne(query).orElse(null);
    }

    /**
     * Determines whether the maximum number of attempts has been exhausted.
     *
     * @param otpEntity OTP entity
     * @return {@code true} when the maximum has been reached
     */
    private boolean isAttemptsExceeded(final EmailVerificationOtpEntity otpEntity) {

        return otpEntity.getAttemptCount() >= otpEntity.getMaxAttempts();
    }

    /**
     * Determines whether the one-hour maximum-attempt cooldown has expired.
     *
     * @param otpEntity OTP entity
     * @param now       current business time
     * @return {@code true} when a new OTP may be requested
     */
    private boolean isMaxAttemptCooldownExpired(final EmailVerificationOtpEntity otpEntity, final LocalDateTime now) {

        if (otpEntity.getAttemptsExceededAt() == null) {
            return false;
        }

        return !now.isBefore(otpEntity.getAttemptsExceededAt().plusHours(MAX_ATTEMPT_COOLDOWN_HOURS));
    }

    /**
     * Determines whether the OTP has expired.
     *
     * @param otpEntity OTP entity
     * @param now       current business time
     * @return {@code true} when the OTP is expired
     */
    private boolean isOtpExpired(final EmailVerificationOtpEntity otpEntity, final LocalDateTime now) {

        return otpEntity.getExpiresAt() == null || !now.isBefore(otpEntity.getExpiresAt());
    }

    /**
     * Generates a cryptographically secure six-digit OTP.
     *
     * @return generated six-digit OTP
     */
    private String generateOtp() {

        final int value = OTP_MINIMUM + secureRandom.nextInt(OTP_BOUND - OTP_MINIMUM);

        return String.format("%0" + OTP_LENGTH + "d", value);
    }

    /**
     * Validates OTP generation input.
     *
     * @param userNumber user business identifier
     * @param email      email address
     */
    private void validateGenerationInput(final String userNumber, final String email) {

        if (userNumber == null || userNumber.isBlank()) {

            throw new BusinessException(AuthenticationErrorConstants.ACCOUNT_VERIFICATION_REQUIRED);
        }

        if (email == null || email.isBlank()) {

            throw new BusinessException(AuthenticationErrorConstants.EMAIL_OTP_REQUIRED);
        }
    }

    /**
     * Validates OTP verification input.
     *
     * @param userNumber user business identifier
     * @param email      email address
     * @param otp        supplied OTP
     */
    private void validateVerificationInput(final String userNumber, final String email, final String otp) {

        if (userNumber == null || userNumber.isBlank()) {

            throw new BusinessException(AuthenticationErrorConstants.ACCOUNT_VERIFICATION_REQUIRED);
        }

        if (email == null || email.isBlank()) {

            throw new BusinessException(AuthenticationErrorConstants.EMAIL_OTP_REQUIRED);
        }

        if (otp == null || otp.isBlank()) {

            throw new BusinessException(AuthenticationErrorConstants.EMAIL_OTP_REQUIRED);
        }

        if (!otp.matches("\\d{" + OTP_LENGTH + "}")) {

            throw new BusinessException(AuthenticationErrorConstants.INVALID_EMAIL_OTP);
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Generates an email verification OTP for a pending registration.
     * </p>
     *
     * <p>
     * The previous active OTP associated with the same registration is revoked
     * before the new OTP is persisted. Existing resend cooldown and maximum-attempt
     * lockout policies are applied consistently with the V1 workflow.
     * </p>
     *
     * <p>
     * The generated OTP is persisted only as a secure hash. Its raw value is
     * returned exclusively for transient email delivery.
     * </p>
     */
    @Override
    public OtpGenerationResult generateRegistrationEmailVerificationOtp(
            final String registrationNumber,
            final String email,
            final IServiceContext serviceContext) {

        /*
         * Validate registration-specific generation input.
         */
        if (registrationNumber == null || registrationNumber.isBlank()
                || serviceContext == null) {

            throw new BusinessException(
                    AuthenticationErrorConstants.ACCOUNT_VERIFICATION_REQUIRED);
        }

        if (email == null || email.isBlank()) {

            throw new BusinessException(
                    AuthenticationErrorConstants.EMAIL_OTP_REQUIRED);
        }

        /*
         * Normalize the email before querying OTP records.
         */
        final String normalizedEmail = FreshMealUtilities.normalizeEmail(email);

        /*
         * Capture the current business timestamp once.
         */
        final LocalDateTime now = AppCalendar.getBusinessLocalDateTime();

        /*
         * Find the active OTP belonging to this exact pending registration
         * and normalized email address.
         *
         * Explicitly require userNumber to be null to preserve the V2 ownership
         * invariant and prevent matching existing-user OTP records.
         */
        final Query activeOtpQuery = new Query(
                new Criteria().andOperator(
                        Criteria.where("registrationNumber").is(registrationNumber),
                        Criteria.where("userNumber").is(null),
                        Criteria.where("email").is(normalizedEmail),
                        Criteria.where("used").is(false),
                        Criteria.where("revoked").is(false)));

        final EmailVerificationOtpEntity activeOtp = otpRepository
                .findOne(activeOtpQuery)
                .orElse(null);

        /*
         * Apply the existing resend cooldown and maximum-attempt lockout policies.
         */
        validateOtpGenerationAllowed(activeOtp, now);

        /*
         * Resolve the audit actor.
         *
         * Public registration is normally unauthenticated, so use the system
         * actor when no authenticated user profile is available.
         */
        final String auditUser = serviceContext.getUserProfile() != null
                ? serviceContext.getUserProfile().getUserNumber()
                : RepositoryConstants.SYSTEM_USER;

        /*
         * Revoke previously active OTPs belonging to this registration.
         *
         * The custom repository performs this operation as a single bulk update
         * and updates the standard audit metadata.
         */
        otpRepository.revokeActiveOtpsByRegistrationNumber(
                registrationNumber,
                now,
                auditUser);

        /*
         * Generate a cryptographically secure six-digit OTP.
         */
        final String otp = generateOtp();

        /*
         * Generate a unique business-facing verification number.
         */
        final long sequence = sequenceService.generateSequence(
                serviceContext,
                OTP_SEQUENCE_NAME);

        final String verificationNumber = OTP_NUMBER_PREFIX + sequence;

        /*
         * Create the OTP entity with explicit V2 ownership.
         */
        final EmailVerificationOtpEntity otpEntity = EmailVerificationOtpEntity
                .createForRegistration(registrationNumber);

        otpEntity.setVerificationNumber(verificationNumber);
        otpEntity.setEmail(normalizedEmail);

        /*
         * Never persist the raw OTP.
         */
        otpEntity.setOtpHash(passwordEncoder.encode(otp));

        /*
         * Initialize the OTP lifecycle.
         *
         * sentAt remains null until the email service successfully accepts
         * the email for dispatch.
         */
        otpEntity.setSentAt(null);
        otpEntity.setExpiresAt(now.plusMinutes(OTP_EXPIRATION_MINUTES));

        otpEntity.setAttemptCount(0);
        otpEntity.setMaxAttempts(OTP_MAX_ATTEMPTS);
        otpEntity.setAttemptsExceededAt(null);

        otpEntity.setUsed(false);
        otpEntity.setUsedAt(null);

        otpEntity.setRevoked(false);
        otpEntity.setRevokedAt(null);

        /*
         * Enforce the exclusive OTP ownership invariant before persistence.
         */
        otpEntity.validateOwnership();

        /*
         * Persist the new registration OTP.
         */
        otpRepository.save(otpEntity);

        /*
         * Return the verification identifier and raw OTP.
         *
         * The raw OTP must only be used for email delivery. It must never be
         * logged, persisted, or included in an API response.
         */
        return new OtpGenerationResult(verificationNumber, otp);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Verifies an email verification OTP associated with a pending registration.
     * </p>
     *
     * <p>
     * The OTP must belong to the supplied registration identifier and normalized
     * email address. Successful verification atomically consumes the OTP, ensuring
     * that concurrent requests cannot reuse it.
     * </p>
     *
     * <p>
     * Incorrect attempts are tracked through an atomic repository operation to
     * prevent concurrent requests from overwriting one another's attempt counts.
     * </p>
     *
     * @param registrationNumber pending registration business identifier
     * @param email              registered email address
     * @param otp                raw OTP supplied by the applicant
     * @param serviceContext     service execution context
     * @return successful verification result
     */
    @Override
    public OtpVerificationResult verifyRegistrationEmailVerificationOtp(
            final String registrationNumber,
            final String email,
            final String otp,
            final IServiceContext serviceContext) {

        /*
         * Validate registration-specific verification input.
         */
        if (registrationNumber == null || registrationNumber.isBlank()
                || serviceContext == null) {

            throw new BusinessException(
                    AuthenticationErrorConstants.ACCOUNT_VERIFICATION_REQUIRED);
        }

        if (email == null || email.isBlank()) {

            throw new BusinessException(
                    AuthenticationErrorConstants.EMAIL_OTP_REQUIRED);
        }

        if (otp == null || otp.isBlank()) {

            throw new BusinessException(
                    AuthenticationErrorConstants.EMAIL_OTP_REQUIRED);
        }

        if (!otp.matches("\\d{" + OTP_LENGTH + "}")) {

            throw new BusinessException(
                    AuthenticationErrorConstants.INVALID_EMAIL_OTP);
        }

        /*
         * Normalize the email before performing the OTP lookup.
         */
        final String normalizedEmail = FreshMealUtilities.normalizeEmail(email);

        /*
         * Capture the current business timestamp once for consistent lifecycle
         * evaluation and atomic repository operations.
         */
        final LocalDateTime now = AppCalendar.getBusinessLocalDateTime();

        /*
         * Resolve the audit actor.
         */
        final String auditUser = serviceContext.getUserProfile() != null
                ? serviceContext.getUserProfile().getUserNumber()
                : RepositoryConstants.SYSTEM_USER;

        /*
         * Retrieve an unused, unrevoked OTP belonging exclusively to this
         * pending registration and normalized email address.
         *
         * The explicit null userNumber condition prevents this V2 operation
         * from matching existing-user OTP records.
         */
        final Query otpQuery = new Query(
                new Criteria().andOperator(
                        Criteria.where("registrationNumber").is(registrationNumber),
                        Criteria.where("userNumber").is(null),
                        Criteria.where("email").is(normalizedEmail),
                        Criteria.where("used").is(false),
                        Criteria.where("revoked").is(false)));

        final EmailVerificationOtpEntity otpEntity = otpRepository.findOne(otpQuery)
                .orElseThrow(() -> new BusinessException(
                        AuthenticationErrorConstants.INVALID_EMAIL_OTP));

        /*
         * An OTP must not be verified before successful email dispatch.
         */
        if (otpEntity.getSentAt() == null) {

            throw new BusinessException(
                    AuthenticationErrorConstants.INVALID_EMAIL_OTP);
        }

        /*
         * Reject an OTP whose maximum verification attempts have been exhausted.
         */
        if (isAttemptsExceeded(otpEntity)) {

            if (!isMaxAttemptCooldownExpired(otpEntity, now)) {

                throw new BusinessException(
                        AuthenticationErrorConstants.EMAIL_OTP_LOCKED);
            }

            /*
             * The cooldown does not revive an exhausted OTP.
             * A newly generated OTP is required.
             */
            throw new BusinessException(
                    AuthenticationErrorConstants.EMAIL_OTP_EXPIRED);
        }

        /*
         * Reject expired OTPs.
         */
        if (isOtpExpired(otpEntity, now)) {

            throw new BusinessException(
                    AuthenticationErrorConstants.EMAIL_OTP_EXPIRED);
        }

        /*
         * Compare the supplied OTP against the persisted hash.
         */
        if (!passwordEncoder.matches(otp, otpEntity.getOtpHash())) {

            /*
             * Atomically increment the failed attempt count.
             */
            final EmailVerificationOtpEntity updatedOtp = otpRepository
                    .incrementFailedAttempt(
                            otpEntity.getVerificationNumber(),
                            otpEntity.getMaxAttempts(),
                            now,
                            auditUser)
                    .orElseThrow(() -> new BusinessException(
                            AuthenticationErrorConstants.INVALID_EMAIL_OTP));

            /*
             * Return zero remaining attempts when the maximum has been reached.
             */
            if (isAttemptsExceeded(updatedOtp)) {
                return new OtpVerificationResult(false, 0);
            }

            /*
             * Return an unsuccessful result so the failed-attempt update
             * can be committed by the enclosing transaction.
             */
            return new OtpVerificationResult(
                    false,
                    updatedOtp.getMaxAttempts() - updatedOtp.getAttemptCount());
        }

        /*
         * Correct OTP — atomically consume the exact verification record.
         *
         * The repository independently checks:
         * - unused and unrevoked state
         * - successful email dispatch
         * - expiration
         * - attempt lockout
         * - active record status
         *
         * Only one concurrent request can successfully consume this OTP.
         */
        otpRepository.consumeEmailVerificationOtp(
                otpEntity.getVerificationNumber(),
                registrationNumber,
                normalizedEmail,
                now,
                auditUser)
                .orElseThrow(() -> new BusinessException(
                        AuthenticationErrorConstants.INVALID_EMAIL_OTP));

        return new OtpVerificationResult(true, 0);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Revokes all active email verification OTPs associated with the specified
     * pending registration.
     * </p>
     *
     * <p>
     * Revocation is performed through a single bulk database operation. This
     * operation is intentionally idempotent: if no active OTP exists, the method
     * completes successfully without modifying any records.
     * </p>
     *
     * @param registrationNumber unique pending registration number
     * @param serviceContext     current service execution context
     */
    @Override
    public void revokeActiveRegistrationEmailVerificationOtp(
            final String registrationNumber,
            final IServiceContext serviceContext) {

        if (registrationNumber == null || registrationNumber.isBlank()
                || serviceContext == null) {
            throw new BusinessException(
                    AuthenticationErrorConstants.ACCOUNT_VERIFICATION_REQUIRED);
        }

        /*
         * Capture the business timestamp once so that all records modified
         * by this operation receive the same revocation timestamp.
         */
        final LocalDateTime revokedAt = AppCalendar.getBusinessLocalDateTime();

        /*
         * Resolve the actor responsible for this operation.
         *
         * During unauthenticated registration workflows, the system user
         * is used as the audit actor.
         */
        final String updatedBy = serviceContext.getUserProfile() != null
                ? serviceContext.getUserProfile().getUserNumber()
                : RepositoryConstants.SYSTEM_USER;

        /*
         * Revoke every active OTP belonging to this pending registration.
         *
         * The repository implementation additionally restricts the operation
         * to records where userNumber is null, preserving V1 OTP isolation.
         */
        otpRepository.revokeActiveOtpsByRegistrationNumber(
                registrationNumber,
                revokedAt,
                updatedBy);
    }

}