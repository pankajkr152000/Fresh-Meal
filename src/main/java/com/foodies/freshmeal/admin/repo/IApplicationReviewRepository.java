
package com.foodies.freshmeal.admin.repo;

import com.foodies.freshmeal.admin.entity.ApplicationReviewEntity;
import com.foodies.freshmeal.common.repository.base.IBaseRepositoryCustom;

/**
 * ============================================================================
 * Repository : IApplicationReviewRepository
 * ============================================================================
 *
 * <p>
 * Provides persistence operations for Admin application reviews.
 * </p>
 *
 * <p>
 * Shared CRUD and active-query operations are inherited from
 * {@link IBaseRepositoryCustom}. Application-specific atomic operations
 * are declared here.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 2.0
 */
public interface IApplicationReviewRepository
        extends IBaseRepositoryCustom<ApplicationReviewEntity, String>, IApplicationReviewRepositoryCustom {

}
