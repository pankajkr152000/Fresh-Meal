package com.foodies.freshmeal.common.repository.base;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.core.query.Query;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.io.service.impl.RepositoryContext;

/**
 * ============================================================================
 * Base Repository Custom Contract
 * ============================================================================
 *
 * Defines enterprise repository operations implemented by the generic
 * repository framework.
 *
 * <p>
 * Unlike Spring Data CRUD methods, these operations provide additional
 * enterprise capabilities such as:
 * </p>
 *
 * <ul>
 * <li>Automatic Soft Delete Filtering</li>
 * <li>Restore Support</li>
 * <li>Logical Delete</li>
 * <li>Generic Query Execution</li>
 * <li>Scoped Archived Query Execution</li>
 * </ul>
 *
 * <p>
 * Unless explicitly stated otherwise, active-query operations automatically
 * exclude logically deleted records.
 * </p>
 *
 * <p>
 * Archived-query operations explicitly target logically deleted records.
 * When a {@link Query} is supplied, the calling domain repository can define
 * additional business-specific retrieval criteria while the generic
 * repository framework continues to enforce the archived state.
 * </p>
 *
 * @param <T>  Domain entity type.
 * @param <ID> Primary key type.
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IBaseRepositoryCustom<T extends ABaseEntity, ID> {

    // =========================================================================
    // Active Read Operations
    // =========================================================================

    /**
     * Retrieves an active entity by its identifier.
     *
     * @param id Entity identifier.
     *
     * @return Matching active entity if present.
     */
    Optional<T> findActiveById(ID id);

    /**
     * Retrieves all active entities.
     *
     * @return Active entities.
     */
    List<T> findAllActive();

    /**
     * Retrieves the first active entity matching the supplied query.
     *
     * @param query Mongo query defining the retrieval criteria.
     *
     * @return Matching active entity if present.
     */
    Optional<T> findOne(Query query);

    /**
     * Retrieves all active entities matching the supplied query.
     *
     * @param query Mongo query defining the retrieval criteria.
     *
     * @return Matching active entities.
     */
    List<T> findAll(Query query);

    /**
     * Determines whether an active entity exists matching the supplied query.
     *
     * @param query Mongo query defining the retrieval criteria.
     *
     * @return {@code true} if a matching active entity exists.
     */
    boolean exists(Query query);

    /**
     * Counts active entities matching the supplied query.
     *
     * @param query Mongo query defining the retrieval criteria.
     *
     * @return Matching active entity count.
     */
    long count(Query query);

    // =========================================================================
    // Soft Delete Operations
    // =========================================================================

    /**
     * Performs a logical delete operation.
     *
     * @param id                Entity identifier.
     * @param repositoryContext Repository execution context.
     *
     * @return Updated entity.
     */
    T softDelete(
            ID id,
            RepositoryContext repositoryContext);

    // =========================================================================
    // Restore Operations
    // =========================================================================

    /**
     * Restores a previously soft-deleted entity.
     *
     * @param id                Entity identifier.
     * @param repositoryContext Repository execution context.
     *
     * @return Restored entity.
     */
    T restore(
            ID id,
            RepositoryContext repositoryContext);

    // =========================================================================
    // Archived Read Operations
    // =========================================================================

    /**
     * Finds an archived entity by identifier.
     *
     * @param id Entity identifier.
     *
     * @return Archived entity if found.
     */
    Optional<T> findDeletedById(ID id);

    /**
     * Retrieves all archived entities.
     *
     * <p>
     * This operation applies only the archived/deleted state criteria and does
     * not apply additional domain-specific filtering.
     * </p>
     *
     * @return Archived entities.
     */
    List<T> findAllDeleted();

    /**
     * Retrieves archived entities matching the supplied query.
     *
     * <p>
     * The supplied query defines additional domain-specific retrieval criteria.
     * The generic repository implementation automatically applies the archived
     * entity condition to the query.
     * </p>
     *
     * <p>
     * This allows domain repositories to implement scoped archived queries
     * without duplicating soft-delete logic.
     * </p>
     *
     * @param query Mongo query defining the retrieval scope.
     *
     * @return Archived entities matching the supplied query.
     */
    List<T> findAllDeleted(Query query);

    // =========================================================================
    // Permanent Delete Operations
    // =========================================================================

    /**
     * Permanently deletes an archived entity.
     *
     * @param id Entity identifier.
     */
    void deletePermanently(ID id);
}