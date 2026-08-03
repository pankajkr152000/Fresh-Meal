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
 * Represents additional business and technical metadata associated with an
 * order.
 *
 * This information is primarily used for analytics, reporting, marketing,
 * auditing, and debugging.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @version 1.0
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
     * Order source.
     *
     * Example:
     * WEB
     * MOBILE_APP
     * ADMIN_PANEL
     * POS
     */
    private OrderSourceConstant orderSource;

    /**
     * Platform.
     *
     * Example:
     * ANDROID
     * IOS
     * WINDOWS
     * MAC
     * LINUX
     */
    private PlatformTypeConstant platformType;

    /**
     * Device type.
     *
     * Example:
     * MOBILE
     * TABLET
     * DESKTOP
     */
    private DeviceTypeConstant deviceType;

    /**
     * Application version.
     *
     * Example:
     * 1.0.5
     */
    private String applicationVersion;

    /**
     * Client IP Address.
     */
    private String ipAddress;

    /**
     * Browser / Device User-Agent.
     */
    private String userAgent;

    /**
     * Marketing campaign code.
     */
    private String campaignCode;

    /**
     * Referral source.
     *
     * Example:
     * Facebook
     * Instagram
     * Google Ads
     */
    private String referralSource;

    /**
     * Coupon code applied.
     */
    private String couponCode;

    /**
     * Gift order.
     */
    @Builder.Default
    private Boolean giftOrder = Boolean.FALSE;

    /**
     * Scheduled order.
     */
    @Builder.Default
    private Boolean scheduledOrder = Boolean.FALSE;

}