package com.foodies.freshmeal.authentication.dto;

import com.foodies.freshmeal.common.valueObject.EmailAddress;
import com.foodies.freshmeal.common.valueObject.PhoneNumber;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * ============================================================================
 * Register Request
 * ============================================================================
 *
 * Request payload used by a new user to create a FreshMeal account.
 *
 * <p>
 * This DTO represents the public self-registration flow and is intentionally
 * separate from {@code UserRequest}, which belongs to the User module's
 * user-management operations.
 * </p>
 *
 * <h3>Registration Responsibilities</h3>
 *
 * <ul>
 * <li>Collect user-provided identity information.</li>
 * <li>Collect credentials required to create the account.</li>
 * <li>Validate the password confirmation at the API boundary.</li>
 * <li>Provide email information required for email OTP verification.</li>
 * </ul>
 *
 * <h3>Server-Controlled Information</h3>
 *
 * <p>
 * The client must not provide or control the following information:
 * </p>
 *
 * <ul>
 * <li>User persistence identifier</li>
 * <li>User number</li>
 * <li>User roles</li>
 * <li>Account state flags</li>
 * <li>Address associations</li>
 * <li>Audit information</li>
 * <li>Record status and soft-delete information</li>
 * <li>Email verification state</li>
 * </ul>
 *
 * <p>
 * A successfully registered account receives the default
 * {@code RoleType.USER} role and remains subject to the email OTP
 * verification process defined by the Authentication module.
 * </p>
 *
 * <p>
 * The raw password must never be persisted. It must be encoded before being
 * written to {@code UserEntity}.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public class RegisterRequest {

    /**
     * Username selected by the user.
     */
    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters.")
    private String username;

    /**
     * Raw password supplied during registration.
     *
     * <p>
     * This value is used only during registration and must never be persisted
     * in raw form.
     * </p>
     */
    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 16, message = "Password must be between 6 and 16 characters.")
    private String password;

    /**
     * Password confirmation supplied by the user.
     *
     * <p>
     * This value is used only to verify that the user entered the intended
     * password consistently and must never be persisted.
     * </p>
     */
    @NotBlank(message = "Password confirmation is required.")
    @Size(min = 6, max = 16, message = "Password confirmation must be between 6 and 16 characters.")
    private String confirmPassword;

    /**
     * User's first name.
     */
    @NotBlank(message = "First name is required.")
    @Size(max = 100, message = "First name must not exceed 100 characters.")
    private String firstName;

    /**
     * User's last name.
     */
    @Size(max = 100, message = "Last name must not exceed 100 characters.")
    private String lastName;

    @NotNull(message = "Email is required.")
    @Valid
    private EmailAddress email;

    @NotNull(message = "Phone number is required.")
    @Valid
    private PhoneNumber phoneNumber;

    /**
     * Returns the username.
     *
     * @return Username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username Username.
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Returns the raw password.
     *
     * @return Raw password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the raw password.
     *
     * @param password Raw password.
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Returns the password confirmation.
     *
     * @return Password confirmation.
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Sets the password confirmation.
     *
     * @param confirmPassword Password confirmation.
     */
    public void setConfirmPassword(final String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * Returns the first name.
     *
     * @return First name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName First name.
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name.
     *
     * @return Last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName Last name.
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the email address.
     *
     * @return Email address.
     */
    public EmailAddress getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email Email address.
     */
    public void setEmail(final EmailAddress email) {
        this.email = email;
    }

    /**
     * Returns the phone number.
     *
     * @return Phone number.
     */
    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number.
     *
     * @param phoneNumber Phone number.
     */
    public void setPhoneNumber(final PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}