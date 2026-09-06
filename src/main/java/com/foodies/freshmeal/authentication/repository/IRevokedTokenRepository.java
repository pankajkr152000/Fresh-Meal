package com.foodies.freshmeal.authentication.repository;

import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.authentication.entity.RevokedTokenEntity;
import com.foodies.freshmeal.common.repository.base.IBaseRepository;

/**
 * ============================================================================
 * Repository : IRevokedTokenRepository
 * ============================================================================
 *
 * <p>
 * Provides persistence access for revoked JWT records.
 * </p>
 *
 * <p>
 * The repository extends FreshMeal's common {@link IBaseRepository} so that
 * standard active-record filtering, soft deletion, restoration, and permanent
 * deletion remain consistent with the rest of the application.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Repository
public interface IRevokedTokenRepository extends IBaseRepository<RevokedTokenEntity, String> {

}