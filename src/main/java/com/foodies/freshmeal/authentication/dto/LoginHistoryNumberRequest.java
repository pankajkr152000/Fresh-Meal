package com.foodies.freshmeal.authentication.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : LoginHistoryNumberRequest
 * ============================================================================
 *
 * <p>
 * Carries the business-facing login-history identifier used to identify a
 * specific authentication history record.
 * </p>
 *
 * <p>
 * The login-history number is a business identifier and is intentionally
 * separated from the internal MongoDB document identifier.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class LoginHistoryNumberRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Business-facing login-history identifier.
     */
    @NotBlank(message = "Login history number is required.")
    @Size(max = 100, message = "Login history number must not exceed 100 characters.")
    private String loginHistoryNumber;
}
