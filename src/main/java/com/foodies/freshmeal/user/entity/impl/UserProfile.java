package com.foodies.freshmeal.user.entity.impl;

import java.util.Collection;
import java.util.Collections;

import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.common.entity.ILoginHistory;
import com.foodies.freshmeal.image.entity.impl.ImageEntity;
import com.foodies.freshmeal.user.entity.IUserEntity;
import com.foodies.freshmeal.user.entity.IUserProfile;

import lombok.Getter;
import lombok.Setter;

/**
 * =====================================================
 * User Profile Entity
 * =====================================================
 *
 * MongoDB collection for application users.
 *
 * Handles:
 * - Authentication
 * - Authorization
 * - Login History
 * - User Metadata
 *
 * =====================================================
 */
@Getter
@Setter
@Document(collection = "fm_user_profiles")
public class UserProfile implements IUserProfile {

	/**
	 *
	 */
	private static final long serialVersionUID = 4377183688839034087L;

	/**
	 * =====================================================
	 * Constructor
	 * =====================================================
	 *
	 * Package-private constructor.
	 * Entity creation should happen only through factory.
	 *
	 * =====================================================
	 */
	UserProfile() {

	}

	/**
	 * =====================================================
	 * Factory Method
	 * =====================================================
	 */
	public static IEntity create() {
		return new UserProfile();
	}

	/**
	 * =====================================================
	 * Mongo Primary Key
	 * =====================================================
	 */
	@Id
	private String id;

	/**
	 * =====================================================
	 * Authentication Fields
	 * =====================================================
	 */
	private String userName;

	private String password;

	private boolean authenticated;

	/**
	 * =====================================================
	 * User Details
	 * =====================================================
	 */
	private IUserEntity userEntity;

	private ILoginHistory loginHistory;
	
	private ImageEntity profileImage;

	/**
	 * =====================================================
	 * Roles / Authorities
	 * =====================================================
	 */
	private Collection<? extends GrantedAuthority> authorities = Collections.emptyList();

	/**
	 * =====================================================
	 * IEntity
	 * =====================================================
	 */
	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * =====================================================
	 * Authentication
	 * =====================================================
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return authorities;
	}

	@Override
	public @Nullable Object getCredentials() {

		return password;
	}

	@Override
	public @Nullable Object getDetails() {

		return userEntity;
	}

	@Override
	public @Nullable Object getPrincipal() {

		return this;
	}

	@Override
	public boolean isAuthenticated() {

		return authenticated;
	}

	@Override
	public void setAuthenticated(
			boolean isAuthenticated)
			throws IllegalArgumentException {

		this.authenticated = isAuthenticated;
	}

	@Override
	public String getName() {

		return userName;
	}

	@Override
	public void eraseCredentials() {

		this.password = null;
	}

	/**
	 * =====================================================
	 * UserDetails
	 * =====================================================
	 */
	@Override
	public @Nullable String getPassword() {

		return password;
	}

	@Override
	public String getUsername() {

		return userName;
	}

	/**
	 * =====================================================
	 * Login History
	 * =====================================================
	 */
	@Override
	public ILoginHistory getLoginHistory() {

		return loginHistory;
	}

	@Override
	public IUserEntity getUserEntity() {

		return userEntity;
	}

	@Override
	public void setLoginHistory(ILoginHistory loginHistory) {

		this.loginHistory = loginHistory;
	}

	@Override
	public void setUserEntity(IUserEntity userEntity) {

		this.userEntity = userEntity;
	}

	public ImageEntity getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(ImageEntity profileImage) {
		this.profileImage = profileImage;
	}
	
	
	
	
}