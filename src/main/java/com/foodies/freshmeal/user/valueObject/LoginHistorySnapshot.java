package com.foodies.freshmeal.user.valueObject;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodies.freshmeal.common.date.DateConstants;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * LoginHistorySnapshot
 * ============================================================================
 *
 * Represents a lightweight historical snapshot of the user's most relevant
 * login information.
 *
 * <p>
 * This object is intentionally different from LoginHistoryEntity.
 * LoginHistoryEntity represents the persistent authentication history record,
 * while this snapshot represents login information retained as part of
 * another user-related object such as UserProfile.
 * </p>
 *
 * <p>
 * The snapshot must remain independent from the LoginHistoryEntity lifecycle.
 * Changes to login history records must not mutate historical profile data
 * already captured in this object.
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
     * Business identifier of the login history record.
     *
     * Example:
     *
     * FM-LGH-0000001
     */
    private String loginHistoryNumber;

    // =========================================================================
    // Login Information
    // =========================================================================

    /**
     * IP address from which the login occurred.
     */
    @Size(max = 100)
    private String ipAddress;

    /**
     * Login status.
     *
     * Examples:
     *
     * SUCCESS
     * FAILED
     * LOCKED
     */
    @Size(max = 50)
    private String loginStatus;

    /**
     * Indicates whether the login attempt was successful.
     */
    private Boolean loginSuccess;

    /**
     * Time at which the login occurred.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime loginTime;

    /**
     * Time at which the session ended.
     *
     * Null when the session is still active.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime logoutTime;

    // =========================================================================
    // Session
    // =========================================================================

    /**
     * Authentication session identifier.
     */
    @Size(max = 200)
    private String sessionId;

    /**
     * Application server that processed the login request.
     */
    @Size(max = 200)
    private String loginServerName;
}
