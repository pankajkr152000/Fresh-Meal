package com.foodies.freshmeal.authentication.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodies.freshmeal.common.builder.ApiResponseBuilder;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.common.exception.CommonErrorConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles authentication failures for protected resources.
 *
 * <p>
 * Returns a standardized HTTP 401 response using the application's
 * centralized API response structure.
 * </p>
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException authenticationException)
            throws IOException, ServletException {

        final ResponseEntity<ApiResponse<Object>> apiResponse = ApiResponseBuilder
                .buildResponse(CommonErrorConstants.UNAUTHORIZED);

        response.setStatus(apiResponse.getStatusCode().value());
        response.setHeader(
                HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getOutputStream(), apiResponse.getBody());
    }
}
