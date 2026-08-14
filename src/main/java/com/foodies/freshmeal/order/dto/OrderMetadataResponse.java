package com.foodies.freshmeal.order.dto;

import java.io.Serial;
import java.io.Serializable;

import com.foodies.freshmeal.common.dto.DisplayOptionResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * OrderMetadataResponse
 * ============================================================================
 *
 * Represents technical, platform, marketing, and source metadata associated
 * with an order.
 *
 * This DTO is an API representation of OrderMetadata.
 *
 * Metadata is primarily useful for:
 *
 * - Analytics
 * - Reporting
 * - Auditing
 * - Debugging
 * - Marketing attribution
 * - Platform analysis
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMetadataResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Order Source
    // =========================================================================

    /**
     * Source from which the order originated.
     *
     * Examples:
     *
     * WEB
     * MOBILE_APP
     * ADMIN_PANEL
     * POS
     */
    private DisplayOptionResponse orderSource;

    // =========================================================================
    // Platform & Device
    // =========================================================================

    /**
     * Platform from which the order was placed.
     */
    private DisplayOptionResponse platformType;

    /**
     * Device type used to place the order.
     */
    private DisplayOptionResponse deviceType;

    /**
     * Application version used when the order was placed.
     */
    private String applicationVersion;

    // =========================================================================
    // Technical Information
    // =========================================================================

    /**
     * Client IP address captured when the order was placed.
     */
    private String ipAddress;

    /**
     * Browser or device user-agent.
     */
    private String userAgent;

    // =========================================================================
    // Marketing & Attribution
    // =========================================================================

    /**
     * Marketing campaign associated with the order.
     */
    private String campaignCode;

    /**
     * Referral source associated with the order.
     */
    private String referralSource;

    /**
     * Coupon code associated with the order.
     */
    private String couponCode;

    // =========================================================================
    // Order Flags
    // =========================================================================

    /**
     * Indicates whether the order was placed as a gift order.
     */
    private Boolean giftOrder;

    /**
     * Indicates whether the order was scheduled.
     */
    private Boolean scheduledOrder;

}