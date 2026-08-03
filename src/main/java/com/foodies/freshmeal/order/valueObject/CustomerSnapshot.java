package com.foodies.freshmeal.order.valueObject;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * CustomerSnapshot
 * ============================================================================
 *
 * Represents the customer information captured at the time an order is placed.
 *
 * This class is a Value Object and should never be treated as the Customer
 * master record.
 *
 * The purpose of this snapshot is to preserve historical order information.
 * Even if the customer updates their profile later, previously placed orders
 * should continue displaying the original customer details.
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
public class CustomerSnapshot implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Customer unique identifier.
     */
    @NotBlank
    private String customerId;

    /**
     * Customer full name.
     */
    @NotBlank
    @Size(max = 150)
    private String customerName;

    /**
     * Customer email address.
     */
    @Email
    @Size(max = 150)
    private String email;

    /**
     * Customer mobile number.
     */
    @NotBlank
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Invalid mobile number.")
    private String mobileNumber;

    /**
     * Customer profile image.
     *
     * Stored only for display purposes.
     */
    private String profileImageUrl;

    /**
     * Indicates whether the customer is a guest user.
     */
    @Builder.Default
    private Boolean guestCustomer = Boolean.FALSE;

    /**
     * Customer loyalty membership ID.
     *
     * Optional.
     */
    private String membershipId;

    /**
     * Loyalty tier.
     *
     * Example:
     * Bronze
     * Silver
     * Gold
     * Platinum
     */
    private String membershipLevel;

    /**
     * GST Number.
     *
     * Useful when customers request business invoices.
     */
    private String gstNumber;
}