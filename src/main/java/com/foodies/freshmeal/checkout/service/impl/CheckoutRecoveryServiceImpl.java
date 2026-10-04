
package com.foodies.freshmeal.checkout.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.foodies.freshmeal.checkout.config.CheckoutRecoveryProperties;
import com.foodies.freshmeal.checkout.constants.CheckoutStatusConstant;
import com.foodies.freshmeal.checkout.entity.CheckoutEntity;
import com.foodies.freshmeal.checkout.repository.ICheckoutRepository;
import com.foodies.freshmeal.checkout.service.ICheckoutRecoveryService;
import com.foodies.freshmeal.checkout.valueObject.CheckoutFailureDetails;
import com.foodies.freshmeal.common.date.AppCalendar;

/**
 * ============================================================================
 * Service : CheckoutRecoveryService
 * ============================================================================
 *
 * Recovers checkout sessions that remain in VALIDATING beyond the configured
 * validation timeout.
 *
 * <p>
 * Recovery is deliberately restricted to stale validation sessions. A checkout
 * in CONFIRMATION_IN_PROGRESS is not automatically failed because order
 * creation may have an uncertain outcome and require reconciliation.
 * </p>
 *
 * <p>
 * Each checkout is processed independently. A failure affecting one record
 * must not prevent the remaining stale sessions from being considered.
 * </p>
 *
 * ============================================================================
 */
