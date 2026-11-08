package com.foodies.freshmeal.authentication.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents the result of a successfully verified registration.
 *
 * <p>
 * Contains the minimum user information required by the
 * authentication layer for post-registration processing.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public class VerifiedRegistrationResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String userNumber;
    private final String email;
    private final String firstName;
}