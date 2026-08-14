package com.foodies.freshmeal.order.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * CustomerDetailsResponse
 * ============================================================================
 *
 * Represents customer information displayed on the Admin Order Details page.
 *
 * This DTO is an API representation of CustomerSnapshot and intentionally
 * does not expose the domain Value Object directly.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetailsResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Customer unique identifier.
     */
    private String customerId;

    /**
     * Customer full name.
     */
    private String customerName;

    /**
     * Customer email address.
     */
    private String email;

    /**
     * Customer mobile number.
     */
    private String mobileNumber;

    /**
     * Customer profile image.
     */
    private String profileImageUrl;

    /**
     * Indicates whether the order was placed by a guest customer.
     */
    private Boolean guestCustomer;

    /**
     * Loyalty membership identifier.
     */
    private String membershipId;

    /**
     * Loyalty membership level.
     */
    private String membershipLevel;

    /**
     * GST number, when available.
     */
    private String gstNumber;

}