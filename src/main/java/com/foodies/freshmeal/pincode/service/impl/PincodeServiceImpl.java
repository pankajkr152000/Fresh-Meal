package com.foodies.freshmeal.pincode.service.impl;

import org.springframework.stereotype.Service;

import com.foodies.freshmeal.pincode.client.PincodeApiClient;
import com.foodies.freshmeal.pincode.dto.PincodeApiData;
import com.foodies.freshmeal.pincode.dto.PincodeApiResponse;
import com.foodies.freshmeal.pincode.service.IPincodeService;
import com.foodies.freshmeal.pincode.valueObject.PincodeDetails;

import lombok.RequiredArgsConstructor;

/**
 * =============================================================================
 * Service Implementation : PincodeServiceImpl
 * =============================================================================
 *
 * Purpose
 * -------
 * Handles pincode-related business logic.
 *
 * Responsibilities
 * ----------------
 * 1. Validate the supplied pincode.
 * 2. Call the external pincode API through PincodeApiClient.
 * 3. Validate the external API response.
 * 4. Convert the external API response into FreshMeal's internal
 * PincodeDetails value object.
 *
 * This class intentionally does not expose the external API response to the
 * rest of the application.
 * =============================================================================
 */
@Service
@RequiredArgsConstructor
public class PincodeServiceImpl implements IPincodeService {

    private final PincodeApiClient pincodeApiClient;

    @Override
    public PincodeDetails getPincodeDetails(String pincode) {

        validatePincode(pincode);

        PincodeApiResponse response = pincodeApiClient.getPincodeDetails(pincode);

        if (response == null
                || response.getData() == null
                || response.getData().isEmpty()) {

            throw new IllegalArgumentException(
                    "No details found for pincode : " + pincode);
        }

        PincodeApiData pincodeData = response.getData().get(0);

        return new PincodeDetails(
                pincodeData.getPincode(),
                "India",
                pincodeData.getStatename(),
                pincodeData.getDistrict());
    }

    /**
     * Validates the basic Indian pincode format.
     *
     * @param pincode pincode supplied by the client
     */
    private void validatePincode(String pincode) {

        if (pincode == null || pincode.isBlank()) {
            throw new IllegalArgumentException(
                    "Pincode must not be empty");
        }

        if (!pincode.matches("\\d{6}")) {
            throw new IllegalArgumentException(
                    "Pincode must contain exactly 6 digits");
        }
    }
}