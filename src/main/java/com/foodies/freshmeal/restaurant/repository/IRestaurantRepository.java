package com.foodies.freshmeal.restaurant.repository;

import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.common.repository.base.IBaseRepository;
import com.foodies.freshmeal.restaurant.entity.RestaurantEntity;

/**
 * ============================================================================
 * Repository : Restaurant
 * ============================================================================
 *
 * Provides persistence operations for RestaurantEntity.
 *
 * Generic persistence operations such as:
 *
 * - findById
 * - findAll
 * - save
 * - delete
 * - softDelete
 * - restore
 *
 * are inherited from the common repository infrastructure.
 *
 * Restaurant-specific queries are declared here only when required.
 *
 * ============================================================================
 */
@Repository
public interface IRestaurantRepository
        extends IBaseRepository<RestaurantEntity, String> {

}