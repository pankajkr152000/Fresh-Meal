package com.foodies.freshmeal.user.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodies.freshmeal.common.audit.annotation.AuditApi;
import com.foodies.freshmeal.common.builder.ApiResponseBuilder;
import com.foodies.freshmeal.common.constants.ActionType;
import com.foodies.freshmeal.common.constants.ApiBaseConstants;
import com.foodies.freshmeal.common.constants.ApiMessageConstants;
import com.foodies.freshmeal.common.constants.AuthorizationConstants;
import com.foodies.freshmeal.common.constants.MethodType;
import com.foodies.freshmeal.common.constants.ModuleType;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.user.constants.UserApiConstants;
import com.foodies.freshmeal.user.controller.IUserController;
import com.foodies.freshmeal.user.dto.UpdateUserInputDTO;
import com.foodies.freshmeal.user.dto.UserIdRequest;
import com.foodies.freshmeal.user.dto.UserInputDTO;
import com.foodies.freshmeal.user.dto.UserNumberRequest;
import com.foodies.freshmeal.user.dto.UserRequest;
import com.foodies.freshmeal.user.dto.UserResponse;
import com.foodies.freshmeal.user.entity.UserEntity;
import com.foodies.freshmeal.user.service.IUserService;

/**
 * ============================================================================
 * User Controller
 * ============================================================================
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Receive HTTP requests.</li>
 * <li>Prepare {@code IServiceInput} objects.</li>
 * <li>Delegate business processing to the User service layer.</li>
 * <li>Return standardized {@link ApiResponse} responses.</li>
 * </ul>
 *
 * <p>
 * The controller contains no business logic. All User business rules and
 * persistence operations are delegated to {@link IUserService}.
 * </p>
 *
 * <p>
 * Base URL: {@code /api/users}
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@RestController
@RequestMapping(ApiBaseConstants.USER_BASE_URL)
public class UserController implements IUserController {

    /**
     * User service.
     */
    private final IUserService userService;

    /**
     * Current service context.
     */
    private final IServiceContext serviceContext;

    /**
     * Creates a UserController.
     *
     * @param userService    User service
     * @param serviceContext current service context
     */
    public UserController(
            IUserService userService,
            IServiceContext serviceContext) {

        this.userService = userService;
        this.serviceContext = serviceContext;
    }

    // =========================================================================
    // Create User
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_ONLY)
    @AuditApi(action = ActionType.ADD_USER, module = ModuleType.USER, method = MethodType.CREATE)
    @PostMapping(UserApiConstants.ADD)
    public ResponseEntity<ApiResponse<UserResponse>> addUser(
            @RequestBody UserRequest request) {

        IServiceInput<UserInputDTO> input = new ServiceInput<>();

        input.setServiceContext(serviceContext);

        UserInputDTO userInputDTO = new UserInputDTO();

        userInputDTO.setUserRequest(request);

        input.setInput(userInputDTO);

        IServiceOutput<UserResponse> output = userService.addUser(input);

        return ApiResponseBuilder.created(
                ApiMessageConstants.USER_CREATED,
                output.getOutput());
    }

    // =========================================================================
    // View User
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_OR_SELF)
    @PostMapping(UserApiConstants.GET_BY_USER_NUMBER)
    public ResponseEntity<ApiResponse<UserResponse>> getUserByUserNumber(
            @RequestBody UserNumberRequest request) {

        IServiceInput<UserNumberRequest> input = new ServiceInput<>();

        input.setServiceContext(serviceContext);

        input.setInput(request);

        IServiceOutput<UserEntity> output = userService.loadUserByUserNumber(input);

        UserEntity userEntity = output.getOutput();

        UserResponse response = toUserResponse(userEntity);

        return ApiResponseBuilder.success(
                ApiMessageConstants.USER_FOUND,
                response);
    }

    // =========================================================================
    // Update User
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_OR_SELF)
    @PostMapping(UserApiConstants.UPDATE)
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @RequestBody UpdateUserInputDTO request) {

        IServiceInput<UpdateUserInputDTO> input = new ServiceInput<>();

        input.setServiceContext(serviceContext);

        input.setInput(request);

        IServiceOutput<UserResponse> output = userService.updateUser(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.USER_UPDATED,
                output.getOutput());
    }

    // =========================================================================
    // Delete User
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_ONLY)
    @PostMapping(UserApiConstants.DELETE)
    public ResponseEntity<ApiResponse<Boolean>> deleteUser(
            @RequestBody UserIdRequest request) {

        IServiceInput<UserIdRequest> input = new ServiceInput<>();

        input.setServiceContext(serviceContext);

        input.setInput(request);

        IServiceOutput<Boolean> output = userService.deleteUser(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.USER_DELETED,
                output.getOutput());
    }

    // =========================================================================
    // Enable User
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_ONLY)
    @PostMapping(UserApiConstants.ENABLE)
    public ResponseEntity<ApiResponse<UserResponse>> enableUser(
            @RequestBody UserNumberRequest request) {

        IServiceInput<UserNumberRequest> input = new ServiceInput<>();

        input.setServiceContext(serviceContext);

        input.setInput(request);

        IServiceOutput<UserResponse> output = userService.enableUser(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.USER_ENABLED,
                output.getOutput());
    }

    // =========================================================================
    // Disable User
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_ONLY)
    @PostMapping(UserApiConstants.DISABLE)
    public ResponseEntity<ApiResponse<UserResponse>> disableUser(
            @RequestBody UserNumberRequest request) {

        IServiceInput<UserNumberRequest> input = new ServiceInput<>();

        input.setServiceContext(serviceContext);

        input.setInput(request);

        IServiceOutput<UserResponse> output = userService.disableUser(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.USER_DISABLED,
                output.getOutput());
    }

    // =========================================================================
    // Lock User
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    @PostMapping(UserApiConstants.LOCK)
    @PreAuthorize(AuthorizationConstants.ADMIN_ONLY)
    public ResponseEntity<ApiResponse<UserResponse>> lockUser(
            @RequestBody UserNumberRequest request) {

        IServiceInput<UserNumberRequest> input = new ServiceInput<>();

        input.setServiceContext(serviceContext);

        input.setInput(request);

        IServiceOutput<UserResponse> output = userService.lockUser(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.USER_LOCKED,
                output.getOutput());
    }

    // =========================================================================
    // Unlock User
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_ONLY)
    @PostMapping(UserApiConstants.UNLOCK)
    public ResponseEntity<ApiResponse<UserResponse>> unlockUser(
            @RequestBody UserNumberRequest request) {

        IServiceInput<UserNumberRequest> input = new ServiceInput<>();

        input.setServiceContext(serviceContext);

        input.setInput(request);

        IServiceOutput<UserResponse> output = userService.unlockUser(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.USER_UNLOCKED,
                output.getOutput());
    }

    // =========================================================================
    // Private Helper Methods
    // =========================================================================

    /**
     * Converts a UserEntity into a UserResponse.
     *
     * @param userEntity user entity
     * @return user response
     */
    private UserResponse toUserResponse(UserEntity userEntity) {

        UserResponse response = new UserResponse();

        response.setUserNumber(userEntity.getUserNumber());
        response.setUsername(userEntity.getUsername());
        response.setFirstName(userEntity.getFirstName());
        response.setLastName(userEntity.getLastName());
        response.setEmail(userEntity.getEmail());
        response.setPhoneNumber(userEntity.getPhoneNumber());
        response.setAddressNumbers(userEntity.getAddressNumbers());
        response.setRoles(userEntity.getRoles());

        return response;
    }
}