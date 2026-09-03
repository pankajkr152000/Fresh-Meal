package com.foodies.freshmeal.user.dto;

import com.foodies.freshmeal.common.valueObject.EmailAddress;
import com.foodies.freshmeal.common.valueObject.PhoneNumber;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =============================================================================
 * DTO : UserRequest
 * =============================================================================
 *
 * Represents client-supplied user information used for creating or updating
 * a FreshMeal user.
 *
 * <p>
 * This DTO contains only information that can legitimately originate from the
 * client. System-controlled fields such as {@code userNumber}, roles, account
 * status, and audit information are intentionally excluded.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Capture user-entered identity information.</li>
 * <li>Capture contact information.</li>
 * <li>Provide validation boundaries for user input.</li>
 * <li>Prevent system-controlled fields from being accepted as normal
 * user input.</li>
 * </ul>
 *
 * <h3>Authentication</h3>
 * <p>
 * Authentication-specific credentials and operations should be handled by
 * dedicated authentication DTOs. This keeps user management separate from
 * login, password reset, verification, and token operations.
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
public class UserRequest {

    // =========================================================================
    // Identity
    // =========================================================================

    /**
     * Username used to identify the user during authentication.
     */
    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    /**
     * User's first name.
     */
    @NotBlank
    @Size(max = 100)
    private String firstName;

    /**
     * User's last name.
     */
    @Size(max = 100)
    private String lastName;

    // =========================================================================
    // Contact Information
    // =========================================================================

    /**
     * User's email address.
     *
     * <p>
     * Validation is delegated to {@link EmailAddress}.
     * </p>
     */
    @Valid
    private EmailAddress email;

    /**
     * User's mobile phone number.
     *
     * <p>
     * Validation is delegated to {@link PhoneNumber}.
     * </p>
     */
    @Valid
    private PhoneNumber phoneNumber;
}