package com.foodies.freshmeal.pincode.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.foodies.freshmeal.common.exception.PincodeException;
import com.foodies.freshmeal.pincode.constants.PincodeErrorConstants;
import com.foodies.freshmeal.pincode.dto.PincodeApiResponse;

/**
 * =============================================================================
 * Client : PincodeApiClient
 * =============================================================================
 *
 * Purpose
 * -------
 * Responsible for communicating with the external pincode API.
 *
 * HTTP-level failures are handled at this layer because this class owns the
 * external HTTP communication.
 * =============================================================================
 */
@Component
public class PincodeApiClient {

    private final RestClient pincodeRestClient;

    public PincodeApiClient(RestClient pincodeRestClient) {
        this.pincodeRestClient = pincodeRestClient;
    }

    /**
     * Fetches pincode details from the external API.
     *
     * @param pincode Indian six-digit pincode
     * @return external API response
     */
    public PincodeApiResponse getPincodeDetails(String pincode) {

        return pincodeRestClient
                .get()
                .uri("/pincode/{pincode}", pincode)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        (request, response) -> {
                            throw new PincodeException(PincodeErrorConstants.PINCODE_API_FAILURE,
                                    response.getStatusCode().value());
                        })
                .body(PincodeApiResponse.class);
    }
}