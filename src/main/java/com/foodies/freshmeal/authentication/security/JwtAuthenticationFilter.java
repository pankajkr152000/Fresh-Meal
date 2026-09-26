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
import com.foodies.freshmeal.common.constants.RoleType;
import com.foodies.freshmeal.common.io.DataContext;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.user.dto.UserNumberRequest;
import com.foodies.freshmeal.user.entity.UserEntity;
import com.foodies.freshmeal.user.entity.UserProfile;
import com.foodies.freshmeal.user.service.IUserService;

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
 * Establishes Spring Security authentication and the FreshMeal request-scoped
 * {@link IServiceContext} from a validated JWT access token.
 * </p>
 *
 * <p>
 * The filter performs the following responsibilities:
 * </p>
 *
 * <ul>
 * <li>Extract the bearer access token.</li>
 * <li>Validate the access token.</li>
 * <li>Validate token and login-session revocation state.</li>
 * <li>Resolve the authenticated {@link UserEntity} using the JWT user
 * number.</li>
 * <li>Create the application's {@link UserProfile}.</li>
 * <li>Populate Spring Security's {@link SecurityContextHolder}.</li>
 * <li>Populate the current request-scoped {@link IServiceContext}.</li>
 * </ul>
 *
 * <p>
 * The JWT remains the authentication credential. The user lookup is performed
 * only to reconstruct the FreshMeal security profile required by the
 * application service layer.
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
 * Token / Session revocation validation
 *      |
 *      v
 * userNumber + username + roles
 *      |
 *      v
 * IUserService.loadUserByUserNumber(...)
 *      |
 *      v
 * UserEntity
 *      |
 *      v
 * UserProfile
 *      |
 *      +----------------------+
 *      |                      |
 *      v                      v
 * SecurityContext       IServiceContext
 *                              |
 *                              v
 *                     Application Services
 * </pre>
 *
 * <h3>Request Context</h3>
 *
 * <p>
 * {@link IServiceContext} is request-scoped. Therefore the authenticated
 * {@link UserProfile} must be populated during every authenticated HTTP
 * request. It must not be expected to survive from the login request into
 * subsequent requests.
 * </p>
 *
 * <h3>Invalid Token Handling</h3>
 *
 * <p>
 * Invalid, expired, malformed, revoked, or incorrectly purposed JWTs never
 * establish authentication. The request is allowed to continue through the
 * filter chain so Spring Security can determine whether the requested resource
 * requires authentication.
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

    /**
     * FreshMeal user service responsible for resolving the authenticated
     * application's user entity.
     */
    private final IUserService userService;

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
     * @param userService            FreshMeal user service.
     */
    public JwtAuthenticationFilter(
            ITokenService tokenService,
            ITokenRevocationService tokenRevocationService,
            IServiceContext serviceContext,
            IUserService userService) {

        this.tokenService = tokenService;
        this.tokenRevocationService = tokenRevocationService;
        this.serviceContext = serviceContext;
        this.userService = userService;
    }

    // =========================================================================
    // Filter Processing
    // =========================================================================

    /**
     * Processes the current HTTP request for a JWT bearer token.
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

        String token = authorizationHeader.substring(BEARER_PREFIX.length()).trim();

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
     * The token is first validated and checked against server-side token and
     * session revocation state. The verified user number is then used to
     * resolve the current {@link UserEntity}. A {@link UserProfile} is created
     * from that entity and the verified JWT authorities.
     * </p>
     *
     * <p>
     * The resulting authentication is stored in Spring Security and the
     * resulting {@link UserProfile} is also stored in the current request-scoped
     * {@link IServiceContext}.
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

            if (!hasText(username)
                    || !hasText(userNumber)) {
                return;
            }

            // -----------------------------------------------------------------
            // Extract and Convert Roles
            // -----------------------------------------------------------------

            List<SimpleGrantedAuthority> authorities = buildAuthorities(tokenService.getRoles(token));

            // -----------------------------------------------------------------
            // Resolve FreshMeal User
            // -----------------------------------------------------------------

            UserEntity userEntity = loadUserByUserNumber(userNumber);

            if (userEntity == null) {
                return;
            }

            // -----------------------------------------------------------------
            // Create FreshMeal User Profile
            // -----------------------------------------------------------------

            UserProfile userProfile = UserProfile.create(userEntity, authorities);

            // -----------------------------------------------------------------
            // Populate Request Service Context
            // -----------------------------------------------------------------

            serviceContext.setUserProfile(userProfile);

            // -----------------------------------------------------------------
            // Establish Spring Security Authentication
            // -----------------------------------------------------------------

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userProfile,
                    null,
                    authorities);

            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request));

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

        } catch (Exception exception) {

            /*
             * Invalid JWTs or user-resolution failures must never establish
             * authentication.
             *
             * The request is intentionally allowed to continue so that the
             * normal Spring Security authorization mechanism can determine
             * whether the requested resource requires authentication.
             */
            SecurityContextHolder.clearContext();
        }
    }

    // =========================================================================
    // User Resolution
    // =========================================================================

    /**
     * Resolves the authenticated FreshMeal user using the business user number
     * contained in the verified JWT.
     *
     * <p>
     * User persistence access remains behind {@link IUserService}. The security
     * filter therefore does not access a repository directly.
     * </p>
     *
     * @param userNumber FreshMeal business user number.
     *
     * @return resolved active user entity, or {@code null} when the user cannot
     *         be resolved.
     */
    private UserEntity loadUserByUserNumber(String userNumber) {

        UserNumberRequest request = new UserNumberRequest();

        request.setUserNumber(userNumber);

        IServiceInput<UserNumberRequest> input = new ServiceInput<>();

        input.setInput(request);

        IServiceOutput<UserEntity> output = userService.loadUserByUserNumber(input);

        if (output == null) {
            return null;
        }

        return output.getOutput();
    }

    // =========================================================================
    // Authority Construction
    // =========================================================================

    /**
     * Converts FreshMeal business roles into Spring Security authorities.
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
     * @param role FreshMeal business role.
     *
     * @return {@code true} when the role is a known FreshMeal role.
     */
    private boolean isValidRole(String role) {

        if (!hasText(role)) {
            return false;
        }

        try {

            RoleType.valueOf(role);
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