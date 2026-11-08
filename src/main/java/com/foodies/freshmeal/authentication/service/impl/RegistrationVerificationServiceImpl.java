package com.foodies.freshmeal.authentication.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodies.freshmeal.authentication.constants.AuthenticationErrorConstants;
import com.foodies.freshmeal.authentication.constants.RegistrationStatus;
import com.foodies.freshmeal.authentication.dto.RegistrationIdentityReservationTransferInput;
import com.foodies.freshmeal.authentication.dto.RegistrationVerificationResult;
import com.foodies.freshmeal.authentication.dto.VerifiedRegistrationResult;
import com.foodies.freshmeal.authentication.entity.PendingRegistrationEntity;
import com.foodies.freshmeal.authentication.service.IOtpService;
import com.foodies.freshmeal.authentication.service.IPendingRegistrationService;
import com.foodies.freshmeal.authentication.service.IRegistrationIdentityReservationService;
import com.foodies.freshmeal.authentication.service.IRegistrationVerificationService;
import com.foodies.freshmeal.authentication.valueObject.OtpVerificationResult;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.exception.BusinessException;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.user.dto.EmailVerifiedUserCreationInput;
import com.foodies.freshmeal.user.entity.UserEntity;
import com.foodies.freshmeal.user.service.IUserService;

import lombok.RequiredArgsConstructor;

/**
 * Handles the transactional verification of pending user registrations.
 *
 * <p>
 * Coordinates OTP verification, permanent user creation, and
 * pending registration completion within a single MongoDB transaction.
 * </p>
 *
 * <p>
 * Email delivery must happen outside this transaction.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class RegistrationVerificationServiceImpl implements IRegistrationVerificationService {

    private final IPendingRegistrationService pendingRegistrationService;

    private final IOtpService otpService;

    private final IUserService userService;

    private final IRegistrationIdentityReservationService registrationIdentityReservationService;

    /**
     * {@inheritDoc}
     */

    /**
     * {@inheritDoc}
     */
    @Transactional(transactionManager = "mongoTransactionManager")
    @Override
    public RegistrationVerificationResult verifyAndCreateUser(
            final String registrationNumber,
            final String otp,
            final IServiceContext serviceContext) {

        /*
         * Step 1: Validate the registration identifier and execution context.
         */
        if (registrationNumber == null || registrationNumber.isBlank()
                || serviceContext == null) {

            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }

        /*
         * Step 2: Retrieve the pending registration.
         */
        final PendingRegistrationEntity pendingRegistration = pendingRegistrationService.getPendingRegistration(
                registrationNumber,
                serviceContext);

        /*
         * Step 3: Validate the registration lifecycle.
         */
        if (!pendingRegistration.isPending()) {

            final RegistrationStatus status = pendingRegistration.getRegistrationStatus();

            if (status == RegistrationStatus.COMPLETED) {
                throw new BusinessException(
                        AuthenticationErrorConstants.PENDING_REGISTRATION_ALREADY_COMPLETED);
            }

            if (status == RegistrationStatus.EXPIRED) {
                throw new BusinessException(
                        AuthenticationErrorConstants.PENDING_REGISTRATION_EXPIRED);
            }

            if (status == RegistrationStatus.CANCELLED) {
                throw new BusinessException(
                        AuthenticationErrorConstants.PENDING_REGISTRATION_ALREADY_CANCELLED);
            }

            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }

        /*
         * Step 4: Capture the current business timestamp once.
         */
        final LocalDateTime verificationTime = AppCalendar.getBusinessLocalDateTime();

        /*
         * Step 5: Validate registration expiration independently
         * of OTP expiration.
         */
        if (pendingRegistration.getExpiresAt() == null) {
            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }

        if (pendingRegistration.isExpired(verificationTime)) {
            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_EXPIRED);
        }

        /*
         * Step 6: Verify the registration-specific OTP.
         *
         * OTP lifecycle, failed attempts, expiration, lockout and
         * atomic consumption remain owned by IOtpService.
         */
        final OtpVerificationResult otpVerificationResult = otpService.verifyRegistrationEmailVerificationOtp(
                registrationNumber,
                pendingRegistration.getEmail().getValue(),
                otp,
                serviceContext);

        /*
         * An incorrect OTP returns normally so that failed-attempt
         * updates can be committed.
         */
        if (!otpVerificationResult.verified()) {

            return new RegistrationVerificationResult(
                    false,
                    otpVerificationResult.attemptsRemaining(),
                    null);
        }

        /*
         * Step 7: Prepare permanent user creation input.
         *
         * The encoded password is transferred from the pending
         * registration. The raw password is never available here.
         */
        final EmailVerifiedUserCreationInput userCreationInput = new EmailVerifiedUserCreationInput();

        userCreationInput.setUsername(pendingRegistration.getUsername());
        userCreationInput.setFirstName(pendingRegistration.getFirstName());
        userCreationInput.setLastName(pendingRegistration.getLastName());
        userCreationInput.setEmail(pendingRegistration.getEmail());
        userCreationInput.setPhoneNumber(pendingRegistration.getPhoneNumber());
        userCreationInput.setEncodedPassword(pendingRegistration.getEncodedPassword());

        /*
         * Step 8: Delegate permanent account creation to the User module.
         *
         * The User module assigns only the initial USER role and
         * marks the email as verified.
         */
        final UserEntity createdUser = userService.createEmailVerifiedUser(
                new ServiceInput<>(userCreationInput, serviceContext))
                .getOutput();

        /*
         * Step 9: Transfer identity reservation ownership.
         *
         * The reservation must currently belong to this pending
         * registration. The reservation service performs the
         * ownership check and atomic ownership transfer.
         *
         * The newly created permanent user becomes the owner.
         */
        final RegistrationIdentityReservationTransferInput transferInput = RegistrationIdentityReservationTransferInput
                .builder()
                .registrationNumber(registrationNumber)
                .userNumber(createdUser.getUserNumber())
                .build();

        registrationIdentityReservationService.transferOwnershipToUser(
                new ServiceInput<>(transferInput, serviceContext));

        /*
         * Step 10: Complete the pending registration.
         *
         * This changes its lifecycle to COMPLETED, clears the
         * temporary encoded password and persists the update.
         */
        pendingRegistrationService.completePendingRegistration(
                registrationNumber,
                serviceContext);

        /*
         * Step 11: Prepare the successful verification result.
         */
        final VerifiedRegistrationResult verifiedRegistration = new VerifiedRegistrationResult(
                createdUser.getUserNumber(),
                pendingRegistration.getEmail().getValue(),
                pendingRegistration.getFirstName());

        return new RegistrationVerificationResult(
                true,
                0,
                verifiedRegistration);
    }

}