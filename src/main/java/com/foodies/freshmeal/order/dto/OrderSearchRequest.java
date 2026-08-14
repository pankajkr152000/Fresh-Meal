package com.foodies.freshmeal.order.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * OrderSearchRequest
 * ============================================================================
 *
 * Represents the search, filtering, sorting, and pagination criteria used by
 * the Admin Order Management screen.
 *
 * This DTO is intentionally designed around the Order Management use case
 * rather than directly exposing MongoDB query parameters.
 *
 * ============================================================================
 *
 * Supported Operations
 * --------------------
 *
 * Search
 * - Order number
 * - Customer name
 * - Customer mobile number
 *
 * Filtering
 * - Order status
 * - Delivery status
 * - Payment status
 * - Order type
 * - Restaurant
 * - Customer
 * - Date range
 *
 * Sorting
 * - Configurable field
 * - Ascending / descending direction
 *
 * Pagination
 * - Page number
 * - Page size
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Search
    // =========================================================================

    /**
     * General search text.
     *
     * The service layer decides which searchable fields are matched.
     *
     * Examples:
     *
     * FM202608140001
     * Pankaj Kumar
     * 9876543210
     */
    @Size(max = 150)
    private String search;

    // =========================================================================
    // Status Filters
    // =========================================================================

    /**
     * Order status values used for filtering.
     *
     * Example:
     *
     * ["PLACED", "CONFIRMED"]
     */
    private List<String> orderStatuses;

    /**
     * Delivery status values used for filtering.
     *
     * Example:
     *
     * ["ASSIGNED", "OUT_FOR_DELIVERY"]
     */
    private List<String> deliveryStatuses;

    /**
     * Payment status values used for filtering.
     *
     * Example:
     *
     * ["PAID", "PENDING"]
     */
    private List<String> paymentStatuses;

    // =========================================================================
    // Order Type
    // =========================================================================

    /**
     * Order type values used for filtering.
     *
     * Example:
     *
     * ["DELIVERY", "TAKEAWAY"]
     */
    private List<String> orderTypes;

    // =========================================================================
    // Customer / Restaurant
    // =========================================================================

    /**
     * Customer identifier used for filtering.
     */
    private String customerId;

    /**
     * Restaurant identifier used for filtering.
     */
    private String restaurantId;

    // =========================================================================
    // Date Range
    // =========================================================================

    /**
     * Start of the order creation/placement date range.
     */
    private LocalDateTime fromDate;

    /**
     * End of the order creation/placement date range.
     */
    private LocalDateTime toDate;

    // =========================================================================
    // Pagination
    // =========================================================================

    /**
     * Zero-based page number.
     *
     * Example:
     *
     * 0 = first page
     * 1 = second page
     */
    @Builder.Default
    @Min(0)
    private Integer page = 0;

    /**
     * Number of records returned per page.
     */
    @Builder.Default
    @Min(1)
    @Max(100)
    private Integer size = 20;

    // =========================================================================
    // Sorting
    // =========================================================================

    /**
     * Field by which the result should be sorted.
     *
     * Example:
     *
     * orderedAt
     * orderNumber
     * priceSummary.grandTotal
     */
    @Builder.Default
    private String sortBy = "orderedAt";

    /**
     * Sort direction.
     *
     * Supported values:
     *
     * ASC
     * DESC
     */
    @Builder.Default
    private String sortDirection = "DESC";

}