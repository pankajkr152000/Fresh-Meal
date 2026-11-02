package com.foodies.freshmeal.authentication.security;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodies.freshmeal.common.builder.ApiResponseBuilder;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.common.exception.CommonErrorConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles authorization failures for authenticated users.
 *
 * <p>
 * Returns a standardized HTTP 403 response when an authenticated user
 * attempts to access a resource without sufficient permissions.
 * </p>
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public JwtAccessDeniedHandler(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        final ResponseEntity<ApiResponse<Object>> apiResponse = ApiResponseBuilder
                .buildResponse(CommonErrorConstants.ACCESS_DENIED);

        response.setStatus(apiResponse.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getOutputStream(), apiResponse.getBody());
    }
}