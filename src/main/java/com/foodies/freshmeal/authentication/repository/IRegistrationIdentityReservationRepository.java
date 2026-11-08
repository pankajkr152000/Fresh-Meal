package com.foodies.freshmeal.authentication.repository;

import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.authentication.entity.RegistrationIdentityReservationEntity;
import com.foodies.freshmeal.common.repository.base.IBaseRepository;

/**
 * Repository contract for managing registration identity reservations.
 *
 * <p>
 * This repository combines the standard base repository operations with
 * specialized atomic operations required by the registration identity
 * reservation lifecycle.
 * </p>
 *
 * <p>
 * Identity reservation operations are delegated to the custom repository
 * contract to ensure ownership checks, concurrency safety, and correct
 * reservation lifecycle management.
 * </p>
 *
 * @author FreshMeal
 * @since 1.0
 */
@Repository
public interface IRegistrationIdentityReservationRepository
        extends IBaseRepository<RegistrationIdentityReservationEntity, String>,
        IRegistrationIdentityReservationRepositoryCustom {
}