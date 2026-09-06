package com.foodies.freshmeal.authentication.token;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.foodies.freshmeal.user.entity.UserProfile;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * ============================================================================
 * Service : JwtTokenService
 * ============================================================================
 *
 * <p>
 * Provides JWT generation and validation for FreshMeal authentication.
 * </p>
 *
 * <p>
 * This service isolates the application from the underlying JWT library and
 * provides separate lifecycle handling for access and refresh tokens.
 * </p>
 *
 * <h3>Access Token</h3>
 *
 * <p>
 * Access tokens are short-lived and contain the authenticated user's identity
 * and business roles required by protected API requests.
 * </p>
 *
 * <h3>Refresh Token</h3>
 *
 * <p>
 * Refresh tokens have a longer lifetime and are intended only for obtaining
 * new access tokens. They intentionally contain fewer authorization claims
 * than access tokens.
 * </p>
 *
 * <h3>Token Claims</h3>
 *
 * <ul>
 * <li>{@code sub} - FreshMeal username.</li>
 * <li>{@code userNumber} - FreshMeal business user number.</li>
 * <li>{@code roles} - FreshMeal business roles on access tokens.</li>
 * <li>{@code tokenType} - access or refresh token purpose.</li>
 * <li>{@code iat} - token issue time.</li>
 * <li>{@code exp} - token expiration time.</li>
 * <li>{@code jti} - unique token identifier.</li>
 * </ul>
 *
 * <h3>Security Considerations</h3>
 *
 * <ul>
 * <li>
 * The signing secret is supplied through external configuration and is
 * never hard-coded in source code.
 * </li>
 * <li>
 * Tokens are cryptographically verified before their claims are trusted.
 * </li>
 * <li>
 * Access and refresh tokens have separate lifetimes and token purposes.
 * </li>
 * <li>
 * Each generated token receives a unique {@code jti}.
 * </li>
 * </ul>
 *
 * <h3>Responsibility Boundary</h3>
 *
 * <p>
 * This service does not authenticate passwords, load users from the database,
 * manage user sessions, or perform endpoint authorization.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Service
public class JwtTokenService implements ITokenService {

    // =========================================================================
    // JWT Configuration
    // =========================================================================

    /**
     * JWT signing key derived from the externally supplied secret.
     */
    private final SecretKey signingKey;

    /**
     * Access-token lifetime.
     */
    private final Duration accessTokenExpiration;

    /**
     * Refresh-token lifetime.
     */
    private final Duration refreshTokenExpiration;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Creates the JWT token service.
     *
     * <p>
     * The JWT secret and token lifetimes are supplied through external
     * application configuration.
     * </p>
     *
     * @param secret                 JWT signing secret.
     * @param accessTokenExpiration  access-token lifetime in milliseconds.
     * @param refreshTokenExpiration refresh-token lifetime in milliseconds.
     *
     * @throws IllegalArgumentException when the secret or token lifetimes are
     *                                  invalid.
     */
    public JwtTokenService(
            @Value("${freshmeal.security.jwt.secret}") String secret,
            @Value("${freshmeal.security.jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${freshmeal.security.jwt.refresh-token-expiration}") long refreshTokenExpiration) {

        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException(
                    "JWT signing secret must not be empty.");
        }

        if (accessTokenExpiration <= 0) {
            throw new IllegalArgumentException(
                    "Access-token expiration must be greater than zero.");
        }

        if (refreshTokenExpiration <= 0) {
            throw new IllegalArgumentException(
                    "Refresh-token expiration must be greater than zero.");
        }

        this.signingKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8));

        this.accessTokenExpiration = Duration.ofMillis(accessTokenExpiration);

        this.refreshTokenExpiration = Duration.ofMillis(refreshTokenExpiration);
    }

    // =========================================================================
    // Access Token
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateAccessToken(UserProfile userProfile, String sessionId) {

        validateUserProfile(userProfile);

        Instant issuedAt = Instant.now();
        Instant expiration = issuedAt.plus(accessTokenExpiration);

        List<String> roles = userProfile.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .filter(authority -> authority != null
                        && authority.startsWith("ROLE_"))
                .map(authority -> authority.substring("ROLE_".length()))
                .toList();

        validateSessionId(sessionId);

        return Jwts.builder()
                .subject(userProfile.getUsername())
                .claim("userNumber", userProfile.getUserNumber())
                .claim("roles", roles)
                .claim("sessionId", sessionId)
                .claim("tokenType", TokenType.ACCESS.name())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .id(UUID.randomUUID().toString())
                .signWith(signingKey)
                .compact();
    }

    // =========================================================================
    // Refresh Token
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateRefreshToken(UserProfile userProfile, String sessionId) {

        validateUserProfile(userProfile);

        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException(
                    "Session ID must not be empty.");
        }

        Instant issuedAt = Instant.now();
        Instant expiration = issuedAt.plus(refreshTokenExpiration);

        return Jwts.builder()
                .subject(userProfile.getUsername())
                .claim("userNumber", userProfile.getUserNumber())
                .claim("sessionId", sessionId)
                .claim("tokenType", TokenType.REFRESH.name())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .id(UUID.randomUUID().toString())
                .signWith(signingKey)
                .compact();
    }

    // =========================================================================
    // Token Validation
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAccessTokenValid(String token) {
        return isTokenValid(token, TokenType.ACCESS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRefreshTokenValid(String token) {
        return isTokenValid(token, TokenType.REFRESH);
    }

    /**
     * Validates a JWT signature, expiration, and intended token purpose.
     *
     * <p>
     * JJWT performs signature and standard JWT validation while parsing the
     * signed claims. The token is considered valid only when the cryptographic
     * signature and expiration are valid and its {@code tokenType} claim
     * matches the expected purpose.
     * </p>
     *
     * @param token        JWT token.
     * @param expectedType expected token purpose.
     *
     * @return {@code true} when the token is valid for the requested purpose.
     */
    private boolean isTokenValid(
            String token,
            TokenType expectedType) {

        if (token == null || token.isBlank() || expectedType == null) {
            return false;
        }

        try {
            Claims claims = parseClaims(token);

            String tokenType = claims.get("tokenType", String.class);

            return expectedType.name().equals(tokenType);

        } catch (Exception exception) {
            return false;
        }
    }

    // =========================================================================
    // Claims
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserNumber(String token) {
        return parseClaims(token)
                .get("userNumber", String.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTokenType(String token) {
        return parseClaims(token)
                .get("tokenType", String.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getRoles(String token) {

        Claims claims = parseClaims(token);

        Object rolesClaim = claims.get("roles");

        if (!(rolesClaim instanceof List<?> roles)) {
            return List.of();
        }

        return roles.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .toList();
    }

    // =========================================================================
    // Access Token Expiration
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public long getAccessTokenExpirationSeconds() {
        return accessTokenExpiration.toSeconds();
    }

    // =========================================================================
    // JWT Parsing
    // =========================================================================

    /**
     * Parses and cryptographically verifies JWT claims.
     *
     * <p>
     * JJWT verifies the token signature using the configured signing key while
     * parsing the signed claims. Expired, malformed, or otherwise invalid
     * tokens are rejected by the parser.
     * </p>
     *
     * <p>
     * Callers must treat the returned claims as trusted only because this method
     * performs signature verification before returning them.
     * </p>
     *
     * @param token JWT token.
     *
     * @return verified JWT claims.
     *
     * @throws RuntimeException when the token cannot be parsed or verified.
     */
    private Claims parseClaims(String token) {

        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException(
                    "JWT token must not be empty.");
        }

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // =========================================================================
    // Validation Helpers
    // =========================================================================

    /**
     * Validates the user profile required for token generation.
     *
     * <p>
     * Token generation requires a valid authenticated user identity. Failing
     * fast here prevents generation of tokens containing incomplete identity
     * information.
     * </p>
     *
     * @param userProfile FreshMeal authenticated user profile.
     *
     * @throws IllegalArgumentException when the profile or required identity
     *                                  information is missing.
     */
    private void validateUserProfile(UserProfile userProfile) {

        if (userProfile == null) {
            throw new IllegalArgumentException(
                    "User profile must not be null.");
        }

        if (userProfile.getUsername() == null
                || userProfile.getUsername().isBlank()) {
            throw new IllegalArgumentException(
                    "User profile username must not be empty.");
        }

        if (userProfile.getUserNumber() == null
                || userProfile.getUserNumber().isBlank()) {
            throw new IllegalArgumentException(
                    "User profile user number must not be empty.");
        }
    }

    /**
     * Returns the unique JWT identifier ({@code jti}) of a token.
     *
     * <p>
     * The token is cryptographically verified before the identifier is returned.
     * </p>
     *
     * @param token JWT token.
     * @return unique JWT identifier.
     */
    @Override
    public String getTokenId(String token) {
        return parseClaims(token).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSessionId(String token) {
        return parseClaims(token)
                .get("sessionId", String.class);
    }

    /**
     * Validates the authentication-session identifier.
     *
     * @param sessionId authentication-session identifier.
     *
     * @throws IllegalArgumentException when the session identifier is empty.
     */
    private void validateSessionId(String sessionId) {

        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException(
                    "Session ID must not be empty.");
        }
    }

    /**
     * Returns the expiration time of a cryptographically verified JWT.
     *
     * <p>
     * The token is cryptographically verified before its expiration claim is
     * returned.
     * </p>
     *
     * @param token JWT token.
     * @return token expiration time.
     */
    @Override
    public LocalDateTime getExpiration(String token) {

        Date expiration = parseClaims(token).getExpiration();

        if (expiration == null) {
            return null;
        }

        return expiration.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();
    }
}