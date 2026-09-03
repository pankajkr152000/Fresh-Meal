package com.foodies.freshmeal.user.dto;

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
 * DTO : UpdateUserInputDTO
 * =============================================================================
 *
 * Purpose
 * -------
 * Represents the input required to update an existing user.
 *
 * <p>
 * The user is identified using the FreshMeal business-facing
 * {@code userNumber}. Editable user information is supplied separately through
 * {@link UserRequest}.
 * </p>
 *
 * <h3>Business Meaning</h3>
 * <p>
 * This DTO intentionally separates the identity of the user being updated from
 * the profile information being changed.
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
public class UpdateUserInputDTO {

    /**
     * Business-facing identifier of the user being updated.
     */
    @NotBlank
    @Size(max = 100)
    private String userNumber;

    /**
     * Editable user information.
     */
    @Valid
    private UserRequest userRequest;
}