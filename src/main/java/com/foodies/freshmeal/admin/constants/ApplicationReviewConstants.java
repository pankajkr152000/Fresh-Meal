package com.foodies.freshmeal.admin.constants;

/**
 * ============================================================================
 * Class : Application Review Constants
 * ============================================================================
 *
 * Contains constants used by the Admin Application Review module.
 *
 * ============================================================================
 */
public final class ApplicationReviewConstants {

    public static final String COLLECTION_NAME = "fm_application_review";

    public static final String FIELD_APPLICATION_ID = "applicationId";

    public static final String FIELD_APPLICATION_NUMBER = "applicationNumber";

    public static final String FIELD_APPLICATION_TYPE = "applicationType";

    public static final String FIELD_APPLICANT_USER_NUMBER = "applicantUserNumber";

    public static final String FIELD_APPLICATION_STATUS = "applicationStatus";

    public static final String FIELD_DOMAIN_ENTITY_ID = "domainEntityId";

    public static final String FIELD_DOMAIN_REFERENCE_NUMBER = "domainReferenceNumber";

    public static final String FIELD_PREVIOUS_APPLICATION_NUMBER = "previousApplicationNumber";

    public static final String FIELD_SUBMITTED_AT = "submittedAt";

    public static final String FIELD_DECISION_AT = "decisionAt";

    public static final String FIELD_REVIEW_HISTORY = "reviewHistory";

    private ApplicationReviewConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }
}