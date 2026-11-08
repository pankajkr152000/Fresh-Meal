
package com.foodies.freshmeal.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =============================================================================
 * DTO : RegistrationIdentityReservationResponse
 * =============================================================================
 *
 * Purpose
 * -------
 * Represents the outcome of a registration identity reservation operation.
 *
 * Responsibilities
 * -----------------
 * - Indicates whether the requested reservation operation succeeded.
 * - Provides a concise description of the operation outcome.
 *
 * Business Rules
 * --------------
 * - The response does not expose reservation persistence details.
 * - Reservation ownership and identity attributes are not returned.
 * - Operation failure details are handled through centralized exceptions.
 *
 * =============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationIdentityReservationResponse {

    /**
     * Indicates whether the requested reservation operation succeeded.
     */
    private boolean successful;

    /**
     * Describes the result of the reservation operation.
     */
    private String message;
}
