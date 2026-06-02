package com.foodies.freshmeal.common.dto;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ApiResponses {

        private ApiResponses() {

        }

        public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {

                return ResponseEntity.ok(new ApiResponse<>(true, message, null, data));
        }

        public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {

                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, message, null, data));
        }

        public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message, List<String> errors) {

                return ResponseEntity.badRequest().body(new ApiResponse<>(false, message, errors, null));
        }

        public static <T> ResponseEntity<ApiResponse<T>> notFound(String message, List<String> errors) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse<>(false, message, errors, null));
        }

        public static <T> ResponseEntity<ApiResponse<T>> internalServerError(String message, List<String> errors) {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ApiResponse<>(false, message, errors, null));
        }
}

