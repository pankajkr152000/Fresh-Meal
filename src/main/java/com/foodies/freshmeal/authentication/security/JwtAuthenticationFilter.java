package com.foodies.freshmeal.authentication.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.foodies.freshmeal.authentication.token.ITokenRevocationService;
import com.foodies.freshmeal.authentication.token.ITokenService;
import com.foodies.freshmeal.common.io.DataContext;
import com.foodies.freshmeal.common.io.service.IServiceContext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ============================================================================
 * Filter : JwtAuthenticationFilter
 * ============================================================================
 *
 * <p>
 * Establishes Spring Security authentication from a FreshMeal JWT access
 * token.
 * </p>
 *
 * <p>
 * This filter processes the HTTP {@code Authorization} header and extracts a
 * Bearer access token. The token is validated through {@link ITokenService}.
 * When valid, the identity and business roles contained in the
 * cryptographically
 * verified token are converted into a Spring Security {@link Authentication}
 * and placed into the current {@link SecurityContextHolder}.
 * </p>
 *
 * <h3>Authentication Flow</h3>
 *
 * <pre>
 * HTTP Request
 *      |
 *      v
 * Authorization: Bearer &lt;access-token&gt;
 *      |
 *      v
 * JWT validation
 *      |
 *      v
 * username + userNumber + business roles
 *      |
 *      v
 * Spring Security authorities
 *      |
 *      v
 * Authentication
 *      |
 *      v
 * SecurityContext
 * </pre>
 *
 * <h3>Stateless Authentication</h3>
 *
 * <p>
 * The filter does not load the user from MongoDB for every request. The
 * identity and authorization roles required for the request are read from
 * the cryptographically signed access token.
 * </p>
 *
 * <h3>Token Purpose</h3>
 *
 * <p>
 * Only tokens validated as {@code ACCESS} tokens by {@link ITokenService} are
 * accepted by this filter. Refresh tokens therefore cannot be used as bearer
 * access tokens.
 * </p>
 *
 * <h3>Role Conversion</h3>
 *
 * <p>
 * FreshMeal JWTs contain business role names such as {@code USER},
 * {@code ADMIN}, and {@code RESTAURANT_OWNER}. This filter converts those
 * values into Spring Security's conventional {@code ROLE_} authorities.
 * </p>
 *
 * <pre>
 * JWT role          Spring authority
 * --------          ----------------
 * USER       ->     ROLE_USER
 * ADMIN      ->     ROLE_ADMIN
 * </pre>
 *
 * <h3>Security Context</h3>
 *
 * <p>
 * An authentication is established only when the current security context
 * does not already contain an authentication. This prevents the filter from
 * unnecessarily replacing an existing authentication.
 * </p>
 *
 * <h3>Invalid Token Handling</h3>
 *
 * <p>
 * Invalid, expired, malformed, or incorrectly purposed JWTs never establish
 * authentication. The request is allowed to continue through the filter
 * chain so Spring Security can determine whether authentication is required
 * for the requested resource.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // =========================================================================
    // Constants
    // =========================================================================

    /**
     * HTTP authorization header.
     */
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * Bearer authentication scheme.
     */
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * Spring Security role authority prefix.
     */
    private static final String ROLE_PREFIX = "ROLE_";

    // =========================================================================
    // Dependencies
    // =========================================================================

    /**
     * Current request-scoped FreshMeal service context.
     */
    private final IServiceContext serviceContext;

    /**
     * FreshMeal JWT token service.
     */
    private final ITokenService tokenService;

    /**
     * Server-side JWT token and session revocation service.
     */
    private final ITokenRevocationService tokenRevocationService;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Creates the JWT authentication filter.
     *
     * @param tokenService           FreshMeal JWT token service.
     * @param tokenRevocationService server-side token and session revocation
     *                               service.
     * @param serviceContext         current request-scoped service context.
     */
    public JwtAuthenticationFilter(
            ITokenService tokenService,
            ITokenRevocationService tokenRevocationService,
            IServiceContext serviceContext) {

        this.tokenService = tokenService;
        this.tokenRevocationService = tokenRevocationService;
        this.serviceContext = serviceContext;
    }

    // =========================================================================
    // Filter Processing
    // =========================================================================

    /**
     * Processes the current HTTP request for a JWT bearer token.
     *
     * <p>
     * Requests without a bearer token are allowed to continue so that public
     * endpoints remain accessible. Protected endpoints will subsequently be
     * rejected by Spring Security when no authenticated principal exists.
     * </p>
     *
     * @param request     current HTTP request.
     * @param response    current HTTP response.
     * @param filterChain remaining servlet filter chain.
     *
     * @throws ServletException when servlet processing fails.
     * @throws IOException      when request processing fails.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        serviceContext.setAttribute(
                DataContext.IP_ADDRESS,
                request.getRemoteAddr());

        serviceContext.setAttribute(
                DataContext.USER_AGENT,
                request.getHeader("User-Agent"));

        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader == null
                || !authorizationHeader.startsWith(BEARER_PREFIX)) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader
                .substring(BEARER_PREFIX.length())
                .trim();

        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        serviceContext.setAttribute(
                DataContext.CURRENT_ACCESS_TOKEN,
                token);

        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();

        if (currentAuthentication != null
                && currentAuthentication.isAuthenticated()) {

            filterChain.doFilter(request, response);
            return;
        }

        authenticateRequest(request, token);

        filterChain.doFilter(request, response);
    }

    // =========================================================================
    // Authentication
    // =========================================================================

    /**
     * Establishes authentication when the supplied access token is valid.
     *
     * <p>
     * The token must first pass access-token validation. Identity and role
     * claims are then extracted from the cryptographically verified token.
     * </p>
     *
     * <p>
     * No database lookup is performed. This keeps the request authentication
     * path stateless and avoids unnecessary database access for every
     * authenticated request.
     * </p>
     *
     * @param request HTTP request.
     * @param token   bearer access token.
     */
    private void authenticateRequest(
            HttpServletRequest request,
            String token) {

        try {

            // -----------------------------------------------------------------
            // Validate Access Token
            // -----------------------------------------------------------------

            if (!tokenService.isAccessTokenValid(token)) {
                return;
            }

            // -----------------------------------------------------------------
            // Check Token Revocation
            // -----------------------------------------------------------------

            if (tokenRevocationService.isTokenRevoked(token)) {
                return;
            }

            // -----------------------------------------------------------------
            // Check Session Revocation
            // -----------------------------------------------------------------

            String sessionId = tokenService.getSessionId(token);

            if (!hasText(sessionId)) {
                return;
            }

            if (tokenRevocationService.isSessionRevoked(sessionId)) {
                return;
            }

            // -----------------------------------------------------------------
            // Extract Identity
            // -----------------------------------------------------------------

            String username = tokenService.getUsername(token);
            String userNumber = tokenService.getUserNumber(token);

            if (!hasText(username) || !hasText(userNumber)) {
                return;
            }

            // -----------------------------------------------------------------
            // Extract and Convert Roles
            // -----------------------------------------------------------------

            List<SimpleGrantedAuthority> authorities = buildAuthorities(tokenService.getRoles(token));

            // -----------------------------------------------------------------
            // Establish Spring Security Authentication
            // -----------------------------------------------------------------

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities);

            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request));

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

        } catch (Exception exception) {

            /*
             * Invalid JWTs must never establish authentication.
             *
             * The request is intentionally allowed to continue so that the
             * normal Spring Security authorization mechanism can determine
             * whether the requested resource requires authentication.
             */
            SecurityContextHolder.clearContext();
        }
    }

    // =========================================================================
    // Authority Construction
    // =========================================================================

    /**
     * Converts FreshMeal business roles into Spring Security authorities.
     *
     * <p>
     * JWT roles intentionally contain only FreshMeal business role names,
     * such as {@code USER} or {@code ADMIN}. The {@code ROLE_} prefix is
     * introduced here at the Spring Security boundary.
     * </p>
     *
     * @param roles FreshMeal business roles extracted from the verified JWT.
     *
     * @return Spring Security role authorities.
     */
    private List<SimpleGrantedAuthority> buildAuthorities(
            List<String> roles) {

        if (roles == null || roles.isEmpty()) {
            return List.of();
        }

        return roles.stream()
                .filter(this::isValidRole)
                .map(role -> new SimpleGrantedAuthority(
                        ROLE_PREFIX + role))
                .toList();
    }

    // =========================================================================
    // Role Validation
    // =========================================================================

    /**
     * Determines whether a JWT role is a valid FreshMeal business role.
     *
     * <p>
     * Only known {@link com.foodies.freshmeal.common.constants.RoleType}
     * values are accepted. This prevents arbitrary strings from being promoted
     * into Spring Security authorities merely because they were present in a
     * signed token.
     * </p>
     *
     * @param role FreshMeal business role.
     *
     * @return {@code true} when the role is a known FreshMeal role.
     */
    private boolean isValidRole(String role) {

        if (!hasText(role)) {
            return false;
        }

        try {
            com.foodies.freshmeal.common.constants.RoleType.valueOf(role);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    // =========================================================================
    // Text Validation
    // =========================================================================

    /**
     * Determines whether a value contains meaningful text.
     *
     * @param value value to validate.
     *
     * @return {@code true} when the value is not null and contains
     *         non-whitespace characters.
     */
    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
