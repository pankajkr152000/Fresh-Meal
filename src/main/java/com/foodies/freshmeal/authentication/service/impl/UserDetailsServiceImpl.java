
package com.foodies.freshmeal.authentication.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.foodies.freshmeal.authentication.constants.AuthenticationErrorConstants;
import com.foodies.freshmeal.authentication.service.IUserDetailsService;
import com.foodies.freshmeal.common.constants.RoleType;
import com.foodies.freshmeal.common.exception.UsernameNotFoundException;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.common.valueObject.EmailAddress;
import com.foodies.freshmeal.user.dto.EmailRequest;
import com.foodies.freshmeal.user.dto.UsernameRequest;
import com.foodies.freshmeal.user.entity.UserEntity;
import com.foodies.freshmeal.user.entity.UserProfile;
import com.foodies.freshmeal.user.service.IUserService;

/**
 * ============================================================================
 * Security : UserDetailsServiceImpl
 * ============================================================================
 *
 * Provides the FreshMeal security bridge between application user data and
 * Spring Security's {@link UserDetails} model.
 *
 * <p>
 * FreshMeal keeps {@link UserEntity} as the persisted source of truth for user
 * identity and uses {@link UserProfile} as the non-persistent Spring Security
 * representation of an authenticated user.
 * </p>
 *
 * <h3>Responsibilities</h3>
 *
 * <ul>
 * <li>Resolve a login identifier to a FreshMeal {@link UserEntity}.</li>
 * <li>Support both username and email authentication identifiers.</li>
 * <li>Convert FreshMeal roles into Spring Security authorities.</li>
 * <li>Create a {@link UserProfile} for Spring Security authentication.</li>
 * </ul>
 *
 * <h3>Security Boundary</h3>
 *
 * <p>
 * This component does not verify passwords itself. Password verification is
 * delegated to Spring Security's authentication provider and configured
 * password encoder.
 * </p>
 *
 * <p>
 * This component also does not access the user repository directly. User lookup
 * remains the responsibility of {@link IUserService}, preserving the existing
 * FreshMeal service-layer architecture.
 * </p>
 *
 * <h3>Authentication Flow</h3>
 *
 * <pre>
 * Login identifier
 *       |
 *       v
 * IUserDetailsService
 *       |
 *       v
 * UserDetailsServiceImpl
 *       |
 *       +---- Email ----&gt; IUserService.loadUserByEmail()
 *       |
 *       +---- Username -&gt; IUserService.loadUserByUsername()
 *                              |
 *                              v
 *                         UserEntity
 *                              |
 *                              v
 *                         UserProfile
 *                              |
 *                              v
 *                    Spring Security
 * </pre>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Service
public class UserDetailsServiceImpl implements IUserDetailsService {

	// =========================================================================
	// Dependencies
	// =========================================================================

	/**
	 * FreshMeal user service responsible for user identity lookup.
	 */
	private final IUserService userService;

	// =========================================================================
	// Constructor
	// =========================================================================

	/**
	 * Creates the FreshMeal user-details service.
	 *
	 * @param userService FreshMeal user service.
	 */
	public UserDetailsServiceImpl(IUserService userService) {

		this.userService = userService;
	}

	// =========================================================================
	// User Details
	// =========================================================================

	/**
	 * Loads a FreshMeal user using either a username or an email address.
	 *
	 * <p>
	 * The supplied identifier is routed to the appropriate user-service lookup
	 * operation. The resulting {@link UserEntity} is then converted into a
	 * {@link UserProfile} containing the user's credentials, account state, and
	 * Spring Security authorities.
	 * </p>
	 *
	 * @param identifier username or email supplied during authentication.
	 * @return Spring Security user details.
	 * @throws UsernameNotFoundException when the user cannot be resolved.
	 */
	@Override
	public UserDetails loadUserByUsernameOrUserEmail(String identifier) {

		if (identifier == null || identifier.isBlank()) {
			throw new UsernameNotFoundException(AuthenticationErrorConstants.AUTHENTICATION_FAILED);
		}

		UserEntity userEntity;

		if (isEmailIdentifier(identifier)) {
			userEntity = loadUserByEmail(identifier);
		} else {
			userEntity = loadUserByUsername(identifier);
		}

		Collection<? extends GrantedAuthority> authorities = buildAuthorities(userEntity.getRoles());

		return UserProfile.create(userEntity, authorities);
	}

	// =========================================================================
	// User Lookup
	// =========================================================================

	/**
	 * Loads a user using a username.
	 *
	 * @param username username used for authentication.
	 * @return matching active user entity.
	 * @throws UsernameNotFoundException when the user cannot be resolved.
	 */
	private UserEntity loadUserByUsername(String username) {

		UsernameRequest request = new UsernameRequest();

		request.setUsername(username);

		IServiceInput<UsernameRequest> input = new ServiceInput<>();

		input.setInput(request);

		IServiceOutput<UserEntity> output = userService.loadUserByUsername(input);

		if (output == null || output.getOutput() == null) {
			throw new UsernameNotFoundException(AuthenticationErrorConstants.AUTHENTICATION_FAILED);
		}

		return output.getOutput();
	}

	/**
	 * Loads a user using an email address.
	 *
	 * <p>
	 * The existing FreshMeal {@link EmailAddress} value object is used rather than
	 * constructing the value object directly.
	 * </p>
	 *
	 * @param email email address used for authentication.
	 * @return matching active user entity.
	 * @throws UsernameNotFoundException when the user cannot be resolved.
	 */
	private UserEntity loadUserByEmail(String email) {

		EmailRequest request = new EmailRequest();

		request.setEmail(EmailAddress.toEmailAddress(email));

		IServiceInput<EmailRequest> input = new ServiceInput<>();

		input.setInput(request);

		IServiceOutput<UserEntity> output = userService.loadUserByEmail(input);

		if (output == null || output.getOutput() == null) {
			throw new UsernameNotFoundException(AuthenticationErrorConstants.AUTHENTICATION_FAILED);
		}

		return output.getOutput();
	}

	// =========================================================================
	// Authority Mapping
	// =========================================================================

	/**
	 * Converts FreshMeal domain roles into Spring Security authorities.
	 *
	 * <p>
	 * FreshMeal roles are business-level roles. Spring Security receives the
	 * conventional {@code ROLE_} prefixed representation.
	 * </p>
	 *
	 * <pre>
	 * ADMIN            -&gt; ROLE_ADMIN
	 * USER             -&gt; ROLE_USER
	 * RESTAURANT_OWNER -&gt; ROLE_RESTAURANT_OWNER
	 * DELIVERY_PARTNER -&gt; ROLE_DELIVERY_PARTNER
	 * </pre>
	 *
	 * @param roles FreshMeal domain roles.
	 * @return immutable collection of Spring Security authorities.
	 */
	private Collection<? extends GrantedAuthority> buildAuthorities(Collection<RoleType> roles) {

		if (roles == null || roles.isEmpty()) {
			return Collections.emptyList();
		}

		return Collections.unmodifiableList(roles.stream().filter(role -> role != null).map(roleType -> roleType.name())
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList()));
	}

	// =========================================================================
	// Identifier Resolution
	// =========================================================================

	/**
	 * Determines whether the supplied authentication identifier represents an email
	 * address.
	 *
	 * <p>
	 * This method performs only identifier routing. Actual email validation remains
	 * the responsibility of the FreshMeal {@link EmailAddress} value object and
	 * user-service validation rules.
	 * </p>
	 *
	 * @param identifier authentication identifier.
	 * @return {@code true} when the identifier appears to be an email address.
	 */
	private boolean isEmailIdentifier(String identifier) {

		return identifier.contains("@");
	}
}