
package com.foodies.freshmeal.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : ApplicationCancellationDTO
 * ============================================================================
 *
 * <p>
 * Carries the information required to cancel a Restaurant or Delivery
 * Partner onboarding application.
 * </p>
 *
 * <p>
 * This is an internal service input DTO used when an authorized Admin
 * or System cancels an application that has not reached a terminal status.
 * </p>
 *
 * <p>
 * The cancellation actor and timestamp are resolved by the service layer.
 * System-initiated cancellations use the configured system actor.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 2.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationCancellationDTO {

    /**
     * Unique business application number.
     */
    private String applicationNumber;

    /**
     * Reason explaining why the application is cancelled.
     */
    private String cancellationReason;

}
