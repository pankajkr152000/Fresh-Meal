
package com.foodies.freshmeal.authentication.token;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.foodies.freshmeal.authentication.entity.RevokedSessionEntity;
import com.foodies.freshmeal.authentication.entity.RevokedTokenEntity;
import com.foodies.freshmeal.authentication.repository.IRevokedSessionRepository;
import com.foodies.freshmeal.authentication.repository.IRevokedTokenRepository;
import com.foodies.freshmeal.common.constants.RepositoryConstants;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.io.service.IServiceContext;

// Add the AppCalendar import from your existing project package.

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ============================================================================
 * Service : TokenRevocationServiceImpl
 * ============================================================================
 *
 * Provides server-side revocation of individual JWTs and complete
 * authentication sessions.
 *
 * <p>
 * Individual JWTs are identified by their {@code jti} claim and persisted
 * through {@link RevokedTokenEntity}. Complete authentication sessions are
 * identified by their {@code sessionId} and persisted through
 * {@link RevokedSessionEntity}.
 * </p>
 *
 * <h3>Security responsibilities</h3>
 * <ul>
 *   <li>Never persist raw JWT values.</li>
 *   <li>Use cryptographically verified JWT claims.</li>
 *   <li>Make revocation operations idempotent.</li>
 *   <li>Prevent one user from revoking another user's session.</li>
 *   <li>Retain revocation records for audit and security enforcement.</li>
 * </ul>
 *
 * <h3>Database requirements</h3>
 * <p>
 * Unique indexes on {@code RevokedTokenEntity.tokenId} and
 * {@code RevokedSessionEntity.sessionId} are mandatory for concurrency-safe
 * idempotency. Duplicate-key handling in this service complements those
 * indexes; it does not replace them.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenRevocationServiceImpl implements ITokenRevocationService {

    // =========================================================================
    // Dependencies
    // =========================================================================

    private final IRevokedTokenRepository revokedTokenRepository;

    private final IRevokedSessionRepository revokedSessionRepository;

    private final ITokenService tokenService;

    // =========================================================================
    // Individual Token Revocation
    // =========================================================================

    /**
     * Revokes an individual JWT.
     *
     * <p>
     * The token is cryptographically verified before its claims are used.
     * Only the token identifier and relevant metadata are persisted.
     * </p>
     *
     * <p>
     * Repeated calls for an already-revoked token are treated as successful
     * idempotent operations.
     * </p>
     *
     * @param token          raw JWT to revoke.
     * @param serviceContext current service context.
     */
    @Override
    public void revokeToken(
            final String token,
            final IServiceContext serviceContext) {

        if (!StringUtils.hasText(token)) {
            return;
        }

        /*
         * Extract the JWT identifier first. ITokenService is responsible
         * for cryptographic verification of the supplied JWT.
         */
        final String tokenId = tokenService.getTokenId(token);

        if (!StringUtils.hasText(tokenId)) {
            throw new IllegalArgumentException(
                    "Verified JWT must contain a valid token identifier.");
        }

        /*
         * Avoid extracting and verifying the same JWT claims more than
         * necessary during the normal idempotency check.
         */
        if (isTokenIdRevoked(tokenId)) {
            return;
        }

        final String sessionId = tokenService.getSessionId(token);
        final String userNumber = tokenService.getUserNumber(token);
        final String tokenTypeValue = tokenService.getTokenType(token);
        final LocalDateTime expiresAt = tokenService.getExpiration(token);

        validateTokenClaims(
                sessionId,
                userNumber,
                tokenTypeValue,
                expiresAt);

        final LocalDateTime revokedAt =
                AppCalendar.getBusinessLocalDateTime();

        final TokenType tokenType = parseTokenType(tokenTypeValue);

        final RevokedTokenEntity entity =
                (RevokedTokenEntity) RevokedTokenEntity.create();

        entity.setTokenId(tokenId);
        entity.setSessionId(sessionId);
        entity.setUserNumber(userNumber);
        entity.setTokenType(tokenType);
        entity.setExpiresAt(expiresAt);
        entity.setRevokedAt(revokedAt);

        populateAuditFields(entity, serviceContext, revokedAt);

        try {
            revokedTokenRepository.save(entity);
        } catch (DuplicateKeyException exception) {

            /*
             * Another concurrent request may have revoked this token
             * after our initial existence check.
             *
             * Treat it as successful only if the same token identifier
             * is already persisted with matching ownership information.
             */
            final RevokedTokenEntity existingEntity =
                    findRevokedToken(tokenId)
                            .orElseThrow(() -> exception);

            if (!Objects.equals(
                    existingEntity.getUserNumber(), userNumber)
                    || !Objects.equals(
                    existingEntity.getSessionId(), sessionId)) {

                throw new IllegalStateException(
                        "Token revocation identifier is associated with "
                                + "conflicting ownership information.",
                        exception);
            }

            log.debug(
                    "JWT token is already revoked. tokenId={}",
                    tokenId);
        }
    }

    // =========================================================================
    // Individual Token Revocation Check
    // =========================================================================

    /**
     * Determines whether an individual JWT has been revoked.
     *
     * @param token raw JWT.
     * @return true if its token identifier has a revocation record.
     */
    @Override
    public boolean isTokenRevoked(final String token) {

        if (!StringUtils.hasText(token)) {
            return false;
        }

        final String tokenId = tokenService.getTokenId(token);

        if (!StringUtils.hasText(tokenId)) {
            return false;
        }

        return isTokenIdRevoked(tokenId);
    }

    /**
     * Checks revocation using an already-extracted token identifier.
     *
     * @param tokenId verified JWT identifier.
     * @return true if a revocation record exists.
     */
    private boolean isTokenIdRevoked(final String tokenId) {

        final Query query = Query.query(
                Criteria.where("tokenId").is(tokenId));

        return revokedTokenRepository.exists(query);
    }

    /**
     * Retrieves an individual token revocation record.
     *
     * @param tokenId verified JWT identifier.
     * @return matching revocation record, if present.
     */
    private Optional<RevokedTokenEntity> findRevokedToken(
            final String tokenId) {

        final Query query = Query.query(
                Criteria.where("tokenId").is(tokenId));

        return revokedTokenRepository.findOne(query);
    }

    // =========================================================================
    // Session Revocation
    // =========================================================================

    /**
     * Revokes an entire authentication session.
     *
     * <p>
     * All access and refresh tokens belonging to this session must be
     * rejected by the authentication and refresh-token validation paths
     * after this operation succeeds.
     * </p>
     *
     * <p>
     * The operation is idempotent. Repeated calls for the same session
     * and user do not create additional records.
     * </p>
     *
     * @param sessionId      authentication-session identifier.
     * @param userNumber     owner of the authentication session.
     * @param serviceContext current service context.
     */
    @Override
    public void revokeSession(
            final String sessionId,
            final String userNumber,
            final IServiceContext serviceContext) {

        if (!StringUtils.hasText(sessionId)) {
            return;
        }

        if (!StringUtils.hasText(userNumber)) {
            throw new IllegalArgumentException(
                    "User number is required for session revocation.");
        }

        /*
         * Verify whether the session has already been revoked.
         * A session identifier must always belong to the same user.
         */
        final Optional<RevokedSessionEntity> existingEntity =
                findRevokedSession(sessionId);

        if (existingEntity.isPresent()) {
            validateSessionOwnership(
                    existingEntity.get(),
                    userNumber);

            return;
        }

        final LocalDateTime revokedAt =
                AppCalendar.getBusinessLocalDateTime();

        final RevokedSessionEntity entity =
                (RevokedSessionEntity) RevokedSessionEntity.create();

        entity.setSessionId(sessionId);
        entity.setUserNumber(userNumber);
        entity.setRevokedAt(revokedAt);

        populateAuditFields(entity, serviceContext, revokedAt);

        try {
            revokedSessionRepository.save(entity);
        } catch (DuplicateKeyException exception) {

            /*
             * Another request may have revoked this session concurrently.
             * Verify the persisted record before treating the operation
             * as an idempotent success.
             */
            final RevokedSessionEntity persistedEntity =
                    findRevokedSession(sessionId)
                            .orElseThrow(() -> exception);

            validateSessionOwnership(
                    persistedEntity,
                    userNumber);

            log.debug(
                    "Authentication session is already revoked. sessionId={}",
                    sessionId);
        }
    }

    // =========================================================================
    // Session Revocation Check
    // =========================================================================

    /**
     * Determines whether an authentication session has been revoked.
     *
     * @param sessionId authentication-session identifier.
     * @return true if the session has a revocation record.
     */
    @Override
    public boolean isSessionRevoked(final String sessionId) {

        if (!StringUtils.hasText(sessionId)) {
            return false;
        }

        final Query query = Query.query(
                Criteria.where("sessionId").is(sessionId));

        return revokedSessionRepository.exists(query);
    }

    /**
     * Retrieves a session revocation record.
     *
     * @param sessionId authentication-session identifier.
     * @return matching revocation record, if present.
     */
    private Optional<RevokedSessionEntity> findRevokedSession(
            final String sessionId) {

        final Query query = Query.query(
                Criteria.where("sessionId").is(sessionId));

        return revokedSessionRepository.findOne(query);
    }

    /**
     * Ensures that an existing revoked session belongs to the expected user.
     *
     * @param entity            persisted revoked session.
     * @param expectedUserNumber expected session owner.
     */
    private void validateSessionOwnership(
            final RevokedSessionEntity entity,
            final String expectedUserNumber) {

        if (!Objects.equals(
                entity.getUserNumber(),
                expectedUserNumber)) {

            throw new IllegalStateException(
                    "Authentication session ownership mismatch.");
        }
    }

    // =========================================================================
    // Token Claim Validation
    // =========================================================================

    /**
     * Validates the security-sensitive claims required to persist a
     * token revocation record.
     *
     * @param sessionId    authentication-session identifier.
     * @param userNumber   token owner.
     * @param tokenType    token-type claim.
     * @param expiresAt    token expiration timestamp.
     */
    private void validateTokenClaims(
            final String sessionId,
            final String userNumber,
            final String tokenType,
            final LocalDateTime expiresAt) {

        if (!StringUtils.hasText(sessionId)) {
            throw new IllegalArgumentException(
                    "JWT must contain a valid session identifier.");
        }

        if (!StringUtils.hasText(userNumber)) {
            throw new IllegalArgumentException(
                    "JWT must contain a valid user identifier.");
        }

        if (!StringUtils.hasText(tokenType)) {
            throw new IllegalArgumentException(
                    "JWT must contain a valid token type.");
        }

        Objects.requireNonNull(
                expiresAt,
                "JWT expiration timestamp must not be null.");

    }

    // =========================================================================
    // Cleanup
    // =========================================================================

    /**
     * Removes individual token revocation records after their corresponding
     * JWTs have expired.
     *
     * <p>
     * Session revocation records are intentionally retained because the
     * session entity currently has no explicit expiration timestamp.
     * </p>
     */
    @Override
    public void cleanupExpiredRevocations() {

        final LocalDateTime now =
                AppCalendar.getBusinessLocalDateTime();

        final Query query = Query.query(
                Criteria.where("expiresAt").lte(now));

        revokedTokenRepository.findAll(query)
                .forEach(entity ->
                        revokedTokenRepository.deletePermanently(
                                entity.getId()));
    }

    // =========================================================================
    // Token Type
    // =========================================================================

    /**
     * Converts a token-type claim into the supported token-type enum.
     *
     * @param tokenTypeValue token-type claim.
     * @return corresponding token type.
     */
    private TokenType parseTokenType(final String tokenTypeValue) {

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
     * Populates common audit information for a revocation record.
     *
     * @param entity         revocation entity.
     * @param serviceContext current service context.
     * @param timestamp      operation timestamp.
     */
    private void populateAuditFields(
            final ABaseEntity entity,
            final IServiceContext serviceContext,
            final LocalDateTime timestamp) {

        final String auditActor = Optional.ofNullable(serviceContext)
                .map(IServiceContext::getUserProfile)
                .filter(Objects::nonNull)
                .map(profile -> profile.getUsername())
                .filter(StringUtils::hasText)
                .orElse(RepositoryConstants.SYSTEM_USER);

        entity.setCreatedAt(timestamp);
        entity.setCreatedBy(auditActor);
        entity.setUpdatedAt(timestamp);
        entity.setUpdatedBy(auditActor);
    }
}
