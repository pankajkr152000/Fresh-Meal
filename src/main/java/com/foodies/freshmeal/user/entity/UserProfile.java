package com.foodies.freshmeal.user.entity;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

/**
 * ============================================================================
 * Entity : UserProfile
 * ============================================================================
 *
 * Represents the authenticated security principal of a FreshMeal user.
 *
 * <p>
 * {@code UserProfile} is a security-facing projection of {@link UserEntity}.
 * It contains the information required by Spring Security while keeping the
 * persisted business representation of the user inside {@link UserEntity}.
 * </p>
 *
 * <p>
 * This class is intentionally not a MongoDB persistence entity. A
 * {@code UserProfile} is created when a user is loaded during authentication
 * and is normally associated with the current Spring Security context for
 * the lifetime of the authenticated request/session.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Represent an authenticated FreshMeal user.</li>
 * <li>Provide username information to Spring Security.</li>
 * <li>Expose authorities derived from the user's FreshMeal roles.</li>
 * <li>Expose account-state information required by Spring Security.</li>
 * <li>Provide access to the underlying {@link UserEntity} when required by
 * the application security/business layer.</li>
 * </ul>
 *
 * <h3>Relationship with UserEntity</h3>
 * <p>
 * {@link UserEntity} remains the single source of truth for persisted user
 * information. {@code UserProfile} must not independently maintain another
 * copy of credentials, roles, or account state.
 * </p>
 *
 * <h3>Authorization</h3>
 * <p>
 * FreshMeal roles are converted into Spring Security
 * {@link GrantedAuthority} instances by the authentication/security layer.
 * This keeps the domain model independent from Spring Security while allowing
 * the application to use standard Spring Security authorization mechanisms.
 * </p>
 *
 * <h3>Security Consideration</h3>
 * <p>
 * The password is exposed through {@link #getPassword()} only because it is
 * required by the {@link UserDetails} contract. The password must always be
 * encoded before being persisted and should never be logged, serialized into
 * API responses, or exposed to the frontend.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
public class UserProfile implements UserDetails {

    private static final long serialVersionUID = 4377183688839034087L;

    // =========================================================================
    // User
    // =========================================================================

    /**
     * Persisted FreshMeal user represented by this security principal.
     */
    private final UserEntity userEntity;

    // =========================================================================
    // Authentication
    // =========================================================================

    /**
     * Username used by Spring Security to identify the authenticated user.
     */
    private final String username;

    /**
     * Encoded password used during authentication.
     *
     * <p>
     * This value is inherited from {@link UserEntity} and is not independently
     * persisted by {@code UserProfile}.
     * </p>
     */
    private final String password;

    // =========================================================================
    // Authorization
    // =========================================================================

    /**
     * Authorities granted to the authenticated user.
     *
     * <p>
     * These authorities are derived from the roles assigned to the underlying
     * {@link UserEntity}.
     * </p>
     */
    private final Collection<? extends GrantedAuthority> authorities;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Creates a security profile from a persisted {@link UserEntity}.
     *
     * @param userEntity  persisted FreshMeal user.
     * @param authorities authorities derived from the user's roles.
     */
    public UserProfile(
            UserEntity userEntity,
            Collection<? extends GrantedAuthority> authorities) {

        if (userEntity == null) {
            throw new IllegalArgumentException("UserEntity must not be null");
        }

        this.userEntity = userEntity;
        this.username = userEntity.getUsername();
        this.password = userEntity.getPassword();
        this.authorities = authorities == null
                ? Collections.emptyList()
                : Collections.unmodifiableCollection(authorities);
    }

    // =========================================================================
    // UserDetails : Account State
    // =========================================================================

    /**
     * Indicates whether the user's account has expired.
     *
     * @return {@code true} when the account has not expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return userEntity.isAccountNonExpired();
    }

    /**
     * Indicates whether the user's account is locked.
     *
     * @return {@code true} when the account is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return userEntity.isAccountNonLocked();
    }

    /**
     * Indicates whether the user's credentials have expired.
     *
     * @return {@code true} when the credentials are still valid.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return userEntity.isCredentialsNonExpired();
    }

    /**
     * Indicates whether the user account is enabled.
     *
     * @return {@code true} when the account is enabled.
     */
    @Override
    public boolean isEnabled() {
        return userEntity.isEnabled();
    }

    // =========================================================================
    // Factory
    // =========================================================================

    /**
     * Creates a security profile from the supplied user entity and authorities.
     *
     * @param userEntity  persisted FreshMeal user.
     * @param authorities authorities derived from the user's roles.
     * @return authenticated user profile.
     */
    public static UserProfile create(
            UserEntity userEntity,
            Collection<? extends GrantedAuthority> authorities) {

        return new UserProfile(userEntity, authorities);
    }

    /**
     * Returns the business identifier of the underlying FreshMeal user.
     *
     * @return user business number.
     */
    public String getUserNumber() {
        return userEntity.getUserNumber();
    }
}