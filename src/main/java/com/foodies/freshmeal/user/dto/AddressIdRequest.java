package com.foodies.freshmeal.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * =============================================================================
 * DTO : AddressIdRequest
 * =============================================================================
 *
 * Purpose
 * -------
 * Carries the business-facing address identifier used to load an address.
 *
 * The identifier is generated and managed by FreshMeal and is different from
 * MongoDB's internal document identifier.
 * =============================================================================
 */
@Getter
@Setter
public class AddressIdRequest {

    /**
     * Business-facing address identifier.
     *
     * Example:
     * FM-ADR-0000001
     */
    @NotBlank
    @Size(max = 50)
    private String addressId;
}