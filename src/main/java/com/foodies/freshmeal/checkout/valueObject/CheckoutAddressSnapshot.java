
package com.foodies.freshmeal.checkout.valueObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * Class : CheckoutAddressSnapshot
 * ============================================================================
 *
 * Represents a snapshot of the delivery address selected for a checkout.
 *
 * <p>
 * The snapshot preserves the address details presented to the customer
 * during checkout. Changes to the customer's saved address must not
 * modify an existing checkout snapshot.
 * </p>
 *
 * <p>
 * This class is an embedded value object within CheckoutEntity and does
 * not maintain independent persistence or business lifecycle behavior.
 * </p>
 *
 * ============================================================================
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutAddressSnapshot {

    /**
     * Business identifier of the saved address, when applicable.
     *
     * <p>
     * This field is null when the customer provides a new address
     * without selecting an existing saved address.
     * </p>
     */
    private String addressNumber;

    /**
     * Name of the person receiving the delivery.
     */
    private String recipientName;

    /**
     * Contact number of the recipient.
     */
    private String phoneNumber;

    /**
     * Primary address line containing house number, building,
     * street, or locality details.
     */
    private String addressLine;

    /**
     * Additional delivery landmark, when provided.
     */
    private String landmark;

    /**
     * City associated with the delivery address.
     */
    private String city;

    /**
     * District associated with the delivery address.
     */
    private String district;

    /**
     * State associated with the delivery address.
     */
    private String state;

    /**
     * Country associated with the delivery address.
     */
    private String country;

    /**
     * Postal or PIN code of the delivery address.
     */
    private String pincode;
}