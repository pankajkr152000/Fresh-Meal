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
 * DTO : UserIdRequest
 * =============================================================================
 *
 * Represents a request containing the business-facing identifier of a
 * FreshMeal user.
 *
 * <p>
 * {@code UserIdRequest} is used by user-related service operations that need
 * to identify an existing user without exposing or depending on the internal
 * MongoDB document identifier.
 * </p>
 *
 * <h3>Business Identifier</h3>
 * <p>
 * The {@code userNumber} represents the external/business identifier of the
 * user and is the preferred identifier for FreshMeal business operations.
 * </p>
 *
 * <p>
 * Example:
 * </p>
 *
 * <pre>
 * FM - USR - 0000001
 * </pre>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Carry a user business identifier into the service layer.</li>
 * <li>Support user lookup and user-specific operations.</li>
 * <li>Keep the internal MongoDB identifier outside the API contract.</li>
 * </ul>
 *
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
public class UserIdRequest {

    // =========================================================================
    // User Identifier
    // =========================================================================

    /**
     * Business-facing identifier of the FreshMeal user.
     */
    @NotBlank
    @Size(max = 100)
    private String userId;
}