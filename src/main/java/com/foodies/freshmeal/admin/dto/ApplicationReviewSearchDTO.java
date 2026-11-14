
package com.foodies.freshmeal.admin.dto;

import com.foodies.freshmeal.admin.constants.ApplicationStatusConstant;
import com.foodies.freshmeal.admin.constants.ApplicationType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : ApplicationReviewSearchDTO
 * ============================================================================
 *
 * <p>
 * Carries the search criteria required to retrieve onboarding applications
 * with filtering and pagination support.
 * </p>
 *
 * <p>
 * This is an internal service input DTO used by applicant-facing and
 * Admin-facing application listing operations.
 * </p>
 *
 * <p>
 * Applicant identity and access restrictions are resolved by the service
 * layer using the authenticated service context. The applicant must not
 * be allowed to retrieve applications belonging to other users.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 2.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationReviewSearchDTO {

    /**
     * Application type filter.
     */
    private ApplicationType applicationType;

    /**
     * Application lifecycle status filter.
     */
    private ApplicationStatusConstant applicationStatus;

    /**
     * Search keyword for application number or domain reference number.
     */
    private String searchKeyword;

    /**
     * Zero-based page number.
     */
    private int pageNumber;

    /**
     * Number of records to retrieve per page.
     */
    private int pageSize;

    /**
     * Field used to sort the application results.
     */
    private String sortBy;

    /**
     * Sorting direction, such as ASC or DESC.
     */
    private String sortDirection;

}
