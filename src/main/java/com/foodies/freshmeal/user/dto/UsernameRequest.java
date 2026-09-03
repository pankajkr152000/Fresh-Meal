package com.foodies.freshmeal.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =============================================================================
 * Request : UsernameRequest
 * =============================================================================
 *
 * Purpose
 * -------
 * Represents a request containing the username used to identify a user.
 * =============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsernameRequest {

    @NotBlank
    @Size(max = 100)
    private String username;
}