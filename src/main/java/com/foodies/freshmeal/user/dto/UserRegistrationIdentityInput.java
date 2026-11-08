package com.foodies.freshmeal.user.dto;

import com.foodies.freshmeal.common.valueObject.EmailAddress;
import com.foodies.freshmeal.common.valueObject.PhoneNumber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =============================================================================
 * DTO : UserRegistrationIdentityInput
 * =============================================================================
 *
 * Purpose
 * -------
 * Carries the identity attributes required to validate a public registration
 * request against existing user records.
 *
 * Responsibilities
 * ----------------
 * - Carries the requested username.
 * - Carries the requested email address.
 * - Carries the requested phone number.
 *
 * Business Rules
 * --------------
 * Identity validation is performed by the User module.
 * This DTO does not perform persistence or business validation itself.
 *
 * =============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationIdentityInput {

    /**
     * Requested authentication username.
     */
    private String username;

    /**
     * Requested email address.
     */
    private EmailAddress email;

    /**
     * Requested phone number.
     */
    private PhoneNumber phoneNumber;
}