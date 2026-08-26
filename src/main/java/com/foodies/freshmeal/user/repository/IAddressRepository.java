package com.foodies.freshmeal.user.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.common.repository.base.IBaseRepository;
import com.foodies.freshmeal.user.entity.AddressEntity;

/**
 * =============================================================================
 * Repository : IAddressRepository
 * =============================================================================
 *
 * Purpose
 * -------
 * Provides persistence operations for AddressEntity.
 *
 * Custom address queries will be added here only when required by the
 * AddressService business operations.
 * =============================================================================
 */
@Repository
public interface IAddressRepository extends IBaseRepository<AddressEntity, String> {
    /**
     * Loads an address using its FreshMeal business-facing address number.
     *
     * @param addressNumber business address identifier
     * @return matching address entity, if present
     */
    Optional<AddressEntity> findByAddressNumber(String addressNumber);
}