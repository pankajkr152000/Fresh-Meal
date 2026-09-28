package com.foodies.freshmeal.cart.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.cart.constants.CartStatusConstant;
import com.foodies.freshmeal.cart.entity.CartEntity;
import com.foodies.freshmeal.common.repository.base.IBaseRepository;

/**
 * Repository abstraction for {@link CartEntity}.
 *
 * <p>
 * Provides persistence operations for the Cart aggregate while inheriting
 * common repository behavior from the FreshMeal base repository abstraction.
 * </p>
 *
 * <p>
 * Cart-specific queries are intentionally limited to persistence-oriented
 * lookup requirements. Business rules such as ownership validation,
 * availability checks, restaurant consistency, lifecycle transitions, and
 * checkout validation remain in the service/domain layers.
 * </p>
 */
@Repository
public interface ICartRepository
        extends IBaseRepository<CartEntity, String> {

    /**
     * Finds the cart belonging to a customer for the specified lifecycle status.
     *
     * <p>
     * This query is primarily used to locate the customer's ACTIVE cart.
     * The service layer determines which status is appropriate for a given
     * business operation.
     * </p>
     *
     * @param userNumber customer business identifier
     * @param status     cart lifecycle status
     * @return matching cart when present
     */
    Optional<CartEntity> findByUserNumberAndStatus(
            String userNumber,
            CartStatusConstant status);

    /**
     * Finds a cart using its business identifier.
     *
     * <p>
     * {@code cartNumber} is the external/business identifier and is preferred
     * over exposing the internal MongoDB identifier to the business layer.
     * </p>
     *
     * @param cartNumber cart business identifier
     * @return matching cart when present
     */
    Optional<CartEntity> findByCartNumber(String cartNumber);
}