package com.foodies.freshmeal.common.exception;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.foodies.freshmeal.common.dto.ApiResponse;

public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidEnum(
            HttpMessageNotReadableException exception) {

        return ApiResponse.badRequest(
                "400",
                List.of("Invalid food status."));

    }
}
