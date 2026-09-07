package com.foodies.freshmeal.authentication.token;

import com.foodies.freshmeal.common.io.service.IServiceContext;

/**
 * ============================================================================
 * Service : ITokenRevocationService
 * ============================================================================
 *
 * Defines the contract for server-side JWT token revocation.
 *
 * <p>
 * FreshMeal uses stateless JWT authentication. A JWT normally remains valid
 * until its expiration time, even after a user logs out. This service provides
 * the server-side revocation mechanism required to invalidate such tokens
 * before their natural expiration.
 * </p>
 *
 * <h3>Revocation Levels</h3>
 * <ul>
 * <li><b>Token level</b> - revokes one individual JWT using its {@code jti}
 * ({@code tokenId}).</li>
 * <li><b>Session level</b> - revokes the complete authentication session using
 * {@code sessionId}.</li>
 * </ul>
 *
 * <h3>Security Principle</h3>
 * <p>
 * The actual JWT value is never persisted. Only its unique JWT identifier and
 * required metadata are stored by the implementation.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface ITokenRevocationService {

	/**
	 * Revokes the supplied JWT.
	 *
	 * <p>
	 * The token's {@code jti}, session identifier, user number, token type,
	 * expiration time, and revocation time are extracted and persisted.
	 * </p>
	 *
	 * @param token          JWT to revoke
	 * @param serviceContext current FreshMeal service context
	 */
	void revokeToken(String token, IServiceContext serviceContext);

	/**
	 * Determines whether an individual JWT has been revoked.
	 *
	 * @param token JWT to check
	 * @return {@code true} when the token has been revoked; otherwise {@code false}
	 */
	boolean isTokenRevoked(String token);

	/**
	 * Revokes an authentication session.
	 *
	 * <p>
	 * Session revocation is used when the complete login session must be
	 * invalidated rather than only one JWT.
	 * </p>
	 *
	 * @param sessionId      FreshMeal authentication session identifier
	 * @param userNumber     FreshMeal user number
	 * @param serviceContext current FreshMeal service context
	 */
	void revokeSession(String sessionId, String userNumber, IServiceContext serviceContext);

	/**
	 * Determines whether an authentication session has been revoked.
	 *
	 * @param sessionId FreshMeal authentication session identifier
	 * @return {@code true} when the session has been revoked; otherwise
	 *         {@code false}
	 */
	boolean isSessionRevoked(String sessionId);

	/**
	 * Removes revocation records whose corresponding JWTs have expired.
	 *
	 * <p>
	 * Expired revocation records no longer provide security value because the
	 * corresponding JWT can no longer be accepted based on its expiration.
	 * </p>
	 */
	void cleanupExpiredRevocations();
}