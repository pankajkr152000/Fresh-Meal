package com.foodies.freshmeal.restaurant.service;

import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.restaurant.entity.RestaurantBranchEntity;
import com.foodies.freshmeal.restaurant.entity.RestaurantEntity;

/**
 * ============================================================================
 * Service : Restaurant Lifecycle
 * ============================================================================
 *
 * <p>
 * Defines hierarchical lifecycle operations for the Restaurant domain.
 * </p>
 *
 * <p>
 * The Restaurant hierarchy follows:
 * </p>
 *
 * <pre>
 * Restaurant
 *     |
 *     +-- Restaurant Branch
 *             |
 *             +-- Food
 * </pre>
 *
 * <p>
 * This service is responsible only for coordinating persistence lifecycle
 * operations across the hierarchy. Normal business operations remain within
 * their respective domain services.
 * </p>
 *
 * <h3>Lifecycle Rules</h3>
 *
 * <ul>
 * <li>Archiving a Restaurant archives all currently active child Branches and
 * Foods.</li>
 * <li>Archiving a Branch archives all currently active Foods belonging to the
 * Branch.</li>
 * <li>Restoration does not automatically restore child entities.</li>
 * <li>Permanent Restaurant deletion permanently deletes its archived child
 * Branches and Foods.</li>
 * <li>Permanent Branch deletion permanently deletes its archived Foods.</li>
 * </ul>
 *
 * <p>
 * Child entities that were already archived independently remain archived and
 * are not implicitly restored.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IRestaurantLifecycleService {

    /**
     * Archives a Restaurant and its currently active child hierarchy.
     *
     * <p>
     * The operation archives:
     * </p>
     *
     * <pre>
     * Restaurant
     *     ↓
     * Active Branches
     *     ↓
     * Active Foods
     * </pre>
     *
     * @param restaurant     restaurant being archived
     * @param serviceContext current service execution context
     */
    void archiveRestaurantHierarchy(
            RestaurantEntity restaurant,
            IServiceContext serviceContext);

    /**
     * Permanently deletes an archived Restaurant and its archived child
     * hierarchy.
     *
     * <p>
     * The Restaurant must already be archived. Any remaining active child
     * entity indicates an invalid lifecycle state and must prevent permanent
     * deletion.
     * </p>
     *
     * @param restaurant     archived restaurant
     * @param serviceContext current service execution context
     */
    void deleteRestaurantHierarchyPermanently(
            RestaurantEntity restaurant,
            IServiceContext serviceContext);

    /**
     * Archives a Restaurant Branch and all currently active Foods belonging to
     * that Branch.
     *
     * @param branch         branch being archived
     * @param serviceContext current service execution context
     */
    void archiveBranchHierarchy(
            RestaurantBranchEntity branch,
            IServiceContext serviceContext);

    /**
     * Permanently deletes an archived Restaurant Branch and its archived Foods.
     *
     * <p>
     * The Branch must already be archived. Any remaining active Food indicates
     * an invalid lifecycle state and must prevent permanent deletion.
     * </p>
     *
     * @param branch         archived branch
     * @param serviceContext current service execution context
     */
    void deleteBranchHierarchyPermanently(
            RestaurantBranchEntity branch,
            IServiceContext serviceContext);
}