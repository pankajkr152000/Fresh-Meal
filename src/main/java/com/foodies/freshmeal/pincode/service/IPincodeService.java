package com.foodies.freshmeal.pincode.service;

import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.pincode.dto.PincodeLookupRequest;
import com.foodies.freshmeal.pincode.valueObject.PincodeDetails;

/**
 * =============================================================================
 * Pincode Service
 * =============================================================================
 *
 * Responsibilities
 * ----------------
 * • Define pincode-related business operations.
 * • Accept requests through the common IServiceInput abstraction.
 * • Return results through the common IServiceOutput abstraction.
 *
 * The implementation is responsible for communicating with the external
 * pincode API and transforming its response into FreshMeal's internal model.
 * =============================================================================
 */
public interface IPincodeService {

    /**
     * Retrieves location details for the supplied pincode.
     *
     * @param input pincode lookup service input
     * @return pincode location details
     */
    IServiceOutput<PincodeDetails> getPincodeDetails(IServiceInput<PincodeLookupRequest> input);

}