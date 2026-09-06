package com.foodies.freshmeal.authentication.token;

import java.time.LocalDateTime;
import java.util.List;

import com.foodies.freshmeal.user.entity.UserProfile;

/**
 * ============================================================================
 * Interface : ITokenService
 * ============================================================================
 *
 * <p>
 * Defines the token operations required by the FreshMeal authentication layer.
 * </p>
 *
 * <p>
 * The token service is responsible for generating, validating, and extracting
 * information from FreshMeal authentication tokens. It does not authenticate
 * passwords, load users, or perform endpoint authorization.
 * </p>
 *
 * <h3>Token Types</h3>
 *
 * <ul>
 * <li>
 * <b>Access Token</b> - short-lived token used to access protected
 * application APIs.
 * </li>
 * <li>
 * <b>Refresh Token</b> - longer-lived token used to obtain a new
 * access token.
 * </li>
 * </ul>
 *
 * <h3>Security Boundary</h3>
 *
 * <p>
 * JWT signing, parsing, validation, and claim extraction are isolated behind
 * this interface so the rest of FreshMeal does not depend directly on the
 * JWT implementation library.
 * </p>
 *
 * <p>
 * Implementations must cryptographically verify a token before trusting any
 * security-sensitive claims extracted from it.
 * </p>
 *
 * <h3>Role Representation</h3>
 *
 * <p>
 * JWT roles represent FreshMeal business roles using their enum names, such as
 * {@code USER}, {@code ADMIN}, or {@code RESTAURANT_OWNER}. Conversion into
 * Spring Security authorities such as {@code ROLE_USER} is the responsibility
 * of the authentication filter rather than the token service.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface ITokenService {

    /**
     * Generates a short-lived access token for the authenticated user.
     *
     * @param userProfile authenticated FreshMeal user.
     *
     * @return signed access token.
     */
    String generateAccessToken(UserProfile userProfile, String sessionId);

    /**
     * Generates a longer-lived refresh token for the authenticated user.
     *
     * @param userProfile authenticated FreshMeal user.
     *
     * @return signed refresh token.
     */
    String generateRefreshToken(UserProfile userProfile, String sessionId);

    /**
     * Validates a FreshMeal access token.
     *
     * <p>
     * Validation must include signature verification, expiration validation,
     * and verification that the token represents an access token.
     * </p>
     *
     * @param token access token.
     *
     * @return {@code true} when the token is valid.
     */
    boolean isAccessTokenValid(String token);

    /**
     * Validates a FreshMeal refresh token.
     *
     * <p>
     * Validation must include signature verification, expiration validation,
     * and verification that the token represents a refresh token.
     * </p>
     *
     * @param token refresh token.
     *
     * @return {@code true} when the token is valid.
     */
    boolean isRefreshTokenValid(String token);

    /**
     * Extracts the username from a cryptographically verified token.
     *
     * @param token JWT token.
     *
     * @return username contained in the verified token.
     */
    String getUsername(String token);

    /**
     * Extracts the FreshMeal business user number from a cryptographically
     * verified token.
     *
     * @param token JWT token.
     *
     * @return FreshMeal user number contained in the verified token.
     */
    String getUserNumber(String token);

    /**
     * Extracts the token type from a cryptographically verified token.
     *
     * <p>
     * The returned value identifies whether the token is an access token or
     * refresh token according to the FreshMeal token contract.
     * </p>
     *
     * @param token JWT token.
     *
     * @return token type contained in the verified token.
     */
    String getTokenType(String token);

    /**
     * Extracts FreshMeal business roles from a cryptographically verified
     * access token.
     *
     * <p>
     * The returned roles contain business role names rather than Spring
     * Security authority prefixes. For example, {@code USER} is returned
     * rather than {@code ROLE_USER}.
     * </p>
     *
     * @param token JWT access token.
     *
     * @return business roles contained in the verified token.
     */
    List<String> getRoles(String token);

    /**
     * {@inheritDoc}
     */
    long getAccessTokenExpirationSeconds();

    String getTokenId(String token);

    /**
     * Returns the FreshMeal authentication-session identifier contained
     * in the token.
     *
     * @param token JWT token.
     * @return authentication-session identifier.
     */
    String getSessionId(String token);

    LocalDateTime getExpiration(String token);
}
