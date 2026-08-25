package com.foodies.freshmeal.pincode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =============================================================================
 * DTO : PincodeApiMeta
 * =============================================================================
 *
 * Purpose
 * -------
 * Represents the metadata returned by the external pincode API.
 *
 * This DTO belongs to the external API integration layer and mirrors the
 * provider's response structure.
 * =============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PincodeApiMeta {

    @JsonProperty("api_version")
    private String apiVersion;

    @JsonProperty("dataset_version")
    private String datasetVersion;

    @JsonProperty("release_id")
    private String releaseId;

    @JsonProperty("request_id")
    private String requestId;

    private Integer count;

}