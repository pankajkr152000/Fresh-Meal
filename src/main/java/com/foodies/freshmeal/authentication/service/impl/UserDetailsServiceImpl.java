package com.foodies.freshmeal.authentication.service.impl;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.foodies.freshmeal.common.constants.RoleType;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.user.dto.UsernameRequest;
import com.foodies.freshmeal.user.entity.UserEntity;
import com.foodies.freshmeal.user.entity.UserProfile;
import com.foodies.freshmeal.user.service.IUserService;

/**
 * ============================================================================
 * Service : UserDetailsServiceImpl
 * ============================================================================
 *
 * Loads FreshMeal users for Spring Security authentication.
 *
 * <p>
 * This implementation acts as the bridge between the FreshMeal user domain
 * represented by {@link UserEntity} and the Spring Security authentication
 * model represented by {@link UserDetails}.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Load an active FreshMeal user through {@link IUserService}.</li>
 * <li>Convert FreshMeal {@link RoleType} values into Spring Security
 * {@link GrantedAuthority} instances.</li>
 * <li>Create a {@link UserProfile} for Spring Security.</li>
 * </ul>
 *
 * <h3>Authentication Boundary</h3>
 * <p>
 * This class does not authenticate passwords itself. Password verification is
 * performed by Spring Security through the configured authentication provider.
 * This service only supplies the user information required by that process.
 * </p>
 *
 * <h3>Domain Separation</h3>
 * <p>
 * FreshMeal business roles remain represented by {@link RoleType}. Spring
 * Security-specific authority names are created only at this security
 * boundary, preventing framework-specific authorization concepts from leaking
 * into the persisted user domain.
 * </p>
 *
 * <h3>Security Consideration</h3>
 * <p>
 * Only active users should be loaded for normal authentication. The existing
 * {@link IUserService} and repository infrastructure are responsible for
 * applying FreshMeal's active-record semantics.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // =========================================================================
    // Dependencies
    // =========================================================================

    /**
     * FreshMeal user service used to load persisted user information.
     */
    private final IUserService userService;

    /**
     * Service context used by the FreshMeal service layer.
     */
    private final IServiceContext serviceContext;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Creates the Spring Security user-details service.
     *
     * @param userService    FreshMeal user service.
     * @param serviceContext current service context.
     */
    public UserDetailsServiceImpl(
            IUserService userService,
            IServiceContext serviceContext) {

        this.userService = userService;
        this.serviceContext = serviceContext;
    }

    // =========================================================================
    // UserDetailsService
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        if (username == null || username.isBlank()) {
            throw new UsernameNotFoundException("User not found.");
        }

        UsernameRequest request = new UsernameRequest();
        request.setUsername(username);

        ServiceInput<UsernameRequest> input = new ServiceInput<>();
        input.setInput(request);
        input.setServiceContext(serviceContext);

        UserEntity userEntity = userService
                .loadUserByUsername(input)
                .getOutput();

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found.");
        }

        Collection<GrantedAuthority> authorities = buildAuthorities(userEntity);

        return UserProfile.create(userEntity, authorities);
    }

    // =========================================================================
    // Authority Mapping
    // =========================================================================

    /**
     * Converts FreshMeal business roles into Spring Security authorities.
     *
     * <p>
     * FreshMeal persists roles as {@link RoleType}. Spring Security expects
     * {@link GrantedAuthority} instances. The conventional {@code ROLE_}
     * prefix is applied at this security boundary.
     * </p>
     *
     * <pre>
     * ADMIN              -> ROLE_ADMIN
     * USER               -> ROLE_USER
     * RESTAURANT_OWNER   -> ROLE_RESTAURANT_OWNER
     * DELIVERY_PARTNER   -> ROLE_DELIVERY_PARTNER
     * </pre>
     *
     * @param userEntity persisted FreshMeal user.
     *
     * @return immutable collection of Spring Security authorities.
     */
    private Collection<GrantedAuthority> buildAuthorities(
            UserEntity userEntity) {

        if (userEntity.getRoles() == null
                || userEntity.getRoles().isEmpty()) {
            return Collections.emptyList();
        }

        return userEntity.getRoles()
                .stream()
                .filter(role -> role != null)
                .map(this::toAuthority)
                .toList();
    }

    /**
     * Converts a FreshMeal role into a Spring Security authority.
     *
     * @param role FreshMeal business role.
     *
     * @return corresponding Spring Security authority.
     */
    private GrantedAuthority toAuthority(RoleType role) {

        return new SimpleGrantedAuthority(
                "ROLE_" + role.name());
    }
}
