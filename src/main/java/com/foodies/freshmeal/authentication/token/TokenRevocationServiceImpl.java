package com.foodies.freshmeal.authentication.token;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.foodies.freshmeal.authentication.entity.RevokedSessionEntity;
import com.foodies.freshmeal.authentication.entity.RevokedTokenEntity;
import com.foodies.freshmeal.authentication.repository.IRevokedSessionRepository;
import com.foodies.freshmeal.authentication.repository.IRevokedTokenRepository;
import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.io.service.IServiceContext;

import lombok.RequiredArgsConstructor;

/**
 * ============================================================================
 * Service : TokenRevocationServiceImpl
 * ============================================================================
 *
 * <p>
 * Provides server-side revocation of FreshMeal JWT access and refresh tokens
 * and complete authentication sessions.
 * </p>
 *
 * <h3>Token Revocation</h3>
 *
 * <p>
 * Individual JWTs are identified using their {@code jti}. A revoked token is
 * represented by {@link RevokedTokenEntity}; the actual JWT value is never
 * persisted.
 * </p>
 *
 * <h3>Session Revocation</h3>
 *
 * <p>
 * Access and refresh tokens generated during the same login operation share
 * one authentication-session identifier. A revoked session is represented by
 * {@link RevokedSessionEntity}.
 * </p>
 *
 * <h3>Security Boundary</h3>
 *
 * <p>
 * Token claims are extracted through {@link ITokenService}, which performs
 * cryptographic JWT verification before returning security-sensitive claims.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class TokenRevocationServiceImpl
        implements ITokenRevocationService {

    // =========================================================================
    // Dependencies
    // =========================================================================

    /**
     * Repository for individual revoked JWT records.
     */
    private final IRevokedTokenRepository revokedTokenRepository;

    /**
     * Repository for revoked authentication sessions.
     */
    private final IRevokedSessionRepository revokedSessionRepository;

    /**
     * FreshMeal JWT token service.
     */
    private final ITokenService tokenService;

    // =========================================================================
    // Token Revocation
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public void revokeToken(
            String token,
            IServiceContext serviceContext) {

        if (!StringUtils.hasText(token)) {
            return;
        }

        /*
         * getTokenId() cryptographically verifies the JWT before returning
         * its jti claim.
         */
        final String tokenId = tokenService.getTokenId(token);

        if (!StringUtils.hasText(tokenId)) {
            return;
        }

        if (isTokenRevoked(token)) {
            return;
        }

        final String sessionId = tokenService.getSessionId(token);
        final String userNumber = tokenService.getUserNumber(token);
        final String tokenTypeValue = tokenService.getTokenType(token);
        final LocalDateTime expiresAt = tokenService.getExpiration(token);
        final LocalDateTime revokedAt = LocalDateTime.now();

        TokenType tokenType = parseTokenType(tokenTypeValue);

        RevokedTokenEntity entity = (RevokedTokenEntity) RevokedTokenEntity.create();

        entity.setTokenId(tokenId);
        entity.setSessionId(sessionId);
        entity.setUserNumber(userNumber);
        entity.setTokenType(tokenType);
        entity.setExpiresAt(expiresAt);
        entity.setRevokedAt(revokedAt);

        populateAuditFields(entity, serviceContext, revokedAt);

        revokedTokenRepository.save(entity);
    }

    // =========================================================================
    // Token Revocation Check
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTokenRevoked(String token) {

        if (!StringUtils.hasText(token)) {
            return false;
        }

        final String tokenId = tokenService.getTokenId(token);

        if (!StringUtils.hasText(tokenId)) {
            return false;
        }

        Query query = Query.query(
                Criteria.where("tokenId").is(tokenId));

        return revokedTokenRepository.exists(query);
    }

    // =========================================================================
    // Session Revocation
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public void revokeSession(
            String sessionId,
            String userNumber,
            IServiceContext serviceContext) {

        if (!StringUtils.hasText(sessionId)) {
            return;
        }

        if (isSessionRevoked(sessionId)) {
            return;
        }

        final LocalDateTime revokedAt = LocalDateTime.now();

        RevokedSessionEntity entity = (RevokedSessionEntity) RevokedSessionEntity.create();

        entity.setSessionId(sessionId);
        entity.setUserNumber(userNumber);
        entity.setRevokedAt(revokedAt);

        populateAuditFields(entity, serviceContext, revokedAt);

        revokedSessionRepository.save(entity);
    }

    // =========================================================================
    // Session Revocation Check
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSessionRevoked(String sessionId) {

        if (!StringUtils.hasText(sessionId)) {
            return false;
        }

        Query query = Query.query(
                Criteria.where("sessionId").is(sessionId));

        return revokedSessionRepository.exists(query);
    }

    // =========================================================================
    // Cleanup
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanupExpiredRevocations() {

        final LocalDateTime now = LocalDateTime.now();

        Query tokenQuery = Query.query(
                Criteria.where("expiresAt").lte(now));

        revokedTokenRepository.findAll(tokenQuery)
                .forEach(entity -> revokedTokenRepository
                        .deletePermanently(entity.getId()));

        /*
         * Session revocation records do not currently contain an explicit
         * session-expiration field. They are therefore intentionally not
         * removed by this cleanup operation.
         *
         * Session cleanup can be introduced later when the authentication
         * session lifecycle has an explicit retention/expiration policy.
         */
    }

    // =========================================================================
    // Token Type
    // =========================================================================

    /**
     * Converts the token-type claim into the FreshMeal token-type enum.
     *
     * @param tokenTypeValue token-type claim.
     *
     * @return FreshMeal token type.
     *
     * @throws IllegalArgumentException when the claim does not represent a
     *                                  supported token type.
     */
    private TokenType parseTokenType(String tokenTypeValue) {

        if (!StringUtils.hasText(tokenTypeValue)) {
            throw new IllegalArgumentException(
                    "JWT token type must not be empty.");
        }

        try {
            return TokenType.valueOf(tokenTypeValue);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Unsupported JWT token type: " + tokenTypeValue,
                    exception);
        }
    }

    // =========================================================================
    // Audit
    // =========================================================================

    /**
     * Populates common audit information for a newly created revocation
     * record.
     *
     * @param entity         revocation entity.
     * @param serviceContext current FreshMeal service context.
     * @param timestamp      creation timestamp.
     */
    private void populateAuditFields(ABaseEntity entity,
            IServiceContext serviceContext,
            LocalDateTime timestamp) {

        String currentUser = "SYSTEM";

        if (serviceContext != null
                && serviceContext.getUserProfile() != null
                && StringUtils.hasText(
                        serviceContext.getUserProfile().getUsername())) {

            currentUser = serviceContext.getUserProfile().getUsername();
        }

        entity.setCreatedAt(timestamp);
        entity.setCreatedBy(currentUser);
        entity.setUpdatedAt(timestamp);
        entity.setUpdatedBy(currentUser);
    }
}