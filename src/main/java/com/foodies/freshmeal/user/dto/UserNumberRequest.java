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
 * DTO : UserNumberRequest
 * =============================================================================
 *
 * Represents a request containing the business-facing identifier of a
 * FreshMeal user.
 *
 * <p>
 * {@code UserNumberRequest} is used when a user-related operation needs to
 * identify a user through the FreshMeal business identifier rather than the
 * internal MongoDB document identifier.
 * </p>
 *
 * <h3>Business Identifier</h3>
 * <p>
 * The {@code userNumber} is the stable business-facing identifier of a
 * FreshMeal user. It may be used by APIs, services, reports, logs, and
 * inter-domain relationships.
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
 * <li>Support user lookup and business operations.</li>
 * <li>Keep business identification separate from MongoDB persistence
 * identification.</li>
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
public class UserNumberRequest {

    // =========================================================================
    // User Number
    // =========================================================================

    /**
     * Business-facing identifier of the FreshMeal user.
     */
    @NotBlank
    @Size(max = 100)
    private String userNumber;
}