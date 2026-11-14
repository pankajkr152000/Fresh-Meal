
package com.foodies.freshmeal.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : ApplicationResubmissionDTO
 * ============================================================================
 *
 * <p>
 * Carries the information required to resubmit an onboarding application
 * after the applicant has addressed the requested corrections.
 * </p>
 *
 * <p>
 * This is an internal service input DTO used when an applicant resubmits
 * an application currently in NEEDS_CORRECTION status.
 * </p>
 *
 * <p>
 * The existing application number is retained during resubmission.
 * The resubmission action and timestamp are recorded in the application
 * review history by the service layer.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 2.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResubmissionDTO {

    /**
     * Unique business application number of the existing application.
     */
    private String applicationNumber;

    /**
     * Optional remarks provided by the applicant during resubmission.
     */
    private String remarks;

}
