package com.foodies.freshmeal.food.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.common.repository.base.IBaseRepository;
import com.foodies.freshmeal.food.entity.FoodEntity;

@Repository
public interface IFoodRepository extends IBaseRepository<FoodEntity, String> {
	// ============================================================================
	// Navigation Queries
	// ============================================================================

	/**
	 * Retrieves the immediate previous food based on Food Id.
	 *
	 * Example:
	 *
	 * Current : FOOD01_00031
	 * Returns : FOOD01_00030
	 *
	 * @param id Current Food Id.
	 * @return Previous Food if present.
	 */
	Optional<FoodEntity> findFirstByIdLessThanOrderByIdDesc(String id);

	/**
	 * Retrieves the immediate next food based on Food Id.
	 *
	 * Example:
	 *
	 * Current : FOOD01_00031
	 * Returns : FOOD01_00032
	 *
	 * @param id Current Food Id.
	 * @return Next Food if present.
	 */
	Optional<FoodEntity> findFirstByIdGreaterThanOrderByIdAsc(String id);
}
