
package com.foodies.freshmeal.admin.dto;

import com.foodies.freshmeal.admin.constants.ApplicationType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : ApplicationSubmissionDTO
 * ============================================================================
 *
 * <p>
 * Carries the application reference information required to register a
 * Restaurant or Delivery Partner onboarding application for Admin review.
 * </p>
 *
 * <p>
 * This is an internal service input DTO used after the corresponding
 * domain onboarding entity has been created successfully.
 * </p>
 *
 * <p>
 * The applicant identity and application number are resolved or generated
 * by the respective service layer.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 2.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSubmissionDTO {

    /**
     * Type of onboarding application.
     */
    private ApplicationType applicationType;

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
     * when this submission represents a reapplication.
     */
    private String previousApplicationNumber;

}
