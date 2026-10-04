
package com.foodies.freshmeal.checkout.service;

/**
 * ============================================================================
 * Interface : ICheckoutRecoveryService
 * ============================================================================
 *
 * Defines operations for recovering stale checkout sessions.
 *
 * <p>
 * Recovery operations maintain checkout lifecycle consistency by identifying
 * sessions that have remained in an intermediate state beyond their configured
 * timeout and applying the appropriate domain transition.
 * </p>
 *
 * ============================================================================
 */
public interface ICheckoutRecoveryService {

    /**
     * Identifies and recovers checkout sessions that have remained in
     * VALIDATING status beyond the configured validation timeout.
     *
     * <p>
     * Each checkout is processed independently. A failure affecting one
     * checkout must not prevent the remaining eligible sessions from being
     * processed.
     * </p>
     *
     * <p>
     * Sessions that have transitioned to another status are skipped.
     * Concurrent modifications are handled without overwriting newer state.
     * </p>
     *
     * @return the number of checkout sessions successfully recovered
     */
    int recoverStaleValidations();

}