@Service
public class CheckoutRecoveryServiceImpl implements ICheckoutRecoveryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutRecoveryServiceImpl.class);

    private static final String RECOVERY_REMARKS = "Checkout validation exceeded the configured timeout.";

    private final ICheckoutRepository checkoutRepository;
    private final CheckoutRecoveryProperties recoveryProperties;

    /**
     * Creates the checkout recovery service.
     *
     * @param checkoutRepository repository used to retrieve and persist checkouts
     * @param recoveryProperties centralized recovery configuration
     */
    public CheckoutRecoveryServiceImpl(
            final ICheckoutRepository checkoutRepository,
            final CheckoutRecoveryProperties recoveryProperties) {

        this.checkoutRepository = Objects.requireNonNull(
                checkoutRepository,
                "Checkout repository must not be null.");

        this.recoveryProperties = Objects.requireNonNull(
                recoveryProperties,
                "Checkout recovery properties must not be null.");
    }

    /**
     * Finds and recovers checkout sessions whose validation has exceeded
     * the configured timeout.
     *
     * <p>
     * The cutoff is calculated once per batch so every candidate is evaluated
     * against the same time boundary.
     * </p>
     *
     * <p>
     * This method is safe to invoke repeatedly. Sessions that have already
     * transitioned out of VALIDATING are ignored during processing.
     * </p>
     *
     * @return number of checkout sessions successfully recovered
     */
    @Override
    public int recoverStaleValidations() {

        final LocalDateTime recoveryStartedAt = AppCalendar.getBusinessLocalDateTime();

        final LocalDateTime cutoffTime = recoveryStartedAt.minusMinutes(
                recoveryProperties.getValidationTimeoutMinutes());

        LOGGER.info(
                "Starting stale checkout validation recovery. cutoffTime={}, timeoutMinutes={}",
                cutoffTime,
                recoveryProperties.getValidationTimeoutMinutes());

        final List<CheckoutEntity> staleCheckouts = checkoutRepository.findByStatusAndStatusUpdatedAtBefore(
                CheckoutStatusConstant.VALIDATING,
                cutoffTime);

        if (staleCheckouts == null || staleCheckouts.isEmpty()) {

            LOGGER.debug(
                    "No stale checkout validation sessions found. cutoffTime={}",
                    cutoffTime);

            return 0;
        }

        int recoveredCount = 0;
        int skippedCount = 0;
        int failedCount = 0;

        for (CheckoutEntity candidate : staleCheckouts) {

            if (candidate == null) {
                skippedCount++;

                LOGGER.warn(
                        "Null checkout candidate returned by recovery query. "
                                + "The candidate will be skipped.");

                continue;
            }

            try {

                final boolean recovered = recoverStaleValidation(candidate);

                if (recovered) {
                    recoveredCount++;
                } else {
                    skippedCount++;
                }

            } catch (OptimisticLockingFailureException exception) {

                /*
                 * Another request updated the checkout after the recovery
                 * query. Do not retry with stale entity data or overwrite
                 * the newer state.
                 */
                skippedCount++;

                LOGGER.info(
                        "Checkout recovery skipped due to a concurrent update. "
                                + "checkoutNumber={}",
                        candidate.getCheckoutNumber());

            } catch (RuntimeException exception) {

                failedCount++;

                LOGGER.error(
                        "Unexpected failure while recovering stale checkout. "
                                + "checkoutNumber={}",
                        candidate.getCheckoutNumber(),
                        exception);
            }
        }

        LOGGER.info(
                "Stale checkout validation recovery completed. "
                        + "candidates={}, recovered={}, skipped={}, failed={}",
                staleCheckouts.size(),
                recoveredCount,
                skippedCount,
                failedCount);

        return recoveredCount;
    }

    /**
     * Attempts to recover a single stale validation session.
     *
     * @param checkout checkout candidate returned by the stale-session query
     * @return true when the checkout was successfully recovered; false when
     *         the candidate is no longer eligible
     */
    private boolean recoverStaleValidation(
            final CheckoutEntity checkout) {

        Objects.requireNonNull(
                checkout,
                "Checkout candidate must not be null.");

        /*
         * Recheck lifecycle state before mutation. The repository query is
         * only a candidate-selection mechanism; it is not a guarantee that
         * the record remains eligible when this method executes.
         */
        if (checkout.getStatus() != CheckoutStatusConstant.VALIDATING) {

            LOGGER.debug(
                    "Checkout recovery skipped because status has changed. "
                            + "checkoutNumber={}, status={}",
                    checkout.getCheckoutNumber(),
                    checkout.getStatus());

            return false;
        }

        final LocalDateTime statusUpdatedAt = checkout.getStatusUpdatedAt();

        if (statusUpdatedAt == null) {

            LOGGER.error(
                    "Checkout has no statusUpdatedAt value and cannot be "
                            + "safely evaluated for recovery. checkoutNumber={}",
                    checkout.getCheckoutNumber());

            return false;
        }

        final LocalDateTime cutoffTime = AppCalendar.getBusinessLocalDateTime().minusMinutes(
                recoveryProperties.getValidationTimeoutMinutes());

        if (!statusUpdatedAt.isBefore(cutoffTime)) {

            LOGGER.debug(
                    "Checkout is no longer stale. checkoutNumber={}, "
                            + "statusUpdatedAt={}, cutoffTime={}",
                    checkout.getCheckoutNumber(),
                    statusUpdatedAt,
                    cutoffTime);

            return false;
        }

        final CheckoutFailureDetails failureDetails = buildValidationTimeoutFailureDetails();

        /*
         * The entity method enforces the domain transition and updates
         * status history. Do not modify lifecycle fields directly here.
         */
        checkout.markValidationTimedOut(failureDetails);

        /*
         * MongoDB optimistic locking prevents this save from silently
         * overwriting a concurrent update.
         */
        final CheckoutEntity savedCheckout = checkoutRepository.save(checkout);

        if (savedCheckout == null) {

            LOGGER.error(
                    "Checkout repository returned null during stale-session "
                            + "recovery. checkoutNumber={}",
                    checkout.getCheckoutNumber());

            return false;
        }

        LOGGER.warn(
                "Stale checkout validation recovered successfully. "
                        + "checkoutNumber={}, previousStatus={}, currentStatus={}",
                savedCheckout.getCheckoutNumber(),
                CheckoutStatusConstant.VALIDATING,
                savedCheckout.getStatus());

        return true;
    }

    /**
     * Builds the failure details for a definitive validation timeout.
     *
     * <p>
     * The timeout is treated as a definitive processing failure because this
     * recovery path applies only to VALIDATING sessions. It must not be reused
     * for uncertain order-creation outcomes.
     * </p>
     *
     * @return immutable-in-intent failure details to embed in the checkout
     */
    private CheckoutFailureDetails buildValidationTimeoutFailureDetails() {

        return CheckoutFailureDetails.builder()
                .failureCategory(
                        CheckoutFailureDetails.FailureCategory.DEFINITIVE_PROCESSING)
                .errorCode(
                        "FM-CHECKOUT-807")
                .message(
                        "Checkout validation exceeded the configured timeout.")
                .occurredAt(
                        AppCalendar.getBusinessLocalDateTime())
                .reconciliationRequired(false)
                .remarks(RECOVERY_REMARKS)
                .build();
    }
}
