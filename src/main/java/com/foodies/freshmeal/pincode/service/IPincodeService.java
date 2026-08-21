package com.foodies.freshmeal.pincode.service;

import com.foodies.freshmeal.pincode.valueObject.PincodeDetails;

/**
 * =============================================================================
 * Service : PincodeService
 * =============================================================================
 *
 * Purpose
 * -------
 * Provides pincode-related business operations for FreshMeal.
 *
 * The service exposes our application's internal PincodeDetails model and
 * remains completely independent of the external pincode API response format.
 * =============================================================================
 */
public interface IPincodeService {

    /**
     * Retrieves location details for the supplied pincode.
     *
     * @param pincode six-digit Indian pincode
     * @return resolved pincode details
     */
    PincodeDetails getPincodeDetails(String pincode);

}