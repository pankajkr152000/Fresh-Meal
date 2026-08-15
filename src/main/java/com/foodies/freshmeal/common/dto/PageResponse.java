package com.foodies.freshmeal.common.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : PageResponse
 * ============================================================================
 *
 * Generic paginated response used across the FreshMeal application.
 *
 * <p>
 * This response is intentionally domain-independent so that the same
 * pagination contract can be reused by:
 * </p>
 *
 * <ul>
 * <li>Food Management</li>
 * <li>Order Management</li>
 * <li>Restaurant Management</li>
 * <li>Customer Management</li>
 * <li>Delivery Partner Management</li>
 * <li>Coupon Management</li>
 * <li>Future Admin modules</li>
 * </ul>
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Records returned for the requested page.
     */
    @Builder.Default
    private List<T> content = Collections.emptyList();

    /**
     * Zero-based current page number.
     */
    private int page;

    /**
     * Number of records requested per page.
     */
    private int size;

    /**
     * Total number of records matching the search/filter criteria.
     */
    private long totalElements;

    /**
     * Total number of available pages.
     */
    private int totalPages;

    /**
     * Indicates whether this is the first page.
     */
    private boolean first;

    /**
     * Indicates whether this is the last page.
     */
    private boolean last;

    /**
     * Indicates whether the current page contains no records.
     */
    private boolean empty;
}