package com.foodies.freshmeal.authentication.token;

import com.foodies.freshmeal.common.contract.IDisplayOption;

/**
 * ============================================================================
 * Enum : TokenType
 * ============================================================================
 *
 * Defines the token purposes supported by FreshMeal authentication.
 *
 * <p>
 * FreshMeal uses separate access and refresh tokens with different
 * responsibilities and lifetimes.
 * </p>
 *
 * <h3>Token Types</h3>
 * <ul>
 * <li>{@link #ACCESS} - short-lived token used to access protected APIs.</li>
 * <li>{@link #REFRESH} - longer-lived token used only to obtain a new
 * authentication token pair.</li>
 * </ul>
 *
 * <h3>Security Boundary</h3>
 * <p>
 * The token type is embedded into the JWT so that a validly signed refresh
 * token cannot be accepted where an access token is required.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum TokenType implements IDisplayOption {

	/**
	 * Short-lived token used for protected API requests.
	 */
	ACCESS("ACCESS"),

	/**
	 * Long-lived token used to obtain new authentication tokens.
	 */
	REFRESH("REFRESH");

	/**
	 * User-friendly display label.
	 */
	private final String displayName;

	/**
	 * Creates a delivery status.
	 *
	 * @param displayName user-friendly display label
	 */
	TokenType(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return displayName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return name();
	}
}
