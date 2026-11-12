
package com.foodies.freshmeal.admin.entity;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.admin.constants.ApplicationReviewConstants;
import com.foodies.freshmeal.admin.constants.ApplicationStatusConstant;
import com.foodies.freshmeal.admin.constants.ApplicationType;
import com.foodies.freshmeal.admin.valueObject.ApplicationReviewHistory;
import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Entity : ApplicationReviewEntity
 * ============================================================================
 *
 * Represents a centralized Admin review application for Restaurant or
 * Delivery Partner onboarding.
 *
 * <p>
 * This entity maintains the application lifecycle, applicant reference,
 * associated domain entity reference, and complete review history.
 * </p>
 *
 * <p>
 * Restaurant and Delivery Partner business information remains owned by
 * their respective domain modules. This entity stores only their identifiers
 * and does not duplicate their business data.
 * </p>
 *
 * <h3>Responsibilities</h3>
 *
 * <ul>
 * <li>Maintain the application identity and applicant reference.</li>
 * <li>Identify the type of onboarding application.</li>
 * <li>Track the application review lifecycle.</li>
 * <li>Maintain references to the associated domain entity.</li>
 * <li>Maintain previous application references for reapplications.</li>
 * <li>Maintain the complete embedded review history.</li>
 * </ul>
 *
 * <h3>Identifiers</h3>
 *
 * <ul>
 * <li>{@code id}: Internal database identifier inherited from ABaseEntity.</li>
 * <li>{@code applicationNumber}: Business-facing application identifier.</li>
 * </ul>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@Document(collection = ApplicationReviewConstants.COLLECTION_NAME)
@CompoundIndexes({

        /**
         * Supports applicant application history and active application
         * lookup by applicant, type, and status.
         */
        @CompoundIndex(name = "idx_application_applicant_type_status", def = "{'applicantUserNumber': 1, "
                + "'applicationType': 1, "
                + "'applicationStatus': 1}"),

        /**
         * Supports Admin review queues filtered by application type and
         * application status, ordered by submission time.
         */
        @CompoundIndex(name = "idx_application_type_status_submitted", def = "{'applicationType': 1, "
                + "'applicationStatus': 1, "
                + "'submittedAt': 1}"),

        @CompoundIndex(name = "uk_active_applicant_application_type", def = "{'applicantUserNumber': 1, 'applicationType': 1}", unique = true, partialFilter = "{'isActiveApplication': true}")
})
public class ApplicationReviewEntity extends ABaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Package-private constructor.
     *
     * <p>
     * Entity creation is controlled through the standard FreshMeal
     * entity factory.
     * </p>
     */
    ApplicationReviewEntity() {
        // Package-private constructor.
    }

    // =========================================================================
    // Factory
    // =========================================================================

    /**
     * Creates a new application review entity.
     *
     * @return newly created application review entity.
     */
    public static IEntity create() {
        return new ApplicationReviewEntity();
    }

    // =========================================================================
    // Business Identifier
    // =========================================================================

    /**
     * Unique business-facing application identifier.
     *
     * <p>
     * This identifier is used in APIs, application tracking, notifications,
     * and business-level references.
     * </p>
     */
    @Indexed(unique = true)
    private String applicationNumber;

    // =========================================================================
    // Application Information
    // =========================================================================

    /**
     * Identifies the onboarding application category.
     *
     * <p>
     * Supported application types are RESTAURANT and DELIVERY_PARTNER.
     * </p>
     */
    private ApplicationType applicationType;

    /**
     * Business identifier of the user who submitted the application.
     */
    @Indexed
    private String applicantUserNumber;

    /**
     * Current lifecycle status of the application.
     */
    private ApplicationStatusConstant applicationStatus;

    // =========================================================================
    // Domain Entity Reference
    // =========================================================================

    /**
     * Internal identifier of the associated domain entity.
     *
     * <p>
     * References RestaurantEntity or DeliveryPartnerEntity depending on
     * the application type.
     * </p>
     */
    private String domainEntityId;

    /**
     * Business-facing identifier of the associated domain entity.
     *
     * <p>
     * References restaurantNumber or partnerNumber.
     * </p>
     */
    private String domainReferenceNumber;

    // =========================================================================
    // Reapplication Reference
    // =========================================================================

    /**
     * Business-facing identifier of the previous rejected application.
     *
     * <p>
     * This field is populated only when an applicant submits a new
     * application after a previous rejection.
     * </p>
     */
    private String previousApplicationNumber;

    // =========================================================================
    // Application Timestamps
    // =========================================================================

    /**
     * Timestamp when the application was initially submitted.
     */
    private LocalDateTime submittedAt;

    /**
     * Timestamp when the application received its final Admin decision.
     *
     * <p>
     * This is populated for APPROVED or REJECTED decisions.
     * Other terminal lifecycle events are recorded in review history.
     * </p>
     */
    private LocalDateTime decisionAt;

    // =========================================================================
    // Review History
    // =========================================================================

    /**
     * Complete history of actions performed on this application.
     *
     * <p>
     * History is embedded in the application document because it belongs
     * to the lifecycle of a single application and is normally retrieved
     * together with that application.
     * </p>
     */
    private List<ApplicationReviewHistory> reviewHistory = new ArrayList<>();

    /**
     * Indicates whether the application occupies the applicant's active
     * application slot for its application type.
     *
     * <p>
     * Only one active application is permitted per applicant and type.
     * </p>
     */
    private boolean isActiveApplication;

}
