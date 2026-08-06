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
 *
 * <ul>
 * <li>Automatic Soft Delete Filtering</li>
 * <li>Restore Support</li>
 * <li>Logical Delete</li>
 * <li>Generic Query Execution</li>
 * </ul>
 *
 * <p>
 * Unless explicitly stated otherwise, every query automatically excludes
 * logically deleted records.
 *
 * @param <T>  Domain entity type.
 * @param <ID> Primary key type.
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IBaseRepositoryCustom<T extends ABaseEntity, ID> {

    /**
     * Retrieves an active entity by its identifier.
     *
     * @param id Entity identifier.
     *
     * @return Matching entity if present.
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
     * @param query Mongo query.
     *
     * @return Matching entity if present.
     */
    Optional<T> findOne(Query query);

    /**
     * Retrieves all active entities matching the supplied query.
     *
     * @param query Mongo query.
     *
     * @return Matching entities.
     */
    List<T> findAll(Query query);

    /**
     * Determines whether an active entity exists.
     *
     * @param query Mongo query.
     *
     * @return True if matching entity exists.
     */
    boolean exists(Query query);

    /**
     * Counts active entities matching the supplied query.
     *
     * @param query Mongo query.
     *
     * @return Matching entity count.
     */
    long count(Query query);

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

    /**
     * Restores a previously soft deleted entity.
     *
     * @param id                Entity identifier.
     * @param repositoryContext Repository execution context.
     *
     * @return Restored entity.
     */
    T restore(ID id, RepositoryContext repositoryContext);

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
     * @return Archived entities.
     */
    List<T> findAllDeleted();

    /**
     * Permanently deletes an entity.
     *
     * @param id Entity identifier.
     */
    void deletePermanently(ID id);

}