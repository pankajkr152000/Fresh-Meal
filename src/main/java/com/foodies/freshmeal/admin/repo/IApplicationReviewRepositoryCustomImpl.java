package com.foodies.freshmeal.admin.repo;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.admin.constants.ApplicationReviewConstants;
import com.foodies.freshmeal.admin.constants.ApplicationStatusConstant;
import com.foodies.freshmeal.admin.dto.ApplicationStatusTransitionDTO;
import com.foodies.freshmeal.admin.entity.ApplicationReviewEntity;
import com.foodies.freshmeal.admin.valueObject.ApplicationReviewHistory;
import com.foodies.freshmeal.common.constants.EntityFieldConstants;
import com.foodies.freshmeal.common.constants.RepositoryConstants;

/**
 * ============================================================================
 * Application Review Custom Repository Implementation
 * ============================================================================
 *
 * <p>
 * Provides application-specific atomic persistence operations.
 * </p>
 *
 * <p>
 * Status transitions are guarded by the expected current status to prevent
 * concurrent requests from overwriting one another.
 * </p>
 */
@Repository
public class IApplicationReviewRepositoryCustomImpl
        implements IApplicationReviewRepositoryCustom {

    /**
     * MongoDB operations.
     */
    private final MongoOperations mongoOperations;

    /**
     * Creates the custom repository implementation.
     *
     * @param mongoOperations MongoDB operations.
     */
    public IApplicationReviewRepositoryCustomImpl(
            final MongoOperations mongoOperations) {

        this.mongoOperations = mongoOperations;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The operation succeeds only when the application exists, is active,
     * and its current status matches the expected status.
     * </p>
     */
    @Override
    public Optional<ApplicationReviewEntity> transitionStatus(
            final ApplicationStatusTransitionDTO transitionDTO) {

        Objects.requireNonNull(transitionDTO, "Transition DTO must not be null.");

        final String applicationNumber = Objects.requireNonNull(
                transitionDTO.getApplicationNumber(),
                "Application number must not be null.");

        final ApplicationStatusConstant expectedStatus = Objects.requireNonNull(
                transitionDTO.getExpectedStatus(),
                "Expected status must not be null.");

        final ApplicationStatusConstant targetStatus = Objects.requireNonNull(
                transitionDTO.getTargetStatus(),
                "Target status must not be null.");

        final ApplicationReviewHistory reviewHistory = Objects.requireNonNull(
                transitionDTO.getReviewHistory(),
                "Review history must not be null.");

        /*
         * Match only the active application whose current status is the
         * expected status.
         */
        final Query query = Query.query(
                Criteria.where(ApplicationReviewConstants.FIELD_APPLICATION_NUMBER)
                        .is(applicationNumber)
                        .and(ApplicationReviewConstants.FIELD_APPLICATION_STATUS)
                        .is(expectedStatus)
                        .and(EntityFieldConstants.DELETED_FLAG)
                        .is(RepositoryConstants.ACTIVE));

        /*
         * Prepare the atomic status and audit update.
         */
        final Update update = new Update()
                .set(
                        ApplicationReviewConstants.FIELD_APPLICATION_STATUS,
                        targetStatus)
                .push(
                        ApplicationReviewConstants.FIELD_REVIEW_HISTORY,
                        reviewHistory)
                .set(
                        EntityFieldConstants.UPDATED_AT,
                        reviewHistory.getPerformedAt())
                .set(
                        EntityFieldConstants.UPDATED_BY,
                        reviewHistory.getPerformedByUserNumber())
                .set(
                        ApplicationReviewConstants.FIELD_IS_ACTIVE_APPLICATION,
                        !targetStatus.isTerminal());

        /*
         * Record the final administrative decision timestamp.
         */
        if (targetStatus == ApplicationStatusConstant.APPROVED
                || targetStatus == ApplicationStatusConstant.REJECTED) {

            update.set(
                    ApplicationReviewConstants.FIELD_DECISION_AT,
                    reviewHistory.getPerformedAt());
        }

        /*
         * Execute the atomic update and return the updated application.
         *
         * An empty result indicates that the application was not found,
         * was deleted, or its status changed concurrently.
         */
        final ApplicationReviewEntity updatedEntity = mongoOperations.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true),
                ApplicationReviewEntity.class);

        return Optional.ofNullable(updatedEntity);
    }
}
