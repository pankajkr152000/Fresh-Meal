package com.foodies.freshmeal.pincode.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodies.freshmeal.pincode.service.IPincodeService;

import lombok.RequiredArgsConstructor;

/**
 * =============================================================================
 * Controller : PincodeController
 * =============================================================================
 *
 * Purpose
 * -------
 * Exposes pincode lookup functionality to the client.
 *
 * The controller is responsible only for:
 * 1. Receiving the request.
 * 2. Delegating the operation to PincodeService.
 * 3. Returning the result.
 *
 * The controller does not communicate with the external pincode API directly.
 * =============================================================================
 */
@RestController
@RequestMapping("/api/pincodes")
@RequiredArgsConstructor
public class PincodeController {

    private final IPincodeService pincodeService;

    /**
     * Looks up location details for the supplied pincode.
     *
     * Example request:
     *
     * POST /api/pincodes/lookup
     *
     * {
     * "pincode": "700001"
     * }
     */

}