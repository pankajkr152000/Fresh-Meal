
package com.foodies.freshmeal.checkout.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.checkout.constants.CheckoutStatusConstant;
import com.foodies.freshmeal.checkout.entity.CheckoutEntity;
import com.foodies.freshmeal.common.repository.base.IBaseRepository;

/**
 * Repository abstraction for {@link CheckoutEntity}.
 *
 * <p>
 * Provides persistence operations for the Checkout aggregate while inheriting
 * common repository behavior from the FreshMeal base repository abstraction.
 * </p>
 *
 * <p>
 * Checkout-specific queries are intentionally limited to persistence-oriented
 * lookup requirements. Business rules such as ownership validation, cart
 * validation, availability checks, pricing verification, lifecycle transitions,
 * and order reconciliation remain in the service and domain layers.
 * </p>
 */
@Repository
public interface ICheckoutRepository extends IBaseRepository<CheckoutEntity, String> {

    /**
     * Finds a checkout using its unique business identifier.
     *
     * @param checkoutNumber checkout business identifier
     * @return matching checkout when present
     */
    Optional<CheckoutEntity> findByCheckoutNumber(String checkoutNumber);

    /**
     * Retrieves all checkouts belonging to a customer for the specified
     * lifecycle status.
     *
     * <p>
     * Multiple checkout attempts may share the same status. Therefore, this
     * method returns a collection rather than assuming uniqueness.
     * </p>
     *
     * @param userNumber customer business identifier
     * @param status     checkout lifecycle status
     * @return matching checkouts
     */
    List<CheckoutEntity> findByUserNumberAndStatus(
            String userNumber,
            CheckoutStatusConstant status);

    /**
     * Retrieves the checkout history of a customer in reverse chronological
     * order.
     *
     * <p>
     * The most recently initiated checkout is returned first.
     * </p>
     *
     * @param userNumber customer business identifier
     * @return customer's checkouts ordered by initiation time, newest first
     */
    List<CheckoutEntity> findByUserNumberOrderByInitiatedAtDesc(String userNumber);

    /**
     * Retrieves checkout attempts associated with the specified cart.
     *
     * <p>
     * A cart may be associated with multiple checkout attempts over time.
     * The service layer determines which attempt is relevant to the
     * requested operation.
     * </p>
     *
     * @param cartNumber cart business identifier
     * @return matching checkout attempts
     */
    List<CheckoutEntity> findByCartNumber(String cartNumber);

    /**
     * Retrieves checkouts matching the specified lifecycle status whose
     * expiration timestamp is earlier than the supplied cutoff.
     *
     * <p>
     * Intended for scheduled expiration processing. The maintenance service
     * must revalidate each checkout's current status and expiration before
     * applying a lifecycle transition.
     * </p>
     *
     * @param status    checkout lifecycle status
     * @param expiresAt expiration cutoff
     * @return matching checkouts eligible for expiration evaluation
     */
    List<CheckoutEntity> findByStatusAndExpiresAtBefore(
            CheckoutStatusConstant status,
            LocalDateTime expiresAt);

    /**
     * Checks whether an order business identifier is already associated
     * with a checkout.
     *
     * <p>
     * Supports order reconciliation and duplicate-association checks.
     * This method does not replace the unique originating-checkout constraint
     * that must be enforced by the Order persistence layer.
     * </p>
     *
     * @param orderNumber order business identifier
     * @return {@code true} if a checkout references the order
     */
    boolean existsByOrderNumber(String orderNumber);
}