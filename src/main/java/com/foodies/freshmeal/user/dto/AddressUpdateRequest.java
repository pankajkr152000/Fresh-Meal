package com.foodies.freshmeal.user.dto;

import com.foodies.freshmeal.common.valueObject.PhoneNumber;
import com.foodies.freshmeal.user.constants.AddressTypeConstant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : AddressUpdateRequest
 * ============================================================================
 *
 * Purpose
 * -------
 * Represents the address information supplied by the client when updating
 * an existing customer address.
 *
 * <p>
 * The business-facing {@code addressNumber} identifies the address to be
 * updated. The authenticated user's identity is resolved by the service layer
 * and must not be supplied by the client.
 * </p>
 *
 * <p>
 * Location fields such as district, state and country are intentionally not
 * accepted from the client. They are resolved by PincodeService using the
 * supplied postal code.
 * </p>
 *
 * <p>
 * The default-address state is intentionally not part of this request.
 * Changing the default address is handled through a dedicated business
 * operation so that the service can maintain the invariant that a user has
 * at most one default address.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressUpdateRequest {

    /**
     * Business-facing address identifier.
     *
     * <p>
     * Example:
     * {@code FM-ADR-0000001}
     * </p>
     */
    @NotBlank
    @Size(max = 50)
    private String addressNumber;

    /**
     * User-defined address label.
     *
     * <p>
     * Examples:
     * HOME, WORK, OTHER
     * </p>
     */
    @NotNull
    private AddressTypeConstant addressType;

    /**
     * Recipient / contact person name.
     */
    @NotBlank
    @Size(max = 100)
    private String recipientName;

    /**
     * Contact phone number for delivery.
     */
    @NotNull
    private PhoneNumber phoneNumber;

    /**
     * Flat, apartment, house or building number.
     */
    @NotBlank
    @Size(max = 150)
    private String addressLine1;

    /**
     * Street, road, locality or area.
     */
    @Size(max = 150)
    private String addressLine2;

    /**
     * Landmark near the address.
     */
    @Size(max = 200)
    private String landmark;

    /**
     * City entered by the user.
     */
    @NotBlank
    @Size(max = 100)
    private String city;

    /**
     * Postal / PIN code.
     *
     * <p>
     * The pincode is used by PincodeService to resolve:
     * country, state and district.
     * </p>
     */
    @NotBlank
    @Pattern(regexp = "^[0-9]{6}$", message = "Invalid pincode.")
    private String postalCode;
}