
package com.foodies.freshmeal.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : ApplicationWithdrawalDTO
 * ============================================================================
 *
 * <p>
 * Carries the information required to withdraw a Restaurant or Delivery
 * Partner onboarding application.
 * </p>
 *
 * <p>
 * This is an internal service input DTO used when an authenticated
 * applicant voluntarily withdraws their own application.
 * </p>
 *
 * <p>
 * The applicant identity and withdrawal timestamp are resolved by
 * the service layer using the authenticated service context and
 * application calendar.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 2.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationWithdrawalDTO {

    /**
     * Unique business application number.
     */
    private String applicationNumber;

    /**
     * Optional reason provided by the applicant for withdrawing
     * the application.
     */
    private String withdrawalReason;

}
