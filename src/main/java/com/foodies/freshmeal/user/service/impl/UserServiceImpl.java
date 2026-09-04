package com.foodies.freshmeal.user.service.impl;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.foodies.freshmeal.common.constants.RoleType;
import com.foodies.freshmeal.common.constants.SequenceConstants;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.enums.EntityName;
import com.foodies.freshmeal.common.exception.BusinessException;
import com.foodies.freshmeal.common.exception.CommonErrorConstants;
import com.foodies.freshmeal.common.exception.ResourceNotFoundException;
import com.foodies.freshmeal.common.factory.EntityFactory;
import com.foodies.freshmeal.common.io.DataContext;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.RepositoryContext;
import com.foodies.freshmeal.common.io.service.impl.ServiceOutput;
import com.foodies.freshmeal.common.sequence.service.IDatabaseSequenceService;
import com.foodies.freshmeal.user.constants.UserErrorConstants;
import com.foodies.freshmeal.user.dto.UpdateUserInputDTO;
import com.foodies.freshmeal.user.dto.UserIdRequest;
import com.foodies.freshmeal.user.dto.UserInputDTO;
import com.foodies.freshmeal.user.dto.UserNumberRequest;
import com.foodies.freshmeal.user.dto.UserRequest;
import com.foodies.freshmeal.user.dto.UserResponse;
import com.foodies.freshmeal.user.dto.UsernameRequest;
import com.foodies.freshmeal.user.entity.UserEntity;
import com.foodies.freshmeal.user.repository.IUserRepository;
import com.foodies.freshmeal.user.service.IUserService;

