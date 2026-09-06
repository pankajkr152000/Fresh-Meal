package com.foodies.freshmeal.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * =========================================================================
 * Update Password Input DTO
 * =========================================================================
 *
 * <p>
 * Internal service input used by the User module when updating a user's
 * password.
 * </p>
 *
 * <p>
 * The password is expected to already be encoded by the Authentication
 * module before this DTO reaches the User module.
 * </p>
 *
 * <p>
 * The raw password must never be placed in this DTO.
 * </p>
 *
 * <h3>Business Meaning</h3>
 * <ul>
 * <li><b>userNumber</b> identifies the user whose password is updated.</li>
 * <li><b>encodedPassword</b> contains the already encoded new password.</li>
 * </ul>
 *
 * =========================================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordInputDTO {

    /**
     * Unique business identifier of the user.
     */
    private String userNumber;

    /**
     * Encoded representation of the user's new password.
     *
     * <p>
     * Raw passwords must never be persisted or passed into the User module
     * through this field.
     * </p>
     */
    private String encodedPassword;
}