
package com.foodies.freshmeal.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : ApplicationNumberRequestDTO
 * ============================================================================
 *
 * <p>
 * Carries the application number required to identify an existing
 * onboarding application.
 * </p>
 *
 * <p>
 * This is an internal service input DTO used for application retrieval
 * and lifecycle operations.
 * </p>
 *
 * <p>
 * Applicant ownership and Admin authorization must be validated by
 * the service layer using the authenticated service context.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 2.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationNumberRequestDTO {

    /**
     * Unique business application number.
     */
    private String applicationNumber;

}
