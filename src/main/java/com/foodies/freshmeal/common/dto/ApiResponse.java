package com.foodies.freshmeal.common.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =========================================================== Generic API
 * Response ===========================================================
 *
 * Standard response wrapper used across the application.
 *
 * Every REST endpoint should return this object to ensure consistent API
 * responses.
 *
 * @param <T> Response payload type
 *
 * @author Pankaj Kumar
 * @version 2.0 ===========================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    /**
     * Indicates whether request was successful.
     */
    private boolean success;

    /**
     * HTTP status code.
     */
    private int httpStatusCode;

    /**
     * HTTP status description.
     */
    private String httpStatusMessage;

    /**
     * Business response message.
     */
    private String message;

    /**
     * Application specific error code.
     *
     * Example:
     * FM-FOOD-001
     * FM-ORD-002
     */
    private String errorCode;
    /**
     * Validation / business errors.
     */
    private List<String> errors;

    /**
     * Response payload.
     */
    private T data;
}