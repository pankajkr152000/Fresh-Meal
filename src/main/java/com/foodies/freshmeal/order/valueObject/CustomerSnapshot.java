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
 * Historical snapshot of customer information at the time the order was
 * created.
 *
 * This is NOT the Customer master record.
 *
 * ============================================================================
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
     * Customer email.
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
     */
    private String profileImageUrl;

    /**
     * Indicates whether the customer was a guest.
     */
    @Builder.Default
    private Boolean guestCustomer = Boolean.FALSE;

    /**
     * Loyalty membership identifier.
     *
     * Future-ready.
     */
    private String membershipId;

    /**
     * Loyalty membership level.
     *
     * Future-ready.
     */
    private String membershipLevel;

    /**
     * GST number for business customers.
     *
     * Future-ready.
     */
    private String gstNumber;

}