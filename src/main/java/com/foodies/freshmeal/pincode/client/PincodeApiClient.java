package com.foodies.freshmeal.pincode.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.foodies.freshmeal.pincode.dto.PincodeApiResponse;

import lombok.RequiredArgsConstructor;

/**
 * =============================================================================
 * Client : PincodeApiClient
 * =============================================================================
 *
 * Purpose
 * -------
 * Responsible only for communicating with the external pincode API.
 *
 * This class does not contain business logic and does not transform the
 * external response into FreshMeal's internal model.
 *
 * That responsibility belongs to PincodeService.
 * =============================================================================
 */
@Component
@RequiredArgsConstructor
public class PincodeApiClient {

    private final RestClient pincodeRestClient;

    /**
     * Fetches pincode information from the external API.
     *
     * @param pincode Indian six-digit pincode
     * @return response received from the external pincode API
     */
    public PincodeApiResponse getPincodeDetails(String pincode) {

        return pincodeRestClient
                .get()
                .uri("/pincode/{pincode}", pincode)
                .retrieve()
                .body(PincodeApiResponse.class);
    }
}