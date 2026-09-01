package com.foodies.freshmeal.user.valueObject;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodies.freshmeal.common.date.DateConstants;
import com.foodies.freshmeal.user.constants.LoginStatus;
import com.foodies.freshmeal.user.entity.LoginHistoryEntity;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * Value Object : LoginHistorySnapshot
 * ============================================================================
 *
 * Represents a lightweight historical snapshot of the most relevant login
 * information associated with a FreshMeal user.
 *
 * <p>
 * {@code LoginHistorySnapshot} is intentionally different from
 * {@link LoginHistoryEntity}. The entity represents the complete persistent
 * authentication audit record, whereas this value object represents only the
 * relevant login information that may be retained by another user-related
 * object.
 * </p>
 *
 * <p>
 * The snapshot is an independent representation of login information.
 * Changes made to the corresponding {@link LoginHistoryEntity} after the
 * snapshot is created must not alter the historical meaning of this object.
 * </p>
 *
 * <h3>Purpose</h3>
 * <ul>
 * <li>Provide lightweight login information to user-related objects.</li>
 * <li>Avoid embedding the complete login-history entity.</li>
 * <li>Preserve historical login information independently.</li>
 * <li>Provide a safe representation for API-facing user information when
 * required.</li>
 * </ul>
 *
 * <h3>Security</h3>
 * <p>
 * This snapshot must never contain passwords, password hashes, access tokens,
 * refresh tokens, OTPs, verification tokens, or other authentication secrets.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginHistorySnapshot implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Login History Identification
    // =========================================================================

    /**
     * Business identifier of the login history record from which this snapshot
     * was created.
     *
     * <p>
     * Example:
     * </p>
     *
     * <pre>
     * FM - LGH - 0000001
     * </pre>
     */
    private String loginHistoryNumber;

    // =========================================================================
    // Login Information
    // =========================================================================

    /**
     * IP address from which the authentication request originated.
     */
    @Size(max = 100)
    private String ipAddress;

    /**
     * Outcome of the authentication attempt.
     */
    private LoginStatus loginStatus;

    /**
     * Time at which the authentication attempt occurred.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime loginTime;

    /**
     * Time at which the authenticated session ended.
     *
     * <p>
     * {@code null} indicates that the session is still active or that logout
     * information is unavailable.
     * </p>
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime logoutTime;

    // =========================================================================
    // Session
    // =========================================================================

    /**
     * Authentication session identifier.
     *
     * <p>
     * This value must identify the session only and must never contain an
     * access token, refresh token, or other authentication secret.
     * </p>
     */
    @Size(max = 200)
    private String sessionId;

    /**
     * Application server that processed the authentication request.
     */
    @Size(max = 200)
    private String loginServerName;
}