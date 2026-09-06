package com.foodies.freshmeal.authentication.entity;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.authentication.token.TokenType;
import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;

/**
 * ============================================================================
 * Entity : RevokedTokenEntity
 * ============================================================================
 *
 * <p>
 * Represents a JWT that has been explicitly revoked before its natural
 * expiration time.
 * </p>
 *
 * <p>
 * FreshMeal uses stateless JWT authentication. Therefore, issuing a logout
 * operation cannot physically remove an already-issued JWT from a client.
 * This entity provides the server-side revocation state required to reject
 * such a token before its normal expiration.
 * </p>
 *
 * <h3>Business Meaning</h3>
 *
 * <ul>
 * <li>{@code tokenId} identifies the individual JWT through its JTI.</li>
 * <li>{@code sessionId} identifies the FreshMeal authentication session.</li>
 * <li>{@code userNumber} identifies the business user.</li>
 * <li>{@code tokenType} distinguishes access and refresh tokens.</li>
 * <li>{@code expiresAt} determines when the revocation record is no longer
 * needed for token validation.</li>
 * <li>{@code revokedAt} records when the token became unusable.</li>
 * </ul>
 *
 * <h3>Security</h3>
 *
 * <p>
 * The actual JWT is never stored. Only its cryptographically generated
 * {@code jti} and non-secret metadata are persisted.
 * </p>
 *
 * <h3>Uses</h3>
 *
 * <ul>
 * <li>Explicit logout.</li>
 * <li>Refresh-token revocation.</li>
 * <li>Session termination.</li>
 * <li>Future logout-all-devices functionality.</li>
 * </ul>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Document(collection = "fm_revoked_tokens")
public class RevokedTokenEntity extends ABaseEntity {

    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Token Identity
    // =========================================================================

    /**
     * Unique JWT identifier ({@code jti}) of the revoked token.
     */
    @Indexed(unique = true)
    private String tokenId;

    /**
     * FreshMeal authentication-session identifier.
     */
    @Indexed
    private String sessionId;

    /**
     * FreshMeal business user number.
     */
    @Indexed
    private String userNumber;

    /**
     * Token purpose.
     */
    @Indexed
    private TokenType tokenType;

    // =========================================================================
    // Revocation Lifecycle
    // =========================================================================

    /**
     * Time at which the token was originally due to expire.
     *
     * <p>
     * This value allows expired revocation records to be cleaned up later.
     * </p>
     */
    @Indexed
    private LocalDateTime expiresAt;

    /**
     * Time at which the token was explicitly revoked.
     */
    @Indexed
    private LocalDateTime revokedAt;

    // =========================================================================
    // Constructor / Factory
    // =========================================================================

    /**
     * Creates a revoked-token entity.
     *
     * <p>
     * The constructor is intentionally package-private. Domain entities are
     * created through the FreshMeal entity factory.
     * </p>
     */
    RevokedTokenEntity() {
        // EntityFactory
    }

    /**
     * Creates a new revoked-token entity through the common entity factory.
     *
     * @return newly created revoked-token entity
     */
    public static IEntity create() {
        return new RevokedTokenEntity();
    }

    // =========================================================================
    // Getters / Setters
    // =========================================================================

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(LocalDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }
}