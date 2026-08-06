package com.foodies.freshmeal.common.exception;

import com.foodies.freshmeal.common.constants.HttpStatusCode;

/**
 * ============================================================================
 * Common Error Constants
 * ============================================================================
 *
 * Centralized common business errors shared across all modules.
 *
 * Used by:
 *
 * • Validation • Authentication • Authorization • Database • File Upload •
 * Configuration • System
 *
 * ============================================================================
 *
 * Error Code Convention
 *
 * FM-COM-XXXXXX
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum CommonErrorConstants implements IBusinessError {

    // =====================================================================
    // Validation
    // =====================================================================

    VALIDATION_FAILED("FM-COM-001", "Validation failed.", HttpStatusCode.BAD_REQUEST),

    INVALID_REQUEST("FM-COM-002", "Invalid request.", HttpStatusCode.BAD_REQUEST),

    REQUEST_BODY_MISSING("FM-COM-003", "Request body is required.", HttpStatusCode.BAD_REQUEST),

    INVALID_REQUEST_PARAMETER("FM-COM-004", "Invalid request parameter.", HttpStatusCode.BAD_REQUEST),

    INVALID_REQUEST_HEADER("FM-COM-005", "Invalid request header.", HttpStatusCode.BAD_REQUEST),

    // =====================================================================
    // Authentication
    // =====================================================================

    UNAUTHORIZED("FM-COM-100", "Authentication required.", HttpStatusCode.UNAUTHORIZED),

    INVALID_CREDENTIALS("FM-COM-101", "Invalid credentials.", HttpStatusCode.UNAUTHORIZED),

    TOKEN_EXPIRED("FM-COM-102", "Authentication token has expired.", HttpStatusCode.UNAUTHORIZED),

    TOKEN_INVALID("FM-COM-103", "Invalid authentication token.", HttpStatusCode.UNAUTHORIZED),

    // =====================================================================
    // Authorization
    // =====================================================================

    ACCESS_DENIED("FM-COM-200", "Access denied.", HttpStatusCode.FORBIDDEN),

    INSUFFICIENT_PERMISSION("FM-COM-201", "Insufficient permission.", HttpStatusCode.FORBIDDEN),

    // =====================================================================
    // Resource
    // =====================================================================

    RESOURCE_NOT_FOUND("FM-COM-300", "Requested resource not found.", HttpStatusCode.NOT_FOUND),

    DUPLICATE_RESOURCE("FM-COM-301", "Resource already exists.", HttpStatusCode.CONFLICT),

    RESOURCE_ALREADY_DELETED("FM-COM-302", "Resource has already been deleted.", HttpStatusCode.GONE),

    // =====================================================================
    // System
    // =====================================================================

    INTERNAL_SERVER_ERROR("FM-COM-500", "An unexpected error occurred.", HttpStatusCode.INTERNAL_SERVER_ERROR),

    OPERATION_FAILED("FM-COM-501", "Operation failed.", HttpStatusCode.INTERNAL_SERVER_ERROR),

    SERVICE_UNAVAILABLE("FM-COM-502", "Service temporarily unavailable.", HttpStatusCode.SERVICE_UNAVAILABLE),

    // =====================================================================
    // Database
    // =====================================================================

    DATABASE_ERROR("FM-COM-600", "Database operation failed.", HttpStatusCode.INTERNAL_SERVER_ERROR),

    DATABASE_CONNECTION_FAILED("FM-COM-601", "Unable to connect to database.", HttpStatusCode.INTERNAL_SERVER_ERROR),

    CONCURRENT_MODIFICATION("FM-COM-602", "Resource was modified by another user.", HttpStatusCode.CONFLICT),

    // =====================================================================
    // File
    // =====================================================================

    FILE_NOT_FOUND("FM-COM-700", "File not found.", HttpStatusCode.NOT_FOUND),

    FILE_UPLOAD_FAILED("FM-COM-701", "File upload failed.", HttpStatusCode.INTERNAL_SERVER_ERROR),

    FILE_DELETE_FAILED("FM-COM-702", "File deletion failed.", HttpStatusCode.INTERNAL_SERVER_ERROR),

    INVALID_FILE("FM-COM-703", "Invalid file.", HttpStatusCode.BAD_REQUEST),

    FILE_SIZE_EXCEEDED("FM-COM-704", "File size exceeded.", HttpStatusCode.BAD_REQUEST),

    // =====================================================================
    // Configuration
    // =====================================================================

    CONFIGURATION_ERROR("FM-COM-800", "Application configuration error.", HttpStatusCode.INTERNAL_SERVER_ERROR),

    FEATURE_NOT_SUPPORTED("FM-COM-801", "Feature not supported.", HttpStatusCode.NOT_IMPLEMENTED);

    /**
     * Error code.
     */
    private final String errorCode;

    /**
     * Error message.
     */
    private final String errorMessage;

    /**
     * HTTP status.
     */
    private final HttpStatusCode httpStatusCode;

    CommonErrorConstants(final String errorCode, final String errorMessage, final HttpStatusCode httpStatusCode) {

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatusCode = httpStatusCode;
    }

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