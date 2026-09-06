package com.foodies.freshmeal.user.service;

import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.user.dto.EmailRequest;
import com.foodies.freshmeal.user.dto.UpdatePasswordInputDTO;
import com.foodies.freshmeal.user.dto.UpdateUserInputDTO;
import com.foodies.freshmeal.user.dto.UserIdRequest;
import com.foodies.freshmeal.user.dto.UserInputDTO;
import com.foodies.freshmeal.user.dto.UserNumberRequest;
import com.foodies.freshmeal.user.dto.UserResponse;
import com.foodies.freshmeal.user.dto.UsernameRequest;
import com.foodies.freshmeal.user.entity.UserEntity;

/**
 * =============================================================================
 * Service : IUserService
 * =============================================================================
 *
 * Purpose
 * -------
 * Defines the business operations supported by the User module.
 *
 * <p>
 * This interface represents the primary service-layer contract for the
 * lifecycle of {@link UserEntity}. It follows the standard FreshMeal service
 * architecture by using {@link IServiceInput} and {@link IServiceOutput}
 * for communication between the controller and service layers.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Load users using their internal persistence identifier.</li>
 * <li>Load users using their business-facing user number.</li>
 * <li>Load users using their username.</li>
 * <li>Generate internal and business user identifiers.</li>
 * <li>Create and initialize new user entities.</li>
 * <li>Add new users.</li>
 * <li>Update existing users.</li>
 * <li>Delete users.</li>
 * <li>Manage user account lifecycle state.</li>
 * </ul>
 *
 * <h3>Business Meaning</h3>
 * <p>
 * {@code IUserService} owns the normal business lifecycle of a user.
 * Authentication-specific responsibilities such as login, logout, password
 * verification, password changes, OTP processing, JWT generation, token
 * refresh, and Spring Security authentication flow belong to the dedicated
 * authentication/security layer.
 * </p>
 *
 * <h3>Identifier Convention</h3>
 * <ul>
 * <li>
 * <b>Internal ID</b> - MongoDB persistence identifier represented by
 * {@link UserIdRequest}.
 * </li>
 * <li>
 * <b>User Number</b> - FreshMeal business identifier represented by
 * {@link UserNumberRequest}.
 * </li>
 * <li>
 * <b>Username</b> - Login/business username represented by
 * {@link UsernameRequest}.
 * </li>
 * </ul>
 *
 * <h3>Separation of Responsibilities</h3>
 * <p>
 * User addresses are managed by {@code IAddressService}. Authentication and
 * authorization are intentionally kept outside this service so that the User
 * domain remains independent from the security implementation.
 * </p>
 *
 * =============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IUserService {

    /**
     * Loads a user using its internal persistence identifier.
     *
     * @param input service input containing {@link UserIdRequest}
     * @return service output containing the matching {@link UserEntity}
     */
    IServiceOutput<UserEntity> loadUser(
            IServiceInput<UserIdRequest> input);

    /**
     * Loads a user using its business-facing user number.
     *
     * @param input service input containing {@link UserNumberRequest}
     * @return service output containing the matching {@link UserEntity}
     */
    IServiceOutput<UserEntity> loadUserByUserNumber(
            IServiceInput<UserNumberRequest> input);

    /**
     * Loads a user using its username.
     *
     * <p>
     * This operation is particularly useful for authentication-related user
     * lookup while keeping persistence access behind the User service layer.
     * </p>
     *
     * @param input service input containing {@link UsernameRequest}
     * @return service output containing the matching {@link UserEntity}
     */
    IServiceOutput<UserEntity> loadUserByUsername(
            IServiceInput<UsernameRequest> input);

    IServiceOutput<UserEntity> loadUserByEmail(
            IServiceInput<EmailRequest> input);

    /**
     * Generates the internal persistence identifier for a user.
     *
     * @param input service input containing {@link UserInputDTO}
     * @return service output containing the generated identifier
     */
    IServiceOutput<String> generateUserId(
            IServiceInput<UserInputDTO> input);

    /**
     * Generates the business-facing user number.
     *
     * @param input service input containing {@link UserInputDTO}
     * @return service output containing the generated user number
     */
    IServiceOutput<String> generateUserNumber(
            IServiceInput<UserInputDTO> input);

    /**
     * Creates and initializes a new {@link UserEntity}.
     *
     * @param input service input containing {@link UserInputDTO}
     * @return service output containing the newly created entity
     */
    IServiceOutput<UserEntity> createUserEntity(
            IServiceInput<UserInputDTO> input);

    /**
     * Adds a new user to the FreshMeal system.
     *
     * @param input service input containing {@link UserInputDTO}
     * @return service output containing the created {@link UserResponse}
     */
    IServiceOutput<UserResponse> addUser(
            IServiceInput<UserInputDTO> input);

    IServiceOutput<UserEntity> registerUser(
            IServiceInput<UserInputDTO> input);

    /**
     * Updates an existing user.
     *
     * @param input service input containing the user update information
     * @return service output containing the updated {@link UserResponse}
     */
    IServiceOutput<UserResponse> updateUser(
            IServiceInput<UpdateUserInputDTO> input);

    /**
     * Deletes an existing user using its internal persistence identifier.
     *
     * @param input service input containing {@link UserIdRequest}
     * @return service output indicating completion of the operation
     */
    IServiceOutput<Boolean> deleteUser(
            IServiceInput<UserIdRequest> input);

    /**
     * Enables a user account.
     *
     * @param input service input containing {@link UserNumberRequest}
     * @return service output containing the updated {@link UserResponse}
     */
    IServiceOutput<UserResponse> enableUser(
            IServiceInput<UserNumberRequest> input);

    /**
     * Disables a user account.
     *
     * @param input service input containing {@link UserNumberRequest}
     * @return service output containing the updated {@link UserResponse}
     */
    IServiceOutput<UserResponse> disableUser(
            IServiceInput<UserNumberRequest> input);

    /**
     * Locks a user account.
     *
     * @param input service input containing {@link UserNumberRequest}
     * @return service output containing the updated {@link UserResponse}
     */
    IServiceOutput<UserResponse> lockUser(
            IServiceInput<UserNumberRequest> input);

    /**
     * Unlocks a user account.
     *
     * @param input service input containing {@link UserNumberRequest}
     * @return service output containing the updated {@link UserResponse}
     */
    IServiceOutput<UserResponse> unlockUser(
            IServiceInput<UserNumberRequest> input);

    /**
     * =================================================================================================
     * REGISTER USER
     * =================================================================================================
     *
     * <p>
     * Registers a self-service user using a password that has already been encoded
     * by the Authentication module.
     * </p>
     *
     * <p>
     * The Authentication module owns password validation and password encoding.
     * The User module owns user creation and persistence.
     * </p>
     *
     * <p>
     * Self-registered users are initially created with email verification pending
     * and the account disabled until the authentication workflow completes email
     * verification.
     * </p>
     *
     * @param input
     *                        service input containing the user registration
     *                        information
     * @param encodedPassword
     *                        password already encoded by the Authentication module
     * @return service output containing the persisted {@link UserEntity}
     */
    IServiceOutput<UserEntity> registerUser(IServiceInput<UserInputDTO> input, String encodedPassword);

    /**
     * =================================================================================================
     * ACTIVATE USER
     * =================================================================================================
     *
     * <p>
     * Activates a user account after the required email verification process has
     * been successfully completed.
     * </p>
     *
     * <p>
     * Activation marks the user's email address as verified and enables the account
     * for authentication.
     * </p>
     *
     * @param input
     *              service input containing the user business identifier
     * @return service output containing the activated {@link UserEntity}
     */
    IServiceOutput<UserEntity> activateUser(IServiceInput<UserNumberRequest> input);

    /**
     * =========================================================================
     * Password Management
     * =========================================================================
     */

    /**
     * Updates the encoded password of an existing user.
     *
     * <p>
     * Password modification is intentionally separated from the general
     * {@code updateUser(...)} operation because password is a sensitive
     * security field.
     * </p>
     *
     * @param input password update input
     * @return {@link UserResponse} representing the updated user
     */
    IServiceOutput<UserResponse> updatePassword(IServiceInput<UpdatePasswordInputDTO> input);

}