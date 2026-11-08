package com.foodies.freshmeal.user.dto;

import com.foodies.freshmeal.common.valueObject.EmailAddress;
import com.foodies.freshmeal.common.valueObject.PhoneNumber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Input required to create a permanent user whose email
 * has already been successfully verified.
 *
 * <p>
 * The requested onboarding role is intentionally excluded.
 * Newly registered users receive only the USER role.
 * </p>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerifiedUserCreationInput {

    private String username;
    private String firstName;
    private String lastName;
    private EmailAddress email;
    private PhoneNumber phoneNumber;
    private String encodedPassword;

}
