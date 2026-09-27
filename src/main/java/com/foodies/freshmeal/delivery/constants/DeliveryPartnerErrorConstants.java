package com.foodies.freshmeal.delivery.constants;

import com.foodies.freshmeal.common.constants.HttpStatusCode;
import com.foodies.freshmeal.common.exception.IBusinessError;

/**
 * ============================================================================
 * Delivery Partner Error Constants
 * ============================================================================
 *
 * <p>
 * Centralized business errors for the Delivery Partner module.
 * </p>
 *
 * <p>
 * These errors represent delivery-partner-specific business and workflow
 * failures. Authentication and generic authorization errors remain owned by
 * their respective modules.
 * </p>
 *
 * <p>
 * Error code convention:
 * </p>
 *
 * <pre>
 * FM - DLP - XXXX
 * </pre>
 *
 * <ul>
 * <li><b>FM</b> - FreshMeal</li>
 * <li><b>DLP</b> - Delivery Partner</li>
 * <li><b>XXXX</b> - Unique delivery-partner error number</li>
 * </ul>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum DeliveryPartnerErrorConstants implements IBusinessError {

    // =========================================================================
    // General Delivery Partner Errors - 001 to 009
    // =========================================================================

    DELIVERY_PARTNER_OPERATION_NOT_ALLOWED(
            "FM-DLP-001",
            "The requested delivery partner operation is not allowed.",
            HttpStatusCode.BAD_REQUEST),

    DELIVERY_PARTNER_NOT_FOUND(
            "FM-DLP-002",
            "Delivery partner was not found.",
            HttpStatusCode.NOT_FOUND),

    DELIVERY_PARTNER_ALREADY_EXISTS(
            "FM-DLP-003",
            "A delivery partner profile already exists for the user.",
            HttpStatusCode.CONFLICT),

    DELIVERY_PARTNER_ID_REQUIRED(
            "FM-DLP-004",
            "Delivery partner identifier is required.",
            HttpStatusCode.BAD_REQUEST),

    DELIVERY_PARTNER_USER_REQUIRED(
            "FM-DLP-005",
            "A valid user is required for the delivery partner operation.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Onboarding Errors - 010 to 019
    // =========================================================================

    DELIVERY_PARTNER_ONBOARDING_NOT_ALLOWED(
            "FM-DLP-010",
            "The user is not allowed to onboard as a delivery partner.",
            HttpStatusCode.BAD_REQUEST),

    DELIVERY_PARTNER_ROLE_REQUIRED(
            "FM-DLP-011",
            "The DELIVERY_PARTNER role is required for delivery partner onboarding.",
            HttpStatusCode.BAD_REQUEST),

    DELIVERY_PARTNER_ALREADY_ONBOARDED(
            "FM-DLP-012",
            "The user has already completed delivery partner onboarding.",
            HttpStatusCode.CONFLICT),

    DELIVERY_PARTNER_REGISTRATION_REQUIRED(
            "FM-DLP-013",
            "Delivery partner registration information is required.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Vehicle Errors - 020 to 029
    // =========================================================================

    VEHICLE_NUMBER_REQUIRED(
            "FM-DLP-020",
            "Vehicle registration number is required.",
            HttpStatusCode.BAD_REQUEST),

    VEHICLE_TYPE_REQUIRED(
            "FM-DLP-021",
            "Vehicle type is required.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_VEHICLE_TYPE(
            "FM-DLP-022",
            "The specified vehicle type is invalid.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Verification Errors - 030 to 039
    // =========================================================================

    VERIFICATION_STATUS_REQUIRED(
            "FM-DLP-030",
            "Delivery partner verification status is required.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_VERIFICATION_STATUS(
            "FM-DLP-031",
            "The requested delivery partner verification status is invalid.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_VERIFICATION_TRANSITION(
            "FM-DLP-032",
            "The requested delivery partner verification transition is not allowed.",
            HttpStatusCode.BAD_REQUEST),

    DELIVERY_PARTNER_ALREADY_VERIFIED(
            "FM-DLP-033",
            "The delivery partner is already verified.",
            HttpStatusCode.BAD_REQUEST),

    DELIVERY_PARTNER_ALREADY_REJECTED(
            "FM-DLP-034",
            "The delivery partner is already rejected.",
            HttpStatusCode.BAD_REQUEST),

    DELIVERY_PARTNER_VERIFICATION_REQUIRED(
            "FM-DLP-035",
            "The delivery partner must be verified before this operation is allowed.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Business Status Errors - 040 to 049
    // =========================================================================

    STATUS_REQUIRED(
            "FM-DLP-040",
            "Delivery partner status is required.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_STATUS_TRANSITION(
            "FM-DLP-041",
            "The requested delivery partner status transition is not allowed.",
            HttpStatusCode.BAD_REQUEST),

    DELIVERY_PARTNER_ALREADY_ACTIVE(
            "FM-DLP-042",
            "The delivery partner is already active.",
            HttpStatusCode.BAD_REQUEST),

    DELIVERY_PARTNER_ALREADY_INACTIVE(
            "FM-DLP-043",
            "The delivery partner is already inactive.",
            HttpStatusCode.BAD_REQUEST),

    DELIVERY_PARTNER_ALREADY_SUSPENDED(
            "FM-DLP-044",
            "The delivery partner is already suspended.",
            HttpStatusCode.BAD_REQUEST),

    DELIVERY_PARTNER_NOT_ACTIVE(
            "FM-DLP-045",
            "The delivery partner must be active for this operation.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Availability Errors - 050 to 059
    // =========================================================================

    AVAILABILITY_REQUIRED(
            "FM-DLP-050",
            "Delivery partner availability is required.",
            HttpStatusCode.BAD_REQUEST),

    DELIVERY_PARTNER_NOT_VERIFIED_FOR_AVAILABILITY(
            "FM-DLP-051",
            "A delivery partner must be verified before availability can be enabled.",
            HttpStatusCode.BAD_REQUEST),

    DELIVERY_PARTNER_NOT_ACTIVE_FOR_AVAILABILITY(
            "FM-DLP-052",
            "A delivery partner must be active before availability can be enabled.",
            HttpStatusCode.BAD_REQUEST),

    DELIVERY_PARTNER_ALREADY_AVAILABLE(
            "FM-DLP-053",
            "The delivery partner is already available.",
            HttpStatusCode.BAD_REQUEST),

    DELIVERY_PARTNER_ALREADY_UNAVAILABLE(
            "FM-DLP-054",
            "The delivery partner is already unavailable.",
            HttpStatusCode.BAD_REQUEST);

    // =========================================================================
    // Fields
    // =========================================================================

    private final String errorCode;

    private final String errorMessage;

    private final HttpStatusCode httpStatusCode;

    // =========================================================================
    // Constructor
    // =========================================================================

    DeliveryPartnerErrorConstants(
            final String errorCode,
            final String errorMessage,
            final HttpStatusCode httpStatusCode) {

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatusCode = httpStatusCode;
    }

    // =========================================================================
    // IBusinessError
    // =========================================================================

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }
}