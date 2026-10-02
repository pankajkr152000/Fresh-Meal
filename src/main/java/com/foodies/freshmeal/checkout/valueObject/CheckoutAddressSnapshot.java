
package com.foodies.freshmeal.checkout.valueObject;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents a snapshot of the delivery address selected for a checkout.
 *
 * <p>
 * The snapshot preserves the address information reviewed by the
 * customer at the time of checkout. Subsequent modifications to the
 * original saved address must not alter this snapshot.
 * </p>
 *
 * <p>
 * The address may originate from a customer's saved address or
 * from a newly entered delivery address.
 * </p>
 *
 * @author Pankaj Kumar
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutAddressSnapshot implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Identifier of the original saved address.
     *
     * <p>
     * Null when the customer provides a new address that has not
     * been saved in the address module.
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
     * Primary address line containing house, building, or street details.
     */
    private String addressLine;

    /**
     * Additional location information, if available.
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
     * State or province associated with the delivery address.
     */
    private String state;

    /**
     * Country associated with the delivery address.
     */
    private String country;

    /**
     * Postal or ZIP code associated with the delivery address.
     */
    private String pincode;
}