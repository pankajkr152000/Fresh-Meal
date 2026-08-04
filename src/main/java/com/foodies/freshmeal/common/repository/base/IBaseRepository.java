package com.foodies.freshmeal.common.repository.base;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.foodies.freshmeal.common.entity.ABaseEntity;

/**
 * ============================================================================
 * Base Repository
 * ============================================================================
 *
 * <p>
 * Defines the common repository contract for all MongoDB entities within the
 * FreshMeal platform.
 * </p>
 *
 * <p>
 * This interface extends Spring Data's {@link MongoRepository} to inherit all
 * standard CRUD, pagination, sorting and batch operations while also exposing
 * enterprise repository capabilities defined in
 * {@link IBaseRepositoryCustom}.
 * </p>
 *
 * <p>
 * Every domain repository should extend this interface instead of directly
 * extending {@link MongoRepository}.
 * </p>
 *
 * <pre>
 * Example:
 *
 * {@code
 * @Repository
 * public interface IFoodRepository
 *         extends IBaseRepository<FoodEntity, String> {
 * }
 * }
 * </pre>
 *
 * <p>
 * Enterprise Features:
 * </p>
 *
 * <ul>
 * <li>Soft Delete Support</li>
 * <li>Restore Support</li>
 * <li>Active Record Retrieval</li>
 * <li>Generic Query Operations</li>
 * <li>Future Search & Pagination Framework</li>
 * </ul>
 *
 * @param <T>  Domain entity type.
 * @param <ID> Primary key type.
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@NoRepositoryBean
public interface IBaseRepository<T extends ABaseEntity, ID>
        extends MongoRepository<T, ID>,
        IBaseRepositoryCustom<T, ID> {

}