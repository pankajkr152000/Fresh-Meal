package com.foodies.freshmeal.pincode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =============================================================================
 * DTO : PincodeApiResponse
 * =============================================================================
 *
 * Purpose
 * -------
 * Represents the top-level response received from the external pincode API.
 *
 * The external API returns the actual pincode information inside the "data"
 * object. Therefore, data is represented by PincodeApiData rather than a List.
 * =============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PincodeApiResponse {

    private Boolean success;

    private PincodeApiData data;

    private PincodeApiMeta meta;

}