package com.foodies.freshmeal.pincode.dto;

import java.util.List;

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
 * This DTO belongs to the external API integration layer. It should not be
 * exposed directly to the frontend or used by the Address domain.
 *
 * The PincodeService will later transform this external response into our
 * internal PincodeDetails model.
 * =============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PincodeApiResponse {

    private String status;

    private List<PincodeApiData> data;

}