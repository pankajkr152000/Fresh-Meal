package com.foodies.freshmeal.common.dto;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

	private boolean success;

	private int httpStatusCode;

	private String httpStatusMessage;

	private String message;

	private List<String> errors;

	private T data;

	public static <T> ResponseEntity<ApiResponse<T>> error(int statusCode, String message) {

		HttpStatus status = HttpStatus.valueOf(statusCode);

		return buildResponse(false, status, message, List.of(message), null);
	}

	public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {

		return buildResponse(true, HttpStatus.OK, message, null, data);
	}

	public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message, List<String> errors) {

		return buildResponse(false, HttpStatus.BAD_REQUEST, message, errors, null);
	}

	public static <T> ResponseEntity<ApiResponse<T>> internalServerError(String message, List<String> errors) {

		return buildResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, message, errors, null);
	}

	private static <T> ResponseEntity<ApiResponse<T>> buildResponse(boolean success, HttpStatus status, String message,
			List<String> errors, T data) {

		ApiResponse<T> response = new ApiResponse<>();
		response.setSuccess(success);
		response.setHttpStatusCode(status.value());
		response.setHttpStatusMessage(status.getReasonPhrase());
		response.setMessage(message);
		response.setErrors(errors);
		response.setData(data);

		return ResponseEntity.status(status).body(response);
	}
}