/**
 * =============================================================================
 * User Service Implementation
 * =============================================================================
 *
 * Purpose ------- Provides the business implementation for the FreshMeal User
 * module.
 *
 * <p>
 * This service follows the standard FreshMeal service architecture based on
 * {@link IServiceInput} and {@link IServiceOutput}. Persistence operations are
 * delegated to {@link IUserRepository}, while identifier generation is handled
 * by {@link IDatabaseSequenceService}.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Load active users.</li>
 * <li>Generate UserEntity identifiers.</li>
 * <li>Generate business-facing user numbers.</li>
 * <li>Create and persist UserEntity instances.</li>
 * <li>Update editable user information.</li>
 * <li>Perform logical user deletion.</li>
 * <li>Manage user account lifecycle flags.</li>
 * <li>Convert UserEntity instances into UserResponse objects.</li>
 * </ul>
 *
 * <h3>Security Boundary</h3>
 * <p>
 * Authentication-specific operations such as password verification, password
 * encoding, login, logout, OTP processing, JWT generation and token refresh are
 * intentionally outside this service.
 * </p>
 *
 * <h3>Persistence Boundary</h3>
 * <p>
 * User reads use the active-record repository operations so logically deleted
 * users are not accidentally returned as normal users.
 * </p>
 *
 * =============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Service
public class UserServiceImpl implements IUserService {

    /**
     * Database sequence service.
     */
    private final IDatabaseSequenceService databaseSequenceService;

    /**
     * User repository.
     */
    private final IUserRepository userRepository;

    /**
     * Current service context.
     */
    private final IServiceContext serviceContext;

    /**
     * Creates a UserServiceImpl.
     *
     * @param databaseSequenceService database sequence service
     * @param userRepository          user repository
     * @param serviceContext          current service context
     */
    public UserServiceImpl(IDatabaseSequenceService databaseSequenceService, IUserRepository userRepository,
            IServiceContext serviceContext) {

        this.databaseSequenceService = databaseSequenceService;
        this.userRepository = userRepository;
        this.serviceContext = serviceContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<UserEntity> loadUser(IServiceInput<UserIdRequest> input) {

        UserIdRequest userIdRequest = input.getInput();

        UserEntity userEntity = userRepository.findActiveById(userIdRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(CommonErrorConstants.RESOURCE_NOT_FOUND));

        return new ServiceOutput<>(userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<UserEntity> loadUserByUserNumber(IServiceInput<UserNumberRequest> input) {

        UserNumberRequest userNumberRequest = input.getInput();

        Query query = Query.query(Criteria.where("userNumber").is(userNumberRequest.getUserNumber()));

        UserEntity userEntity = userRepository.findOne(query)
                .orElseThrow(() -> new ResourceNotFoundException(CommonErrorConstants.RESOURCE_NOT_FOUND));

        return new ServiceOutput<>(userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<UserEntity> loadUserByUsername(IServiceInput<UsernameRequest> input) {

        UsernameRequest usernameRequest = input.getInput();

        Query query = Query.query(Criteria.where("username").is(usernameRequest.getUsername()));

        UserEntity userEntity = userRepository.findOne(query)
                .orElseThrow(() -> new ResourceNotFoundException(CommonErrorConstants.RESOURCE_NOT_FOUND));

        return new ServiceOutput<>(userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<String> generateUserId(IServiceInput<UserInputDTO> input) {

        long sequence = databaseSequenceService.generateSequence(input.getServiceContext(),
                SequenceConstants.USER_ENTITY_SEQUENCE);

        IServiceContext inputServiceContext = input.getServiceContext();

        inputServiceContext.setAttribute(DataContext.USER_SEQUENCE, sequence);

        String userId = String.format(SequenceConstants.USER_DB_ID_PATTERN, sequence);

        IServiceOutput<String> output = new ServiceOutput<>();
        output.setOutput(userId);

        return output;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<String> generateUserNumber(IServiceInput<UserInputDTO> input) {

        long sequence;

        if (input.getServiceContext().hasAttribute(DataContext.USER_SEQUENCE)) {

            sequence = (Long) input.getServiceContext().getAttribute(DataContext.USER_SEQUENCE);

        } else {

            sequence = databaseSequenceService.getCurrentSequence(input.getServiceContext(),
                    SequenceConstants.USER_ENTITY_SEQUENCE);
        }

        String userNumber = String.format(SequenceConstants.USER_NUMBER_PATTERN, sequence);

        IServiceOutput<String> output = new ServiceOutput<>();
        output.setOutput(userNumber);

        return output;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<UserEntity> createUserEntity(IServiceInput<UserInputDTO> input) {

        UserInputDTO userInputDTO = input.getInput();
        UserRequest userRequest = userInputDTO.getUserRequest();

        UserEntity userEntity = (UserEntity) EntityFactory.createEntity(EntityName.USER_ENTITY);

        /*
         * Generate internal UserEntity ID.
         */
        String userId = generateUserId(input).getOutput();

        userEntity.setId(userId);

        /*
         * Generate business-facing user number.
         *
         * The same sequence used for the internal ID is reused so both identifiers
         * belong to the same User sequence.
         */
        String userNumber = generateUserNumber(input).getOutput();

        userEntity.setUserNumber(userNumber);

        /*
         * Copy user information supplied by the client.
         */
        userEntity.setUsername(userRequest.getUsername());
        userEntity.setFirstName(userRequest.getFirstName());
        userEntity.setLastName(userRequest.getLastName());
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setPhoneNumber(userRequest.getPhoneNumber());

        /*
         * A newly registered user receives the normal USER domain role.
         *
         * Elevated roles must never be accepted directly from a normal registration
         * request.
         */
        userEntity.setRoles(List.of(RoleType.USER));

        /*
         * New accounts are active by default.
         *
         * Authentication credentials are intentionally not handled here.
         */
        userEntity.setAccountNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setCredentialsNonExpired(true);
        userEntity.setEnabled(true);

        /*
         * Populate creation audit information.
         */
        userEntity.setCreatedAt(AppCalendar.getBusinessLocalDateTime());

        if (serviceContext.getUserProfile() != null) {

            userEntity.setCreatedBy(serviceContext.getUserProfile().getUserNumber());

        } else {

            userEntity.setCreatedBy(RoleType.ADMIN.getLabel());
        }

        return new ServiceOutput<>(userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<UserResponse> addUser(IServiceInput<UserInputDTO> input) {

        UserInputDTO userInputDTO = input.getInput();
        UserRequest userRequest = userInputDTO.getUserRequest();

        /*
         * Validate unique username before creating the entity.
         */
        ensureUsernameAvailable(userRequest.getUsername(), null);

        /*
         * Validate unique email before creating the entity.
         */
        ensureEmailAvailable(userRequest.getEmail(), null);

        /*
         * Validate unique phone number before creating the entity.
         */
        ensurePhoneNumberAvailable(userRequest.getPhoneNumber(), null);

        UserEntity userEntity = createUserEntity(input).getOutput();

        userEntity = userRepository.save(userEntity);

        UserResponse response = toUserResponse(userEntity);

        return new ServiceOutput<>(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<UserResponse> updateUser(IServiceInput<UpdateUserInputDTO> input) {

        UpdateUserInputDTO updateInput = (UpdateUserInputDTO) input.getInput();

        UserEntity userEntity = userRepository
                .findOne(Query.query(Criteria.where("userNumber").is(updateInput.getUserNumber())))
                .orElseThrow(() -> new ResourceNotFoundException(CommonErrorConstants.RESOURCE_NOT_FOUND));

        UserRequest userRequest = updateInput.getUserRequest();

        /*
         * Validate uniqueness while excluding the current user.
         */
        ensureUsernameAvailable(userRequest.getUsername(), userEntity.getId());

        ensureEmailAvailable(userRequest.getEmail(), userEntity.getId());

        ensurePhoneNumberAvailable(userRequest.getPhoneNumber(), userEntity.getId());

        /*
         * Update only fields owned by UserRequest.
         *
         * Sensitive/security fields, roles, addresses and account-state fields are
         * deliberately not modified through this operation.
         */
        userEntity.setUsername(userRequest.getUsername());

        userEntity.setFirstName(userRequest.getFirstName());

        userEntity.setLastName(userRequest.getLastName());

        userEntity.setEmail(userRequest.getEmail());

        userEntity.setPhoneNumber(userRequest.getPhoneNumber());

        userEntity.setUpdatedAt(AppCalendar.getBusinessLocalDateTime());

        if (serviceContext.getUserProfile() != null) {

            userEntity.setUpdatedBy(serviceContext.getUserProfile().getUserNumber());

        } else {

            userEntity.setUpdatedBy(RoleType.ADMIN.getLabel());
        }

        userEntity = userRepository.save(userEntity);

        return new ServiceOutput<>(toUserResponse(userEntity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<Boolean> deleteUser(IServiceInput<UserIdRequest> input) {

        UserIdRequest userIdRequest = input.getInput();

        RepositoryContext repositoryContext = buildRepositoryContext();

        userRepository.softDelete(userIdRequest.getUserId(), repositoryContext);

        return new ServiceOutput<>(Boolean.TRUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<UserResponse> enableUser(IServiceInput<UserNumberRequest> input) {

        UserEntity userEntity = loadUserByUserNumber(input).getOutput();

        if (userEntity.isEnabled()) {

            throw new BusinessException(UserErrorConstants.ACCOUNT_ALREADY_ENABLED);
        }

        userEntity.setEnabled(true);

        return saveAccountState(userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<UserResponse> disableUser(IServiceInput<UserNumberRequest> input) {

        UserEntity userEntity = loadUserByUserNumber(input).getOutput();

        if (!userEntity.isEnabled()) {

            throw new BusinessException(UserErrorConstants.ACCOUNT_ALREADY_DISABLED);
        }

        userEntity.setEnabled(false);

        return saveAccountState(userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<UserResponse> lockUser(IServiceInput<UserNumberRequest> input) {

        UserEntity userEntity = loadUserByUserNumber(input).getOutput();

        if (!userEntity.isAccountNonLocked()) {

            throw new BusinessException(UserErrorConstants.ACCOUNT_ALREADY_LOCKED);
        }

        userEntity.setAccountNonLocked(false);

        return saveAccountState(userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<UserResponse> unlockUser(IServiceInput<UserNumberRequest> input) {

        UserEntity userEntity = loadUserByUserNumber(input).getOutput();

        if (userEntity.isAccountNonLocked()) {
            throw new BusinessException(UserErrorConstants.ACCOUNT_NOT_LOCKED);
        }

        userEntity.setAccountNonLocked(true);

        return saveAccountState(userEntity);
    }

    // =========================================================================
    // Private Business Helper Methods
    // =========================================================================

    /**
     * Validates username uniqueness.
     *
     * @param username       username to validate
     * @param excludedUserId user ID to exclude during update
     */
    private void ensureUsernameAvailable(final String username, final String excludedUserId) {

        Criteria criteria = Criteria.where("username").is(username);

        if (excludedUserId != null) {
            criteria = criteria.and("id").ne(excludedUserId);
        }

        if (userRepository.exists(Query.query(criteria))) {

            throw new BusinessException(UserErrorConstants.USERNAME_ALREADY_EXISTS);
        }
    }

    /**
     * Validates email uniqueness.
     *
     * @param email          email to validate
     * @param excludedUserId user ID to exclude during update
     */
    private void ensureEmailAvailable(final Object email, final String excludedUserId) {

        if (email == null) {
            return;
        }

        Criteria criteria = Criteria.where("email").is(email);

        if (excludedUserId != null) {
            criteria = criteria.and("id").ne(excludedUserId);
        }

        if (userRepository.exists(Query.query(criteria))) {

            throw new BusinessException(UserErrorConstants.EMAIL_ALREADY_EXISTS);
        }
    }

    /**
     * Validates phone-number uniqueness.
     *
     * @param phoneNumber    phone number to validate
     * @param excludedUserId user ID to exclude during update
     */
    private void ensurePhoneNumberAvailable(final Object phoneNumber, final String excludedUserId) {

        if (phoneNumber == null) {
            return;
        }

        Criteria criteria = Criteria.where("phoneNumber").is(phoneNumber);

        if (excludedUserId != null) {
            criteria = criteria.and("id").ne(excludedUserId);
        }

        if (userRepository.exists(Query.query(criteria))) {
            throw new BusinessException(UserErrorConstants.PHONE_NUMBER_ALREADY_EXISTS);
        }
    }

    /**
     * Saves an account-state change and updates audit information.
     *
     * @param userEntity modified user entity
     * @return updated user response
     */
    private IServiceOutput<UserResponse> saveAccountState(UserEntity userEntity) {

        userEntity.setUpdatedAt(AppCalendar.getBusinessLocalDateTime());

        if (serviceContext.getUserProfile() != null) {

            userEntity.setUpdatedBy(serviceContext.getUserProfile().getUserNumber());

        } else {

            userEntity.setUpdatedBy(RoleType.ADMIN.getLabel());
        }

        userEntity = userRepository.save(userEntity);

        return new ServiceOutput<>(toUserResponse(userEntity));
    }

    /**
     * Creates the repository context required by persistence operations.
     *
     * @return repository context
     */
    private RepositoryContext buildRepositoryContext() {

        String currentUser = RoleType.ADMIN.getLabel();

        if (serviceContext.getUserProfile() != null) {

            currentUser = serviceContext.getUserProfile().getUserNumber();
        }

        return RepositoryContext.of(currentUser, AppCalendar.getBusinessLocalDateTime());
    }

    /**
     * Converts a UserEntity into UserResponse.
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