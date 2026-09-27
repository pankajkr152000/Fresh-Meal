package com.foodies.freshmeal.delivery.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.common.repository.base.IBaseRepository;
import com.foodies.freshmeal.delivery.entity.DeliveryPartnerEntity;

/**
 * ============================================================================
 * Repository : Delivery Partner
 * ============================================================================
 *
 * <p>
 * Repository abstraction for {@link DeliveryPartnerEntity}.
 * </p>
 *
 * <p>
 * Common persistence and lifecycle operations are inherited from
 * {@link IBaseRepository}. Delivery-partner-specific query methods are defined
 * here only when they represent an actual domain lookup requirement.
 * </p>
 *
 * <p>
 * The delivery partner is uniquely associated with a FreshMeal user through
 * {@code userNumber}. This repository therefore provides lookup by
 * {@code userNumber} for user-to-delivery-partner resolution.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Repository
public interface IDeliveryPartnerRepository extends IBaseRepository<DeliveryPartnerEntity, String> {

    /**
     * Finds a delivery partner by the associated FreshMeal user number.
     *
     * @param userNumber FreshMeal user business identifier
     * @return delivery partner associated with the user, if present
     */
    Optional<DeliveryPartnerEntity> findByUserNumber(String userNumber);
}