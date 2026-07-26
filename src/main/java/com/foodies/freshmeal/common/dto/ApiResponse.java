package com.foodies.freshmeal.common.dto;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.foodies.freshmeal.common.constants.HttpStatusCode;
import com.foodies.freshmeal.common.constants.ResponseMessageConstants;

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
	 * Validation / business errors.
	 */
	private List<String> errors;

	/**
	 * Response payload.
	 */
	private T data;

	// ===========================================================
	// Success Responses
	// ===========================================================

	public static <T> ResponseEntity<ApiResponse<T>> success() {
		return success(ResponseMessageConstants.SUCCESS, null);
	}

	public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
		return success(ResponseMessageConstants.SUCCESS, data);
	}

	public static <T> ResponseEntity<ApiResponse<T>> success(String message) {
		return success(message, null);
	}

	public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {

		return buildResponse(true, HttpStatus.OK, message, null, data);
	}

	public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {

		return buildResponse(true, HttpStatus.CREATED, message, null, data);
	}

	public static <T> ResponseEntity<ApiResponse<T>> accepted(String message) {

		return buildResponse(true, HttpStatus.ACCEPTED, message, null, null);
	}

	public static ResponseEntity<Void> noContent() {
		return ResponseEntity.noContent().build();
	}

	// ===========================================================
	// Client Error Responses
	// ===========================================================

	public static <T> ResponseEntity<ApiResponse<T>> badRequest(String errorCode, String message) {

		return badRequest(errorCode, message, List.of(message));
	}

	public static <T> ResponseEntity<ApiResponse<T>> badRequest(String errorCode, String message, List<String> errors) {

		return buildResponse(false, HttpStatus.BAD_REQUEST, message, errors, null);
	}

	public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String message) {

		return buildResponse(false, HttpStatus.UNAUTHORIZED, message, List.of(message), null);
	}

	public static <T> ResponseEntity<ApiResponse<T>> forbidden(String message) {

		return buildResponse(false, HttpStatus.FORBIDDEN, message, List.of(message), null);
	}

	public static <T> ResponseEntity<ApiResponse<T>> notFound(String message) {

		return buildResponse(false, HttpStatus.NOT_FOUND, message, List.of(message), null);
	}

	public static <T> ResponseEntity<ApiResponse<T>> conflict(String message) {

		return buildResponse(false, HttpStatus.CONFLICT, message, List.of(message), null);
	}

	public static <T> ResponseEntity<ApiResponse<T>> methodNotAllowed(String message) {

		return buildResponse(false, HttpStatus.METHOD_NOT_ALLOWED, message, List.of(message), null);
	}

	@SuppressWarnings("deprecation")
	public static <T> ResponseEntity<ApiResponse<T>> payloadTooLarge(String message) {

		return buildResponse(false, HttpStatus.PAYLOAD_TOO_LARGE, message, List.of(message), null);
	}

	// ===========================================================
	// Server Error Responses
	// ===========================================================

	public static <T> ResponseEntity<ApiResponse<T>> internalServerError(String message) {

		return internalServerError(message, List.of(message));
	}

	public static <T> ResponseEntity<ApiResponse<T>> internalServerError(String message, List<String> errors) {

		return buildResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, message, errors, null);
	}

	// ===========================================================
	// Common Response Builder
	// ===========================================================

	/**
	 * Builds a standard API response.
	 *
	 * @param success indicates whether request succeeded
	 * @param status  HTTP status
	 * @param message business message
	 * @param errors  validation/business errors
	 * @param data    response payload
	 *
	 * @return ResponseEntity<ApiResponse<T>>
	 */
	private static <T> ResponseEntity<ApiResponse<T>> buildResponse(boolean success, HttpStatus status, String message,
			List<String> errors, T data) {

		ApiResponse<T> response = new ApiResponse<>();

		response.setSuccess(success);
		response.setHttpStatusCode(status.value());

		response.setHttpStatusMessage(HttpStatusCode.getDescription(status.value()));

		response.setMessage(message);
		response.setErrors(errors);
		response.setData(data);

		return ResponseEntity.status(status).body(response);
	}

	public static <T> ResponseEntity<ApiResponse<T>> badRequest() {

		return badRequest("400", ResponseMessageConstants.BAD_REQUEST);
	}

	public static <T> ResponseEntity<ApiResponse<T>> unauthorized() {

		return unauthorized(ResponseMessageConstants.UNAUTHORIZED);
	}

	public static <T> ResponseEntity<ApiResponse<T>> forbidden() {

		return forbidden(ResponseMessageConstants.FORBIDDEN);
	}

	public static <T> ResponseEntity<ApiResponse<T>> conflict() {

		return conflict(ResponseMessageConstants.CONFLICT);
	}

	public static <T> ResponseEntity<ApiResponse<T>> methodNotAllowed() {

		return methodNotAllowed(ResponseMessageConstants.METHOD_NOT_ALLOWED);
	}

	public static <T> ResponseEntity<ApiResponse<T>> payloadTooLarge() {

		return payloadTooLarge(ResponseMessageConstants.PAYLOAD_TOO_LARGE);
	}

	public static <T> ResponseEntity<ApiResponse<T>> internalServerError() {

		return internalServerError(ResponseMessageConstants.INTERNAL_SERVER_ERROR);
	}

	public static <T> ResponseEntity<ApiResponse<T>> accepted() {

		return accepted(ResponseMessageConstants.ACCEPTED);
	}

	public static <T> ResponseEntity<ApiResponse<T>> created(T data) {

		return created(ResponseMessageConstants.CREATED, data);
	}
}