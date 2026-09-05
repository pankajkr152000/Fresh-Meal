package com.foodies.freshmeal.authentication.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.foodies.freshmeal.user.entity.UserProfile;
import com.foodies.freshmeal.user.service.IUserService;

public interface IUserDetailsService {
    /**
     * Loads a FreshMeal user by username.
     *
     * <p>
     * The actual user lookup is delegated to {@link IUserService} so that
     * authentication does not bypass the existing FreshMeal service and
     * repository architecture.
     * </p>
     *
     * <p>
     * After the user is loaded, the user's business roles are converted into
     * Spring Security authorities and wrapped in a {@link UserProfile}.
     * </p>
     *
     * @param username username supplied by Spring Security.
     *
     * @return Spring Security user details.
     *
     * @throws UsernameNotFoundException when the user cannot be found.
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
