package com.foodies.freshmeal.user.dto;

import com.foodies.freshmeal.common.constants.RoleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =============================================================================
 * DTO : UserRegistrationInputDTO
 * =============================================================================
 *
 * Represents the service-layer input used specifically for public FreshMeal
 * user registration.
 *
 * <p>
 * This DTO extends the normal user registration information with the role
 * requested by the registrant. It is intentionally separate from
 * {@link UserInputDTO} because role selection is specific to the registration
 * workflow and should not become part of generic user operations.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Carry user identity and contact information into the User service.</li>
 * <li>Carry the role requested during public registration.</li>
 * <li>Provide a clear service-layer boundary for registration-specific
 * operations.</li>
 * <li>Keep generic user operations independent from registration-specific
 * concerns.</li>
 * </ul>
 *
 * <h3>Role Security</h3>
 * <p>
 * The requested role is treated as registration intent only. The User service
 * must not assume that every supplied role is permitted for public
 * registration. The Authentication module validates the public registration
 * policy before invoking this service.
 * </p>
 *
 * <p>
 * The {@link RoleType#ADMIN} role must never be granted through public
 * self-registration. Administrative role assignment belongs to an authorized
 * administrative workflow.
 * </p>
 *
 * <h3>Registration Behaviour</h3>
 * <p>
 * The User service uses this input to determine whether to create a new
 * {@code UserEntity} or add the requested role to an existing user identity.
 * A role must never be added more than once.
 * </p>
 *
 * <pre>
 * UserRegistrationInputDTO
 *         |
 *         +--UserRequest
 *         | +--username
 *         | +--firstName
 *         | +--lastName
 *         | +--email
 *         | +--phoneNumber
 *         |
 *         +--requestedRole
 * </pre>
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
public class UserRegistrationInputDTO {

    // =========================================================================
    // User Registration Information
    // =========================================================================

    /**
     * User identity and contact information supplied during registration.
     *
     * <p>
     * Authentication credentials such as the raw password are intentionally
     * excluded. Password validation and encoding are owned by the
     * Authentication module.
     * </p>
     */
    private UserRequest userRequest;

    // =========================================================================
    // Requested Role
    // =========================================================================

    /**
     * Role requested by the user during public registration.
     *
     * <p>
     * This represents the user's registration intent. The actual persisted
     * roles remain owned by {@code UserEntity} and are managed by the User
     * service.
     * </p>
     */
    private RoleType requestedRole;
}