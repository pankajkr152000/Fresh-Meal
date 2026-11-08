package com.foodies.freshmeal.authentication.dto;

import com.foodies.freshmeal.common.valueObject.EmailAddress;
import com.foodies.freshmeal.common.valueObject.PhoneNumber;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =============================================================================
 * DTO : RegistrationIdentityReservationInput
 * =============================================================================
 *
 * Purpose
 * -------
 * Carries the identity attributes and pending registration reference
 * required to reserve a user's registration identity.
 *
 * Responsibilities
 * -----------------
 * - Carries the pending registration number.
 * - Carries the requested username.
 * - Carries the requested email address.
 * - Carries the requested phone number.
 *
 * Business Rules
 * --------------
 * - The reservation must belong to an existing pending registration.
 * - Username, email, and phone number must be reserved together.
 * - Identity normalization and validation belong to the service layer.
 * - Ownership type is assigned internally and must never be supplied
 * through this DTO.
 *
 * Security
 * --------
 * This DTO intentionally excludes passwords, OTPs, ownership type,
 * and audit information.
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
public class RegistrationIdentityReservationInput {

    /**
     * Unique reference of the pending registration that will own
     * the identity reservation.
     */
    private String registrationNumber;

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
