
package com.foodies.freshmeal.admin.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.foodies.freshmeal.admin.constants.ApplicationStatusConstant;
import com.foodies.freshmeal.admin.constants.ApplicationType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : ApplicationReviewResponse
 * ============================================================================
 *
 * <p>
 * Carries the application review information returned by Admin Application
 * Review service operations.
 * </p>
 *
 * <p>
 * Contains application identification, applicant reference, domain reference,
 * lifecycle status, submission information, decision information, and
 * application review history.
 * </p>
 *
 * <p>
 * This response DTO exposes application review information without exposing
 * the underlying ApplicationReviewEntity.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 2.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationReviewResponse {

    /**
     * Unique business application number.
     */
    private String applicationNumber;

    /**
     * Type of onboarding application.
     */
    private ApplicationType applicationType;

    /**
     * User number of the applicant.
     */
    private String applicantUserNumber;

    /**
     * Current application lifecycle status.
     */
    private ApplicationStatusConstant applicationStatus;

    /**
     * Database identifier of the associated domain entity.
     */
    private String domainEntityId;

    /**
     * Business reference number of the associated domain entity.
     */
    private String domainReferenceNumber;

    /**
     * Application number of the previously rejected application,
     * if this application is a reapplication.
     */
    private String previousApplicationNumber;

    /**
     * Timestamp when the application was initially submitted.
     */
    private LocalDateTime submittedAt;

    /**
     * Timestamp when the final application decision was made.
     */
    private LocalDateTime decisionAt;

    /**
     * Complete review history associated with the application.
     */
    private List<ApplicationReviewHistoryResponse> reviewHistory;

}
