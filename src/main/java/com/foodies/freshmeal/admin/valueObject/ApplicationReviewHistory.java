package com.foodies.freshmeal.admin.valueObject;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.foodies.freshmeal.admin.constants.ApplicationReviewActionConstant;

import lombok.Getter;

/**
 * ============================================================================
 * Value Object : Application Review History
 * ============================================================================
 *
 * Represents an individual action performed during the lifecycle of an
 * onboarding application.
 *
 * <p>
 * This value object is embedded inside ApplicationReviewEntity to maintain
 * the chronological history of application-related activities.
 * </p>
 *
 * <p>
 * History entries are intended to be append-only. Existing entries should
 * not be modified or removed during normal application operations.
 * </p>
 *
 * ============================================================================
 */
@Getter
public final class ApplicationReviewHistory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Action performed on the application.
     */
    private final ApplicationReviewActionConstant action;

    /**
     * Business identifier of the user or system responsible for the action.
     */
    private final String performedByUserNumber;

    /**
     * Business timestamp when the action was performed.
     */
    private final LocalDateTime performedAt;

    /**
     * Optional remarks associated with the action.
     */
    private final String remarks;

    /**
     * Creates an application review history entry.
     *
     * @param action                action performed
     * @param performedByUserNumber business identifier of the actor
     * @param performedAt           business timestamp
     * @param remarks               optional remarks
     */
    private ApplicationReviewHistory(
            final ApplicationReviewActionConstant action,
            final String performedByUserNumber,
            final LocalDateTime performedAt,
            final String remarks) {

        this.action = Objects.requireNonNull(action, "Action cannot be null.");
        this.performedByUserNumber = Objects.requireNonNull(
                performedByUserNumber, "Performed by user number cannot be null.");
        this.performedAt = Objects.requireNonNull(
                performedAt, "Performed at cannot be null.");
        this.remarks = remarks;
    }

    /**
     * Creates a new application review history entry.
     *
     * @param action                action performed
     * @param performedByUserNumber business identifier of the actor
     * @param performedAt           business timestamp
     * @param remarks               optional remarks
     * @return application review history value object
     */
    public static ApplicationReviewHistory create(
            final ApplicationReviewActionConstant action,
            final String performedByUserNumber,
            final LocalDateTime performedAt,
            final String remarks) {

        return new ApplicationReviewHistory(
                action,
                performedByUserNumber,
                performedAt,
                remarks);
    }
}