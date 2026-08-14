package com.foodies.freshmeal.order.repository;

import java.util.Optional;

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
 * This repository extends the application's common IBaseRepository so that
 * OrderEntity automatically receives:
 *
 * - Standard MongoDB CRUD operations
 * - Pagination
 * - Sorting
 * - Soft delete support
 * - Restore support
 * - Active record retrieval
 * - Archived record retrieval
 * - Generic Mongo query operations
 *
 * Business rules and order lifecycle logic belong to the service/business
 * layer and should not be implemented here.
 *
 * ============================================================================
 */
@Repository
public interface IOrderRepository extends IBaseRepository<OrderEntity, String> {

    /**
     * Finds an active order using its business-facing order number.
     *
     * Example:
     *
     * FM-ORD-0000001
     *
     * <p>
     * The repository framework's standard Spring Data query mechanism is used
     * here. For business operations where soft-delete filtering is critical,
     * the service should prefer the active-query methods provided by
     * IBaseRepositoryCustom.
     * </p>
     *
     * @param orderNumber business order number
     * @return matching order when present
     */
    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    /**
     * Determines whether an order exists using its business-facing order
     * number.
     *
     * @param orderNumber business order number
     * @return true when an order exists
     */
    boolean existsByOrderNumber(String orderNumber);
}
