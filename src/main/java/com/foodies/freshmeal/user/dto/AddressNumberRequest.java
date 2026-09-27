package com.foodies.freshmeal.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : AddressNumberRequest
 * ============================================================================
 *
 * Purpose
 * -------
 * Carries the business-facing address number used to identify a saved
 * customer address.
 *
 * <p>
 * The address number is generated and managed by FreshMeal and is different
 * from the technical entity/document identifier stored in the database.
 * </p>
 *
 * <p>
 * Example:
 * {@code FM-ADR-0000001}
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class AddressNumberRequest {

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
}