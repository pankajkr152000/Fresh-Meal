package com.foodies.freshmeal.authentication.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.foodies.freshmeal.user.constants.LoginStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : LoginHistoryInputDTO
 * ============================================================================
 *
 * <p>
 * Carries authentication-session information required by the Authentication
 * module to create a
 * {@link com.foodies.freshmeal.user.entity.LoginHistoryEntity}
 * record.
 * </p>
 *
 * <p>
 * This DTO contains only authentication audit information. Passwords, password
 * hashes, access tokens, refresh tokens, API keys, and other authentication
 * secrets must never be included.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Carry the FreshMeal user business identifier.</li>
 * <li>Carry the authentication outcome.</li>
 * <li>Carry the login timestamp.</li>
 * <li>Carry the authentication session identifier.</li>
 * <li>Carry client network information.</li>
 * <li>Carry client user-agent information.</li>
 * <li>Carry the application/server instance information.</li>
 * </ul>
 *
 * <h3>Business Meaning</h3>
 * <p>
 * This DTO represents the audit information surrounding a single
 * authentication attempt. It is intentionally independent from
 * {@code UserEntity} so authentication history remains a separate concern.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class LoginHistoryInputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Business identifier of the FreshMeal user.
     */
    private String userNumber;

    /**
     * Authentication outcome.
     */
    private LoginStatus loginStatus;

    /**
     * Time at which the authentication attempt occurred.
     */
    private LocalDateTime loginTime;

    /**
     * Identifier of the authentication session.
     *
     * <p>
     * This must identify the session only and must never contain an access
     * token, refresh token, or other authentication secret.
     * </p>
     */
    private String sessionId;

    /**
     * IP address from which the authentication request originated.
     */
    private String ipAddress;

    /**
     * User-agent supplied by the client.
     */
    private String userAgent;

    /**
     * Application/server instance that processed the authentication request.
     */
    private String loginServerName;
}
