package com.foodies.freshmeal.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =============================================================================
 * DTO : UserInputDTO
 * =============================================================================
 *
 * Represents the service-layer input for user-related operations.
 *
 * <p>
 * {@code UserInputDTO} acts as the boundary between the common
 * {@code IServiceInput}/{@code IServiceOutput} architecture and the
 * user-specific request model.
 * </p>
 *
 * <p>
 * The DTO keeps client-facing {@link UserRequest} data separate from the
 * service context required by the FreshMeal service infrastructure.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Carry user request data into the User service layer.</li>
 * <li>Provide access to service execution context when required.</li>
 * <li>Keep transport-level input separate from the persistence entity.</li>
 * </ul>
 *
 * <h3>Design</h3>
 * <p>
 * System-controlled information such as user number, roles, account status,
 * audit fields, and authentication state must not be accepted through this
 * DTO. Those values are managed by the appropriate service/security layers.
 * </p>
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
public class UserInputDTO {

    // =========================================================================
    // User Request
    // =========================================================================

    /**
     * Client-supplied user information.
     */
    private UserRequest userRequest;

}