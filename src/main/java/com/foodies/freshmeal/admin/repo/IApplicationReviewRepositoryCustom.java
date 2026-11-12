package com.foodies.freshmeal.admin.repo;

import java.util.Optional;

import com.foodies.freshmeal.admin.dto.ApplicationStatusTransitionDTO;
import com.foodies.freshmeal.admin.entity.ApplicationReviewEntity;

/**
 * ============================================================================
 * Application Review Custom Repository Contract
 * ============================================================================
 *
 * <p>
 * Defines application-specific atomic persistence operations.
 * </p>
 */
public interface IApplicationReviewRepositoryCustom {

    /**
     * Atomically transitions an application's status.
     *
     * @param transitionDTO Status transition details.
     * @return Updated application if the transition succeeds.
     */
    Optional<ApplicationReviewEntity> transitionStatus(ApplicationStatusTransitionDTO transitionDTO);
}
