package com.foodies.freshmeal.restaurant.service;

import java.util.List;

import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchCreateRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchDetailsResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchIdRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchListResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchUpdateRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantIdRequest;

/**
 * ============================================================================
 * Service : Restaurant Branch
 * ============================================================================
 *
 * <p>
 * Defines business operations for Restaurant Branch management.
 * </p>
 *
 * <h3>Responsibilities</h3>
 *
 * <ul>
 * <li>Restaurant branch creation</li>
 * <li>Restaurant branch retrieval</li>
 * <li>Restaurant branch update</li>
 * <li>Restaurant branch archival</li>
 * <li>Restaurant branch restoration</li>
 * <li>Archived restaurant branch retrieval</li>
 * <li>Restaurant branch permanent deletion</li>
 * <li>Parent restaurant validation</li>
 * </ul>
 *
 * <p>
 * Persistence lifecycle operations are delegated to the repository layer
 * through the service implementation.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IRestaurantBranchService {

    // =========================================================================
    // Create
    // =========================================================================

    /**
     * Creates a new restaurant branch.
     *
     * @param input branch creation request
     * @return created branch details
     */
    IServiceOutput<RestaurantBranchDetailsResponse> create(
            IServiceInput<RestaurantBranchCreateRequest> input);

    // =========================================================================
    // Read
    // =========================================================================

    /**
     * Retrieves an active restaurant branch by its identifier.
     *
     * @param input branch identifier request
     * @return branch details
     */
    IServiceOutput<RestaurantBranchDetailsResponse> getById(
            IServiceInput<RestaurantBranchIdRequest> input);

    /**
     * Retrieves all active restaurant branches.
     *
     * @param input service execution input
     * @return active branch list
     */
    IServiceOutput<List<RestaurantBranchListResponse>> getAll(
            IServiceInput<Void> input);

    /**
     * Retrieves all active branches belonging to a restaurant.
     *
     * @param input restaurant identifier request
     * @return active branches belonging to the restaurant
     */
    IServiceOutput<List<RestaurantBranchListResponse>> getByRestaurantId(
            IServiceInput<RestaurantIdRequest> input);

    /**
     * Retrieves all archived restaurant branches.
     *
     * @param input service execution input
     * @return archived branch list
     */
    IServiceOutput<List<RestaurantBranchListResponse>> getArchived(
            IServiceInput<Void> input);

    // =========================================================================
    // Update
    // =========================================================================

    /**
     * Updates standard business information of an existing restaurant branch.
     *
     * <p>
     * Parent restaurant relationship and persistence lifecycle information are
     * intentionally excluded from this operation.
     * </p>
     *
     * @param input branch update request
     * @return updated branch details
     */
    IServiceOutput<RestaurantBranchDetailsResponse> update(
            IServiceInput<RestaurantBranchUpdateRequest> input);

    // =========================================================================
    // Lifecycle
    // =========================================================================

    /**
     * Archives an active restaurant branch.
     *
     * <p>
     * Archival is implemented as a soft-delete operation. The branch remains
     * physically stored and can subsequently be restored.
     * </p>
     *
     * @param input branch identifier request
     * @return archived branch details
     */
    IServiceOutput<RestaurantBranchDetailsResponse> archive(
            IServiceInput<RestaurantBranchIdRequest> input);

    /**
     * Restores an archived restaurant branch.
     *
     * @param input branch identifier request
     * @return restored branch details
     */
    IServiceOutput<RestaurantBranchDetailsResponse> restore(
            IServiceInput<RestaurantBranchIdRequest> input);

    /**
     * Permanently deletes an archived restaurant branch.
     *
     * <p>
     * Permanent deletion is intentionally restricted to an already archived
     * branch.
     * </p>
     *
     * @param input branch identifier request
     */
    void deletePermanently(
            IServiceInput<RestaurantBranchIdRequest> input);
}