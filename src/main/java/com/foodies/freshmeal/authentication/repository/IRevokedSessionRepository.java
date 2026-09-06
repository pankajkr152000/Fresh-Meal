package com.foodies.freshmeal.authentication.repository;

import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.authentication.entity.RevokedSessionEntity;
import com.foodies.freshmeal.common.repository.base.IBaseRepository;

/**
 * ============================================================================
 * Repository : IRevokedSessionRepository
 * ============================================================================
 *
 * Repository contract for {@link RevokedSessionEntity}.
 *
 * <p>
 * Uses the common FreshMeal repository infrastructure so authentication
 * session revocation follows the same persistence conventions as the rest
 * of the application.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Repository
public interface IRevokedSessionRepository extends IBaseRepository<RevokedSessionEntity, String> {

}