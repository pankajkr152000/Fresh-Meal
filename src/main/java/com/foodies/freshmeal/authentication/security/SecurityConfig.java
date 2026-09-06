package com.foodies.freshmeal.authentication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.foodies.freshmeal.authentication.constants.AuthenticationApiConstants;
import com.foodies.freshmeal.authentication.service.IUserDetailsService;
import com.foodies.freshmeal.common.constants.ApiBaseConstants;

/**
 * ============================================================================
 * Configuration : SecurityConfig
 * ============================================================================
 *
 * <p>
 * Central Spring Security configuration for the FreshMeal application.
 * </p>
 *
 * <p>
 * FreshMeal uses stateless token-based authentication. Username/password
 * authentication is delegated to Spring Security's standard
 * {@link DaoAuthenticationProvider}, while JWT processing is handled by
 * {@link JwtAuthenticationFilter}.
 * </p>
 *
 * <h3>Authentication Architecture</h3>
 *
 * <pre>
 * AuthenticationService
 *          |
 *          v
 * AuthenticationManager
 *          |
 *          v
 * DaoAuthenticationProvider
 *          |
 *          v
 * Spring UserDetailsService
 *          |
 *          v
 * IUserDetailsService
 *          |
 *          v
 * UserDetailsServiceImpl
 *          |
 *          v
 * IUserService
 * </pre>
 *
 * <h3>Responsibilities</h3>
 *
 * <ul>
 * <li>Provide the application's {@link PasswordEncoder}.</li>
 * <li>Adapt the FreshMeal {@link IUserDetailsService} to Spring Security's
 * {@link UserDetailsService} contract.</li>
 * <li>Configure {@link DaoAuthenticationProvider}.</li>
 * <li>Expose the application's {@link AuthenticationManager}.</li>
 * <li>Configure stateless HTTP security.</li>
 * <li>Register the {@link JwtAuthenticationFilter}.</li>
 * <li>Define public and authenticated API boundaries.</li>
 * </ul>
 *
 * <h3>Password Security</h3>
 *
 * <p>
 * FreshMeal uses Spring Security's delegating password encoder. The encoded
 * password contains an explicit encoding identifier, allowing the application
 * to evolve password-encoding strategies without changing the domain model.
 * </p>
 *
 * <h3>Session Policy</h3>
 *
 * <p>
 * FreshMeal is a stateless REST API. Authentication state is therefore carried
 * by access tokens rather than server-side HTTP sessions.
 * </p>
 *
 * <h3>Authorization Boundary</h3>
 *
 * <p>
 * This configuration establishes authentication boundaries only. Detailed
 * role- and permission-based authorization belongs to the Authorization module
 * and can be introduced without redesigning the authentication infrastructure.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final IUserDetailsService userDetailsService;

    /**
     * Creates the security configuration.
     *
     * @param jwtAuthenticationFilter JWT request authentication filter.
     * @param userDetailsService      FreshMeal user-details service.
     */
    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            IUserDetailsService userDetailsService) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    // =========================================================================
    // Password Encoding
    // =========================================================================

    /**
     * Provides the application's password encoder.
     *
     * <p>
     * A delegating password encoder is used so that passwords are stored with
     * an explicit encoding identifier. This allows FreshMeal to support secure
     * password-encoding strategies and future migration without coupling the
     * domain model to a concrete encoder implementation.
     *
     * <pre>
     * {id}encoded-password
     * </pre>
     * </p>
     *
     * @return configured password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // =========================================================================
    // Spring Security UserDetails Adapter
    // =========================================================================

    /**
     * Adapts the FreshMeal user-details service to Spring Security.
     *
     * <p>
     * FreshMeal intentionally owns its own {@link IUserDetailsService}
     * abstraction so authentication remains integrated with the application's
     * existing service architecture.
     * </p>
     *
     * <p>
     * Spring Security's {@link DaoAuthenticationProvider}, however, consumes
     * the standard {@link UserDetailsService} contract. This adapter bridges
     * those two abstractions without forcing the FreshMeal service interface to
     * extend or depend on Spring Security's service contract.
     * </p>
     *
     * @return Spring Security user-details adapter.
     */
    @Bean
    public UserDetailsService springUserDetailsService() {
        return userDetailsService::loadUserByUsernameOrUserEmail;
    }

    // =========================================================================
    // Authentication Manager
    // =========================================================================

    /**
     * Creates the application's authentication manager.
     *
     * <p>
     * Username/password authentication is delegated to Spring Security's
     * {@link DaoAuthenticationProvider}. The provider loads the user through
     * the FreshMeal user-details adapter and verifies the supplied password
     * using the configured {@link PasswordEncoder}.
     * </p>
     *
     * <pre>
     * AuthenticationManager
     *        |
     *        v
     * DaoAuthenticationProvider
     *        |
     *        +---- UserDetailsService
     *        |
     *        +---- PasswordEncoder
     * </pre>
     *
     * @param springUserDetailsService Spring Security user-details adapter.
     * @param passwordEncoder          configured password encoder.
     *
     * @return configured authentication manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService springUserDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(springUserDetailsService);

        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    // =========================================================================
    // Security Filter Chain
    // =========================================================================

    /**
     * Configures the FreshMeal HTTP security filter chain.
     *
     * <p>
     * FreshMeal is a stateless REST API. Server-side HTTP sessions are therefore
     * disabled and authentication is established through JWT access tokens.
     * </p>
     *
     * <h3>Public Authentication Endpoints</h3>
     *
     * <p>
     * Registration, email verification, OTP resend, login, token refresh and
     * password recovery/reset endpoints must be accessible without an existing
     * authentication token.
     * </p>
     *
     * <h3>Authenticated Authentication Endpoints</h3>
     *
     * <p>
     * Logout and password change require an authenticated principal.
     * </p>
     *
     * <h3>Application Endpoints</h3>
     *
     * <p>
     * Application APIs require authentication unless a more specific
     * authorization rule is introduced by the Authorization module.
     * </p>
     *
     * @param http HTTP security configuration.
     *
     * @return configured security filter chain.
     *
     * @throws Exception when the security configuration cannot be built.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                // =============================================================
                // CSRF
                // =============================================================
                .csrf(csrf -> csrf.disable())

                // =============================================================
                // Stateless Authentication
                // =============================================================
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                // =============================================================
                // JWT Authentication Filter
                // =============================================================
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)

                // =============================================================
                // Authorization Rules
                // =============================================================
                .authorizeHttpRequests(authorize -> authorize

                        // -----------------------------------------------------
                        // Public Authentication Endpoints
                        // -----------------------------------------------------
                        .requestMatchers(
                                ApiBaseConstants.AUTHENTICATION_BASE_URL
                                        + AuthenticationApiConstants.REGISTER,

                                ApiBaseConstants.AUTHENTICATION_BASE_URL
                                        + AuthenticationApiConstants.VERIFY_EMAIL_OTP,

                                ApiBaseConstants.AUTHENTICATION_BASE_URL
                                        + AuthenticationApiConstants.RESEND_EMAIL_OTP,

                                ApiBaseConstants.AUTHENTICATION_BASE_URL
                                        + AuthenticationApiConstants.LOGIN,

                                ApiBaseConstants.AUTHENTICATION_BASE_URL
                                        + AuthenticationApiConstants.REFRESH,

                                ApiBaseConstants.AUTHENTICATION_BASE_URL
                                        + AuthenticationApiConstants.FORGOT_PASSWORD,

                                ApiBaseConstants.AUTHENTICATION_BASE_URL
                                        + AuthenticationApiConstants.RESET_PASSWORD)
                        .permitAll()

                        // -----------------------------------------------------
                        // Authenticated Authentication Endpoints
                        // -----------------------------------------------------
                        .requestMatchers(
                                ApiBaseConstants.AUTHENTICATION_BASE_URL
                                        + AuthenticationApiConstants.LOGOUT,

                                ApiBaseConstants.AUTHENTICATION_BASE_URL
                                        + AuthenticationApiConstants.CHANGE_PASSWORD)
                        .authenticated()

                        // -----------------------------------------------------
                        // Swagger / OpenAPI
                        // -----------------------------------------------------
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**")
                        .permitAll()

                        // -----------------------------------------------------
                        // Application Endpoints
                        // -----------------------------------------------------
                        .requestMatchers(
                                ApiBaseConstants.USER_BASE_URL + "/**",
                                ApiBaseConstants.ORDER_BASE_URL + "/**")
                        .authenticated()

                        // -----------------------------------------------------
                        // Everything Else
                        // -----------------------------------------------------
                        .anyRequest()
                        .authenticated());

        return http.build();
    }
}