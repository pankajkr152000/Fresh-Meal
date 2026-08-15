package com.foodies.freshmeal.order.repository;

import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.common.repository.base.IBaseRepository;
import com.foodies.freshmeal.order.entity.OrderEntity;

/**
 * ============================================================================
 * Repository : OrderRepository
 * ============================================================================
 *
 * Persistence repository for the Order aggregate.
 *
 * <p>
 * This repository extends the common FreshMeal repository infrastructure
 * instead of directly extending Spring Data MongoRepository.
 * </p>
 *
 * <p>
 * Common persistence capabilities such as CRUD, pagination, sorting,
 * active-record retrieval, soft delete, restore, and generic MongoDB
 * queries are inherited from IBaseRepository.
 * </p>
 *
 * <p>
 * Order-specific query behavior will be introduced only when the actual
 * Order Management use cases require it.
 * </p>
 *
 * ============================================================================
 */
@Repository
public interface IOrderRepository extends IBaseRepository<OrderEntity, String> {

}
