
package com.foodies.freshmeal.admin.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : ApplicationReviewPageResponse
 * ============================================================================
 *
 * <p>
 * Carries a paginated collection of application review responses along
 * with pagination metadata.
 * </p>
 *
 * <p>
 * This DTO is used by applicant-facing and Admin-facing application
 * listing operations.
 * </p>
 *
 * <p>
 * The application records are represented using ApplicationReviewResponse
 * rather than exposing the underlying persistence entities.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 2.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationReviewPageResponse {

    /**
     * Application review records for the current page.
     */
    private List<ApplicationReviewResponse> applications;

    /**
     * Current zero-based page number.
     */
    private int pageNumber;

    /**
     * Number of records requested per page.
     */
    private int pageSize;

    /**
     * Total number of matching application records.
     */
    private long totalElements;

    /**
     * Total number of available pages.
     */
    private int totalPages;

    /**
     * Indicates whether a previous page is available.
     */
    private boolean hasPrevious;

    /**
     * Indicates whether a next page is available.
     */
    private boolean hasNext;

}
