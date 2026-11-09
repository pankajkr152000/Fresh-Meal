
package com.foodies.freshmeal.admin.constants;

import com.foodies.freshmeal.common.constants.HttpStatusCode;
import com.foodies.freshmeal.common.exception.IBusinessError;

/**
 * ============================================================================
 * Enum : Admin Error Constants
 * ============================================================================
 *
 * Contains centralized business errors related to Admin operations and
 * application review management.
 *
 * Error Code Format:
 *
 * FM-ADMIN-XXXXXXXX
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum AdminErrorConstants implements IBusinessError {

    // =========================================================================
    // Application Retrieval and Submission
    // =========================================================================

    /**
     * Requested application review does not exist.
     */
    APPLICATION_REVIEW_NOT_FOUND(
            "FM-ADMIN-001",
            "Application review not found.",
            HttpStatusCode.NOT_FOUND),

    /**
     * Applicant already has an active application of the same type.
     */
    ACTIVE_APPLICATION_ALREADY_EXISTS(
            "FM-ADMIN-002",
            "An active application of this type already exists.",
            HttpStatusCode.CONFLICT),

    /**
     * Application could not be submitted.
     */
    APPLICATION_SUBMISSION_FAILED(
            "FM-ADMIN-021",
            "Unable to submit the application.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    // =========================================================================
    // Application Lifecycle
    // =========================================================================

    /**
     * Requested application status transition is not permitted.
     */
    INVALID_APPLICATION_STATUS_TRANSITION(
            "FM-ADMIN-003",
            "Invalid application status transition.",
            HttpStatusCode.CONFLICT),

    /**
     * Application cannot be modified in its current status.
     */
    APPLICATION_MODIFICATION_NOT_ALLOWED(
            "FM-ADMIN-012",
            "Application cannot be modified in its current status.",
            HttpStatusCode.CONFLICT),

    /**
     * Application is not eligible for resubmission.
     */
    APPLICATION_NOT_ELIGIBLE_FOR_RESUBMISSION(
            "FM-ADMIN-011",
            "Application is not eligible for resubmission.",
            HttpStatusCode.CONFLICT),

    /**
     * Application cannot be withdrawn in its current status.
     */
    APPLICATION_WITHDRAWAL_NOT_ALLOWED(
            "FM-ADMIN-013",
            "Application cannot be withdrawn in its current status.",
            HttpStatusCode.CONFLICT),

    /**
     * Application cannot be cancelled in its current status.
     */
    APPLICATION_CANCELLATION_NOT_ALLOWED(
            "FM-ADMIN-014",
            "Application cannot be cancelled in its current status.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Application Decision
    // =========================================================================

    /**
     * Rejection reason is mandatory when rejecting an application.
     */
    APPLICATION_REJECTION_REASON_REQUIRED(
            "FM-ADMIN-005",
            "A rejection reason is required.",
            HttpStatusCode.BAD_REQUEST),

    /**
     * Correction instructions are mandatory when requesting corrections.
     */
    APPLICATION_CORRECTION_INSTRUCTIONS_REQUIRED(
            "FM-ADMIN-006",
            "Correction instructions are required.",
            HttpStatusCode.BAD_REQUEST),

    /**
     * Application decision has already been processed.
     */
    APPLICATION_DECISION_ALREADY_PROCESSED(
            "FM-ADMIN-020",
            "Application decision has already been processed.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Application Ownership and Reapplication
    // =========================================================================

    /**
     * Application does not belong to the requesting applicant.
     */
    APPLICATION_OWNERSHIP_MISMATCH(
            "FM-ADMIN-004",
            "Application does not belong to the requesting applicant.",
            HttpStatusCode.FORBIDDEN),

    /**
     * Previous application reference does not exist.
     */
    PREVIOUS_APPLICATION_NOT_FOUND(
            "FM-ADMIN-007",
            "Previous application not found.",
            HttpStatusCode.NOT_FOUND),

    /**
     * Previous application must be rejected before reapplication.
     */
    PREVIOUS_APPLICATION_NOT_REJECTED(
            "FM-ADMIN-008",
            "Previous application must be rejected before reapplication.",
            HttpStatusCode.CONFLICT),

    /**
     * Previous application belongs to a different applicant.
     */
    PREVIOUS_APPLICATION_APPLICANT_MISMATCH(
            "FM-ADMIN-009",
            "Previous application belongs to a different applicant.",
            HttpStatusCode.CONFLICT),

    /**
     * Previous application has a different application type.
     */
    PREVIOUS_APPLICATION_TYPE_MISMATCH(
            "FM-ADMIN-010",
            "Previous application has a different application type.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Applicant and Domain Validation
    // =========================================================================

    /**
     * Applicant account is not active.
     */
    APPLICANT_ACCOUNT_NOT_ACTIVE(
            "FM-ADMIN-015",
            "Applicant account is not active.",
            HttpStatusCode.FORBIDDEN),

    /**
     * Associated restaurant or delivery partner entity does not exist.
     */
    APPLICATION_DOMAIN_ENTITY_NOT_FOUND(
            "FM-ADMIN-016",
            "Associated domain entity not found.",
            HttpStatusCode.NOT_FOUND),

    /**
     * Domain entity reference does not match the application reference.
     */
    APPLICATION_DOMAIN_REFERENCE_MISMATCH(
            "FM-ADMIN-017",
            "Application domain reference does not match.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Role Granting
    // =========================================================================

    /**
     * Required application role could not be granted after approval.
     */
    APPLICATION_ROLE_GRANT_FAILED(
            "FM-ADMIN-018",
            "Unable to grant the required application role.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    // =========================================================================
    // Persistence and Concurrency
    // =========================================================================

    /**
     * Application was modified concurrently by another operation.
     */
    APPLICATION_CONCURRENT_MODIFICATION(
            "FM-ADMIN-019",
            "Application was modified by another operation. Please retry.",
            HttpStatusCode.CONFLICT),

    /**
     * Application review history could not be updated.
     */
    APPLICATION_REVIEW_HISTORY_UPDATE_FAILED(
            "FM-ADMIN-022",
            "Unable to update application review history.",
            HttpStatusCode.INTERNAL_SERVER_ERROR);

    // =========================================================================
    // Fields
    // =========================================================================

    private final String errorCode;

    private final String errorMessage;

    private final HttpStatusCode httpStatusCode;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Creates an Admin business error definition.
     *
     * @param errorCode      unique business error code
     * @param errorMessage   descriptive error message
     * @param httpStatusCode associated HTTP status
     */
    AdminErrorConstants(
            final String errorCode,
            final String errorMessage,
            final HttpStatusCode httpStatusCode) {

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatusCode = httpStatusCode;
    }

    // =========================================================================
    // Getters
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }
}
