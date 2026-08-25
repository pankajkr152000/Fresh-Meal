package com.foodies.freshmeal.pincode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =============================================================================
 * DTO : PincodePostOffice
 * =============================================================================
 *
 * Purpose
 * -------
 * Represents a single post-office record returned by the external pincode API.
 *
 * This is an external API DTO and should remain isolated from the Address
 * domain model.
 * =============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PincodePostOffice {

    private Long id;

    private String circle;

    @JsonProperty("circle_slug")
    private String circleSlug;

    private String region;

    @JsonProperty("region_slug")
    private String regionSlug;

    private String division;

    @JsonProperty("division_slug")
    private String divisionSlug;

    @JsonProperty("office_name")
    private String officeName;

    @JsonProperty("office_slug")
    private String officeSlug;

    private String pincode;

    @JsonProperty("office_type")
    private String officeType;

    @JsonProperty("delivery_status")
    private String deliveryStatus;

    private String district;

    @JsonProperty("district_slug")
    private String districtSlug;

    private String state;

    @JsonProperty("state_slug")
    private String stateSlug;

    private Double latitude;

    private Double longitude;

    private String digipin;

}