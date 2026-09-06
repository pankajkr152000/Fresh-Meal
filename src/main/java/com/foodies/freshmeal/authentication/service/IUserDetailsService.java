
package com.foodies.freshmeal.authentication.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.foodies.freshmeal.common.exception.UsernameNotFoundException;
import com.foodies.freshmeal.user.entity.UserProfile;
import com.foodies.freshmeal.user.service.IUserService;

/**
 * ============================================================================
 * FreshMeal Authentication - User Details Service
 * ============================================================================
 *
 * <p>
 * Defines the FreshMeal authentication abstraction responsible for loading
 * application users for Spring Security authentication.
 * </p>
 *
 * <p>
 * This interface intentionally remains a FreshMeal-specific abstraction rather
 * than extending Spring Security's {@code UserDetailsService}. The
 * authentication
 * module owns the integration between this service and Spring Security.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Load a user using either username or email address.</li>
 * <li>Delegate user lookup to the existing {@link IUserService}.</li>
 * <li>Preserve the existing FreshMeal service and repository architecture.</li>
 * <li>Return a Spring Security compatible {@link UserDetails}
 * implementation.</li>
 * <li>Expose the user's business roles as Spring Security authorities.</li>
 * </ul>
 *
 * <h3>Security Boundary</h3>
 *
 * <p>
 * This service does not authenticate passwords itself. Password authentication
 * is handled by Spring Security's {@code DaoAuthenticationProvider} using the
 * {@link UserDetails} returned by this service and the configured
 * {@code PasswordEncoder}.
 * </p>
 *
 * @see IUserService
 * @see UserProfile
 * @see UserDetails
 */
public interface IUserDetailsService {

    /**
     * Loads a FreshMeal user using a username or email address.
     *
     * <p>
     * The supplied identifier is resolved as either a username or an email
     * address. The actual user lookup is delegated to {@link IUserService} so
     * that authentication does not bypass the existing FreshMeal service and
     * repository architecture.
     * </p>
     *
     * <p>
     * After the user is loaded, the user's business roles are converted into
     * Spring Security authorities and wrapped in a {@link UserProfile}.
     * </p>
     *
     * <p>
     * The returned {@link UserDetails} is subsequently consumed by Spring
     * Security's authentication infrastructure for credential and account-state
     * validation.
     * </p>
     *
     * @param identifier username or email address supplied for authentication.
     *
     * @return Spring Security compatible user details.
     *
     * @throws UsernameNotFoundException when the user cannot be found or the
     *                                   supplied identifier is invalid.
     */
    UserDetails loadUserByUsernameOrUserEmail(String identifier)
            throws UsernameNotFoundException;
}