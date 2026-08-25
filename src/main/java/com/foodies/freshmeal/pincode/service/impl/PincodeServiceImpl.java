package com.foodies.freshmeal.pincode.service.impl;

import org.springframework.stereotype.Service;

import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceOutput;
import com.foodies.freshmeal.pincode.client.PincodeApiClient;
import com.foodies.freshmeal.pincode.dto.PincodeApiData;
import com.foodies.freshmeal.pincode.dto.PincodeApiResponse;
import com.foodies.freshmeal.pincode.dto.PincodeLookupRequest;
import com.foodies.freshmeal.pincode.dto.PincodePostOffice;
import com.foodies.freshmeal.pincode.service.IPincodeService;
import com.foodies.freshmeal.pincode.valueObject.PincodeDetails;

/**
 * =============================================================================
 * Pincode Service Implementation
 * =============================================================================
 *
 * Responsibilities
 * ----------------
 * • Validate the supplied pincode.
 * • Call the external pincode API through PincodeApiClient.
 * • Validate the external API response.
 * • Extract the required location information.
 * • Convert the external response into FreshMeal's PincodeDetails.
 *
 * The service does not expose external API DTOs to the controller or other
 * application modules.
 * =============================================================================
 */
@Service
public class PincodeServiceImpl implements IPincodeService {

    private final PincodeApiClient pincodeApiClient;

    public PincodeServiceImpl(PincodeApiClient pincodeApiClient) {
        this.pincodeApiClient = pincodeApiClient;
    }

    @Override
    public IServiceOutput<PincodeDetails> getPincodeDetails(
            IServiceInput<PincodeLookupRequest> request) {

        PincodeLookupRequest pincodeRequest = request.getInput();

        validatePincode(pincodeRequest);

        PincodeApiResponse response = pincodeApiClient.getPincodeDetails(
                pincodeRequest.pincode());

        if (response == null
                || !Boolean.TRUE.equals(response.getSuccess())
                || response.getData() == null
                || response.getData().getPostOffices() == null
                || response.getData().getPostOffices().isEmpty()) {

            throw new IllegalStateException(
                    "No details found for pincode : "
                            + pincodeRequest.pincode());

        }

        PincodeApiData pincodeData = response.getData();

        PincodePostOffice postOffice = pincodeData.getPostOffices().get(0);

        PincodeDetails output = new PincodeDetails(
                pincodeData.getPincode(),
                "India",
                postOffice.getState(),
                postOffice.getDistrict());

        return new ServiceOutput<>(output);
    }

    /**
     * Validates the supplied pincode.
     *
     * @param request pincode lookup request
     */
    private void validatePincode(PincodeLookupRequest request) {

        if (request == null || request.pincode() == null) {
            throw new IllegalArgumentException(
                    "Pincode must not be null");
        }

        if (request.pincode().isBlank()) {
            throw new IllegalArgumentException(
                    "Pincode must not be empty");
        }

        if (!request.pincode().matches("\\d{6}")) {
            throw new IllegalArgumentException(
                    "Pincode must contain exactly 6 digits");
        }
    }
}