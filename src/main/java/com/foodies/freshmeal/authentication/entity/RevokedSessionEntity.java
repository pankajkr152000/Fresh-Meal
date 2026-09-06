package com.foodies.freshmeal.authentication.entity;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Entity : RevokedSessionEntity
 * ============================================================================
 *
 * Represents a FreshMeal authentication session that has been revoked.
 *
 * <p>
 * Access and refresh tokens generated during the same login operation share
 * one authentication-session identifier. Revoking the session therefore
 * invalidates the complete authentication session rather than only one JWT.
 * </p>
 *
 * <h3>Security Boundary</h3>
 * <p>
 * This entity does not store access tokens, refresh tokens, passwords, or any
 * other authentication secret. Only the session identifier and required
 * revocation metadata are persisted.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@Document(collection = "fm_revoked_sessions")
public class RevokedSessionEntity extends ABaseEntity {

    /**
     * Unique FreshMeal authentication-session identifier.
     */
    @Indexed(unique = true)
    @Field("sessionId")
    private String sessionId;

    /**
     * FreshMeal user number associated with the revoked session.
     */
    @Indexed
    @Field("userNumber")
    private String userNumber;

    /**
     * Time at which the session was revoked.
     */
    @Indexed
    @Field("revokedAt")
    private LocalDateTime revokedAt;

    // =========================================================================
    // Constructor / Factory
    // =========================================================================

    /**
     * Creates a revoked-session entity.
     *
     * <p>
     * The constructor is intentionally package-private. Domain entities are
     * created through the FreshMeal entity factory.
     * </p>
     */
    RevokedSessionEntity() {
        // EntityFactory
    }

    /**
     * Creates a new revoked-session entity through the common entity factory.
     *
     * @return newly created revoked-session entity
     */
    public static IEntity create() {
        return new RevokedSessionEntity();
    }
}