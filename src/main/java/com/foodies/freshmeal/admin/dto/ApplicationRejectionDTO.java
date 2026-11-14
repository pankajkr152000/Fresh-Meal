
package com.foodies.freshmeal.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : ApplicationRejectionDTO
 * ============================================================================
 *
 * <p>
 * Carries the information required to reject a Restaurant or Delivery
 * Partner onboarding application.
 * </p>
 *
 * <p>
 * This is an internal service input DTO used when an authorized Admin
 * makes the final rejection decision for an application under review.
 * </p>
 *
 * <p>
 * A rejection reason must be provided so that the applicant can
 * understand why the application was rejected.
 * </p>
 *
 * <p>
 * The reviewer identity and rejection timestamp are resolved by the
 * service layer using the authenticated service context and application
 * calendar.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 2.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRejectionDTO {

    /**
     * Unique business application number.
     */
    private String applicationNumber;

    /**
     * Mandatory reason explaining why the application is rejected.
     */
    private String rejectionReason;

}
