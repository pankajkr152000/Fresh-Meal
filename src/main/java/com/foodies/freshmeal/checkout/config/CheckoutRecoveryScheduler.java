package com.foodies.freshmeal.checkout.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.foodies.freshmeal.checkout.service.ICheckoutRecoveryService;

/**
 * ============================================================================
 * Scheduler : CheckoutRecoveryScheduler
 * ============================================================================
 *
 * Periodically triggers recovery of checkout sessions that remain stuck in
 * intermediate processing states.
 *
 * The scheduler delegates all recovery decisions and lifecycle transitions
 * to the checkout recovery service.
 *
 * ============================================================================
 */
@Component
public class CheckoutRecoveryScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutRecoveryScheduler.class);

    private final ICheckoutRecoveryService checkoutRecoveryService;

    public CheckoutRecoveryScheduler(
            final ICheckoutRecoveryService checkoutRecoveryService) {
        this.checkoutRecoveryService = checkoutRecoveryService;
    }

    /**
     * Recovers stale checkout validations at a fixed interval.
     *
     * <p>
     * The initial delay prevents the recovery task from executing immediately
     * during application startup.
     * </p>
     */
    @Scheduled(fixedDelayString = "${freshmeal.checkout.recovery.fixed-delay:60000}", initialDelayString = "${freshmeal.checkout.recovery.initial-delay:30000}")
    public void recoverStaleCheckouts() {

        LOGGER.info("Starting scheduled checkout recovery.");

        int recoveredCount = checkoutRecoveryService.recoverStaleValidations();

        LOGGER.info(
                "Scheduled checkout recovery completed. Recovered sessions: {}",
                recoveredCount);
    }
}
