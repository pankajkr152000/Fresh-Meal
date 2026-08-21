package com.foodies.freshmeal.pincode.dto;

/**
 * =============================================================================
 * DTO : PincodeLookupRequest
 * =============================================================================
 *
 * Purpose
 * -------
 * Represents the request received by FreshMeal when a client wants to look up
 * the details of a pincode.
 *
 * Example
 * -------
 * {
 * "pincode": "700001"
 * }
 * =============================================================================
 */
public record PincodeLookupRequest(

        String pincode

) {
}