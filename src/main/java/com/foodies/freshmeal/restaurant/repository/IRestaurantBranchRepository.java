package com.foodies.freshmeal.restaurant.repository;

import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.common.repository.base.IBaseRepository;
import com.foodies.freshmeal.restaurant.entity.RestaurantBranchEntity;

/**
 * ============================================================================
 * Repository : Restaurant Branch
 * ============================================================================
 *
 * Provides persistence operations for RestaurantBranchEntity.
 *
 * Generic persistence behavior is inherited from the common repository
 * infrastructure.
 *
 * Only branch-specific queries are declared here.
 *
 * ============================================================================
 */
@Repository
public interface IRestaurantBranchRepository extends IBaseRepository<RestaurantBranchEntity, String> {

}