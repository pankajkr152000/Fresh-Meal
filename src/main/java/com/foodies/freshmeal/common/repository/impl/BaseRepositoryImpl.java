package com.foodies.freshmeal.common.repository.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

import com.foodies.freshmeal.common.constants.EntityFieldConstants;
import com.foodies.freshmeal.common.constants.RepositoryConstants;
import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.exception.CommonErrorConstants;
import com.foodies.freshmeal.common.exception.ResourceNotFoundException;
import com.foodies.freshmeal.common.io.service.impl.RepositoryContext;
import com.foodies.freshmeal.common.repository.base.IBaseRepositoryCustom;

/**
 * ============================================================================
 * Base Repository Implementation
 * ============================================================================
 *
 * <p>
 * Generic MongoDB repository implementation shared by every domain repository
 * within the FreshMeal platform.
 * </p>
 *
 * <p>
 * This implementation extends Spring Data's {@link SimpleMongoRepository} while
 * providing enterprise persistence features including:
 * </p>
 *
 * <ul>
 * <li>Automatic Soft Delete Filtering</li>
 * <li>Logical Delete</li>
 * <li>Restore Support</li>
 * <li>Generic Query Execution</li>
 * <li>Future Search Framework</li>
 * </ul>
 *
 * <p>
 * Every repository implementation in the application inherits this class.
 * </p>
 *
 * @param <T>  Domain Entity Type
 * @param <ID> Primary Key Type
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public class BaseRepositoryImpl<T extends ABaseEntity, ID> extends SimpleMongoRepository<T, ID>
		implements IBaseRepositoryCustom<T, ID> {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseRepositoryImpl.class);

	/**
	 * Entity metadata.
	 */
	protected final MongoEntityInformation<T, ID> entityInformation;

	/**
	 * Mongo operations.
	 */
	protected final MongoOperations mongoOperations;

	/**
	 * Domain entity class.
	 */
	protected final Class<T> entityClass;

	/**
	 * Creates a new repository implementation.
	 *
	 * @param entityInformation Entity metadata.
	 * @param mongoOperations   Mongo operations.
	 */
	public BaseRepositoryImpl(final MongoEntityInformation<T, ID> entityInformation,
			final MongoOperations mongoOperations) {

		super(entityInformation, mongoOperations);

		this.entityInformation = entityInformation;
		this.mongoOperations = mongoOperations;
		this.entityClass = entityInformation.getJavaType();
	}

	// =========================================================================
	// Private Query Builder Methods
	// =========================================================================

	/**
	 * Creates criteria that returns only active (non-deleted) documents.
	 *
	 * @return Active entity criteria.
	 */
	private Criteria activeCriteria() {

		return Criteria.where(EntityFieldConstants.DELETED_FLAG).is(RepositoryConstants.ACTIVE);
	}

	/**
	 * Creates criteria that returns only logically deleted documents.
	 *
	 * @return Deleted entity criteria.
	 */
	private Criteria deletedCriteria() {

		return Criteria.where(EntityFieldConstants.DELETED_FLAG).is(RepositoryConstants.DELETED);
	}

	/**
	 * Creates a query containing only active records.
	 *
	 * @return Active query.
	 */
	private Query activeQuery() {

		return new Query(activeCriteria());
	}

	/**
	 * Creates a query containing only logically deleted records.
	 *
	 * @return Deleted query.
	 */
	// private Query deletedQuery() {

	// return new Query(deletedCriteria());
	// }

	/**
	 * Creates query for archived entities.
	 *
	 * @return Mongo query.
	 */

	private Query deletedQuery() {

		return Query.query(deletedCriteria());
	}

	/**
	 * Creates a query for retrieving an active entity by its identifier.
	 *
	 * @param id Entity identifier.
	 *
	 * @return Active entity query.
	 */
	private Query activeIdQuery(final ID id) {

		Query query = activeQuery();

		query.addCriteria(Criteria.where(EntityFieldConstants.ID).is(id));

		return query;
	}

	/**
	 * Creates a query for retrieving a logically deleted entity by its identifier.
	 *
	 * @param id Entity identifier.
	 *
	 * @return Deleted entity query.
	 */
	// private Query deletedIdQuery(final ID id) {

	// Query query = deletedQuery();

	// query.addCriteria(
	// Criteria.where(EntityFieldConstants.ID)
	// .is(id));

	// return query;
	// }

	/**
	 * Creates query for archived entity.
	 *
	 * @param id Entity identifier.
	 *
	 * @return Mongo query.
	 */
	private Query deletedIdQuery(ID id) {

		return Query.query(

				Criteria.where(EntityFieldConstants.ID).is(id)

						.and(EntityFieldConstants.DELETED_FLAG).is(true));
	}

	// =========================================================================
	// Generic Query Builder Methods
	// =========================================================================

	/**
	 * Creates a defensive copy of the supplied query and appends the supplied
	 * criteria.
	 *
	 * <p>
	 * The original query instance is never modified.
	 * </p>
	 *
	 * @param sourceQuery Source query.
	 * @param criteria    Additional criteria.
	 *
	 * @return Newly constructed query.
	 */
	private Query buildQuery(final Query sourceQuery, final Criteria criteria) {

		final Query query = (sourceQuery == null) ? new Query() : Query.of(sourceQuery);

		if (criteria != null) {
			query.addCriteria(criteria);
		}

		return query;
	}

	/**
	 * Creates an active query.
	 *
	 * @param sourceQuery Source query.
	 *
	 * @return Active query.
	 */
	private Query buildActiveQuery(final Query sourceQuery) {

		return buildQuery(sourceQuery, activeCriteria());
	}

	// =========================================================================
	// Update Builder Methods
	// =========================================================================

	/**
	 * Creates a new Mongo update instance.
	 *
	 * @return Empty update instance.
	 */
	private Update createUpdate() {

		return new Update();
	}

	/**
	 * Creates a soft delete update.
	 *
	 * <p>
	 * This update performs a logical delete without replacing the MongoDB document.
	 * </p>
	 *
	 * @param repositoryContext Repository execution context.
	 *
	 * @return Mongo update.
	 */
	private Update buildSoftDeleteUpdate(final RepositoryContext repositoryContext) {

		return createUpdate()

				.set(EntityFieldConstants.DELETED_FLAG, RepositoryConstants.DELETED)

				.set(EntityFieldConstants.DELETED_AT, repositoryContext.currentDateTime())

				.set(EntityFieldConstants.DELETED_BY, repositoryContext.currentUser())

				.set(EntityFieldConstants.UPDATED_AT, repositoryContext.currentDateTime())

				.set(EntityFieldConstants.UPDATED_BY, repositoryContext.currentUser());
	}

	/**
	 * Creates a restore update.
	 *
	 * @param repositoryContext Repository execution context.
	 *
	 * @return Mongo update.
	 */
	private Update buildRestoreUpdate(final RepositoryContext repositoryContext) {

		return createUpdate()

				.set(EntityFieldConstants.DELETED_FLAG, RepositoryConstants.ACTIVE)

				.set(EntityFieldConstants.DELETED_AT, null)

				.set(EntityFieldConstants.DELETED_BY, null)

				.set(EntityFieldConstants.UPDATED_AT, repositoryContext.currentDateTime())

				.set(EntityFieldConstants.UPDATED_BY, repositoryContext.currentUser());
	}

	// =========================================================================
	// Generic Mongo Execution Methods
	// =========================================================================

	/**
	 * Retrieves a single entity.
	 *
	 * @param query Mongo query.
	 *
	 * @return Matching entity if found.
	 */
	private Optional<T> executeFindOne(final Query query) {

		return Optional.ofNullable(mongoOperations.findOne(query, entityClass));
	}

	/**
	 * Retrieves all matching entities.
	 *
	 * @param query Mongo query.
	 *
	 * @return Matching entities.
	 */
	private List<T> executeFindAll(final Query query) {

		return mongoOperations.find(query, entityClass);
	}

	/**
	 * Determines whether at least one matching entity exists.
	 *
	 * @param query Mongo query.
	 *
	 * @return True if entity exists.
	 */
	private boolean executeExists(final Query query) {

		return mongoOperations.exists(query, entityClass);
	}

	/**
	 * Counts matching entities.
	 *
	 * @param query Mongo query.
	 *
	 * @return Matching entity count.
	 */
	private long executeCount(final Query query) {

		return mongoOperations.count(query, entityClass);
	}

	// =========================================================================
	// Soft Delete Operations
	// =========================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T softDelete(final ID id, final RepositoryContext repositoryContext) {

		final Query query = activeIdQuery(id);

		final Update update = buildSoftDeleteUpdate(repositoryContext);
		final T entity = mongoOperations.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true),
				entityClass);

		if (entity == null) {
			throw new ResourceNotFoundException(CommonErrorConstants.RESOURCE_NOT_FOUND);
		}

		return entity;
	}

	// =========================================================================
	// Restore Operations
	// =========================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T restore(final ID id, final RepositoryContext repositoryContext) {

		final Query query = deletedIdQuery(id);

		final Update update = buildRestoreUpdate(repositoryContext);

		final T entity = mongoOperations.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true),
				entityClass);

		if (entity == null) {

			LOGGER.warn("Failed to restore {} with id [{}]. Entity not found or already active.",
					entityClass.getSimpleName(), id);

			throw new ResourceNotFoundException(CommonErrorConstants.RESOURCE_NOT_FOUND);
		}

		return entity;
	}

	// =========================================================================
	// Active Read Operations
	// =========================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<T> findActiveById(final ID id) {

		return executeFindOne(activeIdQuery(id));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> findAllActive() {

		return executeFindAll(activeQuery());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<T> findOne(final Query query) {

		return executeFindOne(buildActiveQuery(query));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> findAll(final Query query) {

		return executeFindAll(buildActiveQuery(query));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean exists(final Query query) {

		return executeExists(buildActiveQuery(query));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long count(final Query query) {

		return executeCount(buildActiveQuery(query));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<T> findDeletedById(ID id) {

		Query query = deletedIdQuery(id);

		return Optional.ofNullable(mongoOperations.findOne(query, entityClass));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> findAllDeleted() {

		Query query = deletedQuery();

		return mongoOperations.find(query, entityClass);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deletePermanently(ID id) {

		mongoOperations.remove(deletedIdQuery(id), entityClass);
	}

}