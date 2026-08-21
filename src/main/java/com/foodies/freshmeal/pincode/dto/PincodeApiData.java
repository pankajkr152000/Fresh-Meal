package com.foodies.freshmeal.pincode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =============================================================================
 * DTO : PincodeApiData
 * =============================================================================
 *
 * Purpose
 * -------
 * Represents a single pincode/post-office record returned by the external
 * pincode API.
 *
 * This is an external API DTO. It represents the provider's response structure
 * and should not be used directly by the Address domain.
 * =============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PincodeApiData {

    private Long id;

    private String officename;

    private String officetype;

    private String pincode;

    private String delivery;

    private String district;

    private String statename;

    private Double latitude;

    private Double longitude;

}