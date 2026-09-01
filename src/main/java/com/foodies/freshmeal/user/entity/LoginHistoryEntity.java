package com.foodies.freshmeal.user.entity;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.user.constants.LoginStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Entity : LoginHistoryEntity
 * ============================================================================
 *
 * Represents an authentication attempt and login session history record for
 * a FreshMeal user.
 *
 * <p>
 * {@code LoginHistoryEntity} is an audit-oriented persistence entity. It
 * records authentication events separately from {@link UserEntity}, keeping
 * the user document focused on the user's current state rather than its
 * historical authentication activity.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Record authentication attempts.</li>
 * <li>Identify the FreshMeal user involved in the attempt.</li>
 * <li>Record authentication outcome.</li>
 * <li>Record login and logout timestamps.</li>
 * <li>Record session information.</li>
 * <li>Record request/network information useful for auditing.</li>
 * </ul>
 *
 * <h3>User Relationship</h3>
 * <p>
 * The relationship with {@link UserEntity} is maintained using the user's
 * business identifier {@code userNumber} rather than the internal MongoDB
 * identifier. This keeps the audit record independent from MongoDB-specific
 * document relationships.
 * </p>
 *
 * <h3>Security</h3>
 * <p>
 * This entity must never contain passwords, password hashes, JWTs, refresh
 * tokens, API keys, or other authentication secrets.
 * </p>
 *
 * <h3>Audit Lifecycle</h3>
 * <p>
 * A record is created when an authentication attempt occurs. For successful
 * authentication, {@code logoutTime} may subsequently be populated when the
 * associated session ends. Failed authentication attempts remain as audit
 * records even though no authenticated session was established.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@Document(collection = "fm_login_history")
public class LoginHistoryEntity extends ABaseEntity {

    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Package-private constructor.
     *
     * <p>
     * Entity creation should happen through the factory method.
     * </p>
     */
    LoginHistoryEntity() {
        // Package-private constructor.
    }

    // =========================================================================
    // Factory
    // =========================================================================

    /**
     * Creates a new {@link LoginHistoryEntity}.
     *
     * @return new login history entity.
     */
    public static IEntity create() {
        return new LoginHistoryEntity();
    }

    // =========================================================================
    // Business Identifier
    // =========================================================================

    /**
     * Business-facing identifier of the login-history record.
     *
     * <p>
     * Example:
     * </p>
     *
     * <pre>
     * FM - LGH - 0000001
     * </pre>
     */
    @Indexed(unique = true)
    private String loginHistoryNumber;

    // =========================================================================
    // User Reference
    // =========================================================================

    /**
     * Business identifier of the user associated with this authentication
     * attempt.
     */
    @Indexed
    private String userNumber;

    // =========================================================================
    // Authentication Result
    // =========================================================================

    /**
     * Outcome of the authentication attempt.
     */
    @Indexed
    private LoginStatus loginStatus;

    // =========================================================================
    // Authentication Time
    // =========================================================================

    /**
     * Time at which the authentication attempt occurred.
     */
    @Indexed
    private LocalDateTime loginTime;

    /**
     * Time at which the authenticated session ended.
     *
     * <p>
     * This value remains {@code null} when authentication failed, the session
     * is still active, or logout information is unavailable.
     * </p>
     */
    private LocalDateTime logoutTime;

    // =========================================================================
    // Session Information
    // =========================================================================

    /**
     * Identifier of the authentication session associated with this record.
     *
     * <p>
     * The value should identify the session only and must never contain an
     * access token, refresh token, or other authentication secret.
     * </p>
     */
    @Indexed
    private String sessionId;

    // =========================================================================
    // Request / Network Information
    // =========================================================================

    /**
     * IP address from which the authentication request originated.
     */
    private String ipAddress;

    /**
     * User-agent information supplied by the client.
     *
     * <p>
     * Useful for authentication auditing and security analysis.
     * </p>
     */
    private String userAgent;

    /**
     * Application/server instance that processed the authentication request.
     *
     * <p>
     * This is particularly useful when FreshMeal is deployed across multiple
     * application instances.
     * </p>
     */
    private String loginServerName;
}