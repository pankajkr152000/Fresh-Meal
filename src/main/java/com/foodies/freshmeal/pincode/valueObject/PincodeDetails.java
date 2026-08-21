package com.foodies.freshmeal.pincode.valueObject;

/**
 * =============================================================================
 * Value Object : PincodeDetails
 * =============================================================================
 *
 * Purpose
 * -------
 * Represents the location information that FreshMeal actually needs from a
 * pincode lookup.
 *
 * This is our application's internal representation. It is intentionally
 * independent of the external pincode API response structure.
 *
 * The Address module can use this object without knowing anything about the
 * external API.
 * =============================================================================
 */
public record PincodeDetails(

        String pincode,

        String country,

        String state,

        String district

) {
}