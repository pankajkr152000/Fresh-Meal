
package com.foodies.freshmeal.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : ApplicationCorrectionRequestDTO
 * ============================================================================
 *
 * <p>
 * Carries the information required to request corrections to an
 * onboarding application during the Admin review process.
 * </p>
 *
 * <p>
 * This is an internal service input DTO used when an authorized Admin
 * determines that an applicant must correct and resubmit application
 * information or documents.
 * </p>
 *
 * <p>
 * The reviewer identity and correction request timestamp are resolved
 * by the service layer using the authenticated service context and
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
public class ApplicationCorrectionRequestDTO {

    /**
     * Unique business application number.
     */
    private String applicationNumber;

    /**
     * Instructions describing the corrections required from the applicant.
     */
    private String correctionInstructions;

}
