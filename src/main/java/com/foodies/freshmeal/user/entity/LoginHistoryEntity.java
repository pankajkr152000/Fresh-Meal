package com.foodies.freshmeal.user.entity;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Entity : LoginHistoryEntity
 * ============================================================================
 *
 * Represents a historical authentication/login session of a FreshMeal user.
 *
 * <p>
 * Login history is an audit-oriented domain entity. It records when and from
 * where a user attempted to authenticate and the resulting login status.
 * </p>
 *
 * <p>
 * The entity extends {@link ABaseEntity} so that it participates in the common
 * FreshMeal persistence lifecycle and audit infrastructure.
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
     * Creates a new LoginHistoryEntity.
     *
     * @return new LoginHistoryEntity.
     */
    public static IEntity create() {
        return new LoginHistoryEntity();
    }

    // =========================================================================
    // Business Identifier
    // =========================================================================

    /**
     * Business-facing login history identifier.
     *
     * <p>
     * Example:
     *
     * <pre>
     * FM - LGH - 0000001
     * </pre>
     * </p>
     */
    @Indexed(unique = true)
    private String loginHistoryNumber;

    // =========================================================================
    // User
    // =========================================================================

    /**
     * Business identifier of the user associated with this login attempt.
     *
     * <p>
     * The user number is preferred over storing the internal MongoDB id as
     * part of the business relationship.
     * </p>
     */
    @Indexed
    private String userNumber;

    // =========================================================================
    // Authentication
    // =========================================================================

    /**
     * Login status.
     *
     * <p>
     * Example values:
     * </p>
     *
     * <ul>
     * <li>SUCCESS</li>
     * <li>FAILED</li>
     * <li>LOCKED</li>
     * </ul>
     */
    private String loginStatus;

    /**
     * Indicates whether authentication was successful.
     */
    private boolean loginSuccess;

    /**
     * Time at which the login attempt occurred.
     */
    private LocalDateTime loginTime;

    /**
     * Time at which the authenticated session ended.
     *
     * <p>
     * Null when the session is still active or logout information is not
     * available.
     * </p>
     */
    private LocalDateTime logoutTime;

    // =========================================================================
    // Session
    // =========================================================================

    /**
     * Authentication session identifier.
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
     * Application server that processed the login request.
     */
    private String loginServerName;
}