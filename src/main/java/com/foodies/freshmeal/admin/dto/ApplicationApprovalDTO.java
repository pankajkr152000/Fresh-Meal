
package com.foodies.freshmeal.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : ApplicationApprovalDTO
 * ============================================================================
 *
 * <p>
 * Carries the information required to approve a Restaurant or Delivery
 * Partner onboarding application.
 * </p>
 *
 * <p>
 * This is an internal service input DTO used when an authorized Admin
 * makes the final approval decision for an application under review.
 * </p>
 *
 * <p>
 * The reviewer identity, approval timestamp, and corresponding role grant
 * are managed by the service layer.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 2.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationApprovalDTO {

    /**
     * Unique business application number.
     */
    private String applicationNumber;

    /**
     * Optional remarks provided by the Admin during approval.
     */
    private String remarks;

}
