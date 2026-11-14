
package com.foodies.freshmeal.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : ApplicationDecisionDTO
 * ============================================================================
 *
 * <p>
 * Carries the information required to initiate the review of a submitted
 * onboarding application.
 * </p>
 *
 * <p>
 * This is an internal service input DTO used when an authorized Admin
 * starts reviewing an application.
 * </p>
 *
 * <p>
 * The reviewer identity and review timestamp are resolved by the service
 * layer using the authenticated service context and application calendar.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 2.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDecisionDTO {

    /**
     * Unique business application number.
     */
    private String applicationNumber;

    /**
     * Optional remarks provided by the Admin when starting the review.
     */
    private String remarks;

}
