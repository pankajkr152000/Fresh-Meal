package com.foodies.freshmeal.user.controller;

import org.springframework.http.ResponseEntity;

import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.user.dto.UpdateUserInputDTO;
import com.foodies.freshmeal.user.dto.UserIdRequest;
import com.foodies.freshmeal.user.dto.UserNumberRequest;
import com.foodies.freshmeal.user.dto.UserRequest;
import com.foodies.freshmeal.user.dto.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * ============================================================================
 * User Controller
 * ============================================================================
 *
 * <p>
 * Defines HTTP operations for FreshMeal user management.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Define HTTP operations for users.</li>
 * <li>Receive request data from the client.</li>
 * <li>Delegate processing to the User service implementation.</li>
 * <li>Expose the User API contract through Swagger documentation.</li>
 * </ul>
 *
 * <p>
 * The controller implementation is responsible for creating the common
 * {@code IServiceInput} structure before invoking the service layer.
 * </p>
 *
 * <h3>Security Boundary</h3>
 * <p>
 * Authentication and authorization operations are intentionally outside this
 * controller contract. Those operations will be exposed through their
 * respective modules.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Tag(name = "User", description = "APIs for user management.")
public interface IUserController {

    // =========================================================================
    // Create User
    // =========================================================================

    /**
     * Creates a new FreshMeal user.
     *
     * <p>
     * The user is created using the information supplied in the request.
     * </p>
     *
     * @param request user creation request
     *
     * @return created user information
     */
    @Operation(summary = "Add user", description = "Creates a new FreshMeal user.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User created successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user request or validation failure.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Username, email address, or phone number already exists.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<UserResponse>> addUser(UserRequest request);

    // =========================================================================
    // View User
    // =========================================================================

    /**
     * Retrieves a user using its business-facing user number.
     *
     * @param request user identifier request
     *
     * @return user information
     */
    @Operation(summary = "Get user by user number", description = "Retrieves a user using its business-facing user number.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user identifier.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<UserResponse>> getUserByUserNumber(
            UserNumberRequest request);

    // =========================================================================
    // Update User
    // =========================================================================

    /**
     * Updates editable information of an existing user.
     *
     * <p>
     * Security-sensitive information, roles, addresses, and account-state
     * information are not modified through this operation.
     * </p>
     *
     * @param request user update request
     *
     * @return updated user information
     */
    @Operation(summary = "Update user", description = "Updates editable information of an existing user.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user update request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Username, email address, or phone number already exists.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<UserResponse>> updateUser(UpdateUserInputDTO request);

    // =========================================================================
    // Delete User
    // =========================================================================

    /**
     * Performs a logical deletion of a user.
     *
     * @param request user ID request
     *
     * @return deletion result
     */
    @Operation(summary = "Delete user", description = "Performs a logical deletion of a user.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User deleted successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user identifier.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<Boolean>> deleteUser(UserIdRequest request);

    // =========================================================================
    // Enable User
    // =========================================================================

    /**
     * Enables a disabled user account.
     *
     * @param request user number request
     *
     * @return updated user information
     */
    @Operation(summary = "Enable user", description = "Enables a disabled user account.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User enabled successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "User account is already enabled.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<UserResponse>> enableUser(UserNumberRequest request);

    // =========================================================================
    // Disable User
    // =========================================================================

    /**
     * Disables an active user account.
     *
     * @param request user number request
     *
     * @return updated user information
     */
    @Operation(summary = "Disable user", description = "Disables an active user account.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User disabled successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "User account is already disabled.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<UserResponse>> disableUser(UserNumberRequest request);

    // =========================================================================
    // Lock User
    // =========================================================================

    /**
     * Locks a user account.
     *
     * @param request user number request
     *
     * @return updated user information
     */
    @Operation(summary = "Lock user", description = "Locks a user account.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User locked successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "User account is already locked.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<UserResponse>> lockUser(UserNumberRequest request);

    // =========================================================================
    // Unlock User
    // =========================================================================

    /**
     * Unlocks a locked user account.
     *
     * @param request user number request
     *
     * @return updated user information
     */
    @Operation(summary = "Unlock user", description = "Unlocks a locked user account.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User unlocked successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "User account is not locked.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<UserResponse>> unlockUser(UserNumberRequest request);

}