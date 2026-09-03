package com.foodies.freshmeal.user.dto;

import java.util.List;

import com.foodies.freshmeal.common.constants.RoleType;
import com.foodies.freshmeal.common.valueObject.EmailAddress;
import com.foodies.freshmeal.common.valueObject.PhoneNumber;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =============================================================================
 * DTO : UserResponse
 * =============================================================================
 *
 * Represents the API response containing non-sensitive information about a
 * FreshMeal user.
 *
 * <p>
 * {@code UserResponse} provides the frontend or other API consumers with the
 * user information that is safe and relevant for normal user-related
 * operations.
 * </p>
 *
 * <p>
 * Sensitive authentication information and system-controlled security details
 * are intentionally excluded from this response.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Expose the FreshMeal business-facing user identifier.</li>
 * <li>Expose user identity and contact information.</li>
 * <li>Expose assigned business roles where appropriate.</li>
 * <li>Expose user-owned address references.</li>
 * <li>Provide a stable API representation independent of the persistence
 * entity.</li>
 * </ul>
 *
 * <h3>Security</h3>
 * <p>
 * Passwords, password hashes, authentication tokens, session identifiers,
 * authentication state, and internal persistence identifiers must not be
 * exposed through this DTO.
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
public class UserResponse {

    // =========================================================================
    // Business Identifier
    // =========================================================================

    /**
     * Business-facing identifier of the FreshMeal user.
     *
     * <p>
     * Example:
     * </p>
     *
     * <pre>
     * FM - USR - 0000001
     * </pre>
     */
    private String userNumber;

    // =========================================================================
    // User Information
    // =========================================================================

    /**
     * Username associated with the FreshMeal account.
     */
    private String username;

    /**
     * User's first name.
     */
    private String firstName;

    /**
     * User's last name.
     */
    private String lastName;

    // =========================================================================
    // Contact Information
    // =========================================================================

    /**
     * User's email address.
     */
    private EmailAddress email;

    /**
     * User's phone number.
     */
    private PhoneNumber phoneNumber;

    // =========================================================================
    // Address References
    // =========================================================================

    /**
     * Business identifiers of addresses owned by the user.
     */
    private List<String> addressNumbers;

    // =========================================================================
    // Authorization
    // =========================================================================

    /**
     * Business roles assigned to the user.
     *
     * <p>
     * Whether roles should be exposed through a particular API should be
     * decided by the authorization requirements of that endpoint.
     * </p>
     */
    private List<RoleType> roles;
}