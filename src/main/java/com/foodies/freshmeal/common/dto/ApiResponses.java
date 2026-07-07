package com.foodies.freshmeal.common.dto;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.foodies.freshmeal.common.constants.HttpStatusCode;

/**
 * Utility class for building standardized API responses.
 */
public final class ApiResponses {

        private ApiResponses() {

        }

        /**
         * Generic response builder.
         *
         * @param success response status
         * @param status  HTTP status
         * @param message response message
         * @param errors  validation/business errors
         * @param data    response payload
         * @return standardized API response
         */
        private static <T> ResponseEntity<ApiResponse<T>> buildResponse(
                        boolean success,
                        HttpStatus status,
                        String message,
                        List<String> errors,
                        T data) {

                return ResponseEntity.status(status)
                                .body(new ApiResponse<>(
                                                success,
                                                status.value(),
                                                HttpStatusCode.getDescription(status.value()),
                                                message,
                                                errors,
                                                data));
        }

        // ================= SUCCESS RESPONSES =================

        public static <T> ResponseEntity<ApiResponse<T>> ok(
                        String message,
                        T data) {

                return buildResponse(
                                true,
                                HttpStatus.OK,
                                message,
                                null,
                                data);
        }

        public static <T> ResponseEntity<ApiResponse<T>> created(
                        String message,
                        T data) {

                return buildResponse(
                                true,
                                HttpStatus.CREATED,
                                message,
                                null,
                                data);
        }

        public static <T> ResponseEntity<ApiResponse<T>> accepted(
                        String message,
                        T data) {

                return buildResponse(
                                true,
                                HttpStatus.ACCEPTED,
                                message,
                                null,
                                data);
        }

        // ================= CLIENT ERROR RESPONSES =================

        public static <T> ResponseEntity<ApiResponse<T>> badRequest(
                        String message,
                        List<String> errors) {

                return buildResponse(
                                false,
                                HttpStatus.BAD_REQUEST,
                                message,
                                errors,
                                null);
        }

        public static <T> ResponseEntity<ApiResponse<T>> unauthorized(
                        String message,
                        List<String> errors) {

                return buildResponse(
                                false,
                                HttpStatus.UNAUTHORIZED,
                                message,
                                errors,
                                null);
        }

        public static <T> ResponseEntity<ApiResponse<T>> forbidden(
                        String message,
                        List<String> errors) {

                return buildResponse(
                                false,
                                HttpStatus.FORBIDDEN,
                                message,
                                errors,
                                null);
        }

        public static <T> ResponseEntity<ApiResponse<T>> notFound(
                        String message,
                        List<String> errors) {

                return buildResponse(
                                false,
                                HttpStatus.NOT_FOUND,
                                message,
                                errors,
                                null);
        }

        public static <T> ResponseEntity<ApiResponse<T>> conflict(
                        String message,
                        List<String> errors) {

                return buildResponse(
                                false,
                                HttpStatus.CONFLICT,
                                message,
                                errors,
                                null);
        }

        // ================= SERVER ERROR RESPONSES =================

        public static <T> ResponseEntity<ApiResponse<T>> internalServerError(
                        String message,
                        List<String> errors) {

                return buildResponse(
                                false,
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                message,
                                errors,
                                null);
        }

        public static <T> ResponseEntity<ApiResponse<T>> success(String message) {

            return buildResponse(true, HttpStatus.OK, message, null, null);
        }

        public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {

            return buildResponse(true, HttpStatus.OK, message, null, data);
        }
}
