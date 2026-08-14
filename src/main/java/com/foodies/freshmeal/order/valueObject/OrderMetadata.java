package com.foodies.freshmeal.order.valueObject;

import java.io.Serial;
import java.io.Serializable;

import com.foodies.freshmeal.order.constants.DeviceTypeConstant;
import com.foodies.freshmeal.order.constants.OrderSourceConstant;
import com.foodies.freshmeal.order.constants.PlatformTypeConstant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * OrderMetadata
 * ============================================================================
 *
 * Represents technical, marketing and source metadata associated with an
 * order.
 *
 * Business information such as coupon, gift order and scheduling belongs to
 * their respective domain objects and is intentionally not duplicated here.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Source from which the order originated.
     *
     * Examples:
     *
     * WEB
     * MOBILE_APP
     * POS
     */
    private OrderSourceConstant orderSource;

    /**
     * Platform used to create the order.
     */
    private PlatformTypeConstant platformType;

    /**
     * Device used to create the order.
     */
    private DeviceTypeConstant deviceType;

    /**
     * Application version.
     */
    private String applicationVersion;

    /**
     * Client IP address.
     */
    private String ipAddress;

    /**
     * Browser / device user agent.
     */
    private String userAgent;

    /**
     * Marketing campaign code.
     */
    private String campaignCode;

    /**
     * Referral source.
     */
    private String referralSource;

}