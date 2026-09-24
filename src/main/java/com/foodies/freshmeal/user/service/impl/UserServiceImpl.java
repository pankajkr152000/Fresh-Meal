package com.foodies.freshmeal.user.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import com.foodies.freshmeal.common.io.IDataContext;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.RepositoryContext;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.common.io.service.impl.ServiceOutput;
import com.foodies.freshmeal.common.sequence.service.IDatabaseSequenceService;
import com.foodies.freshmeal.common.util.FreshMealUtilities;
import com.foodies.freshmeal.common.valueObject.EmailAddress;
import com.foodies.freshmeal.user.constants.UserErrorConstants;
import com.foodies.freshmeal.user.dto.EmailRequest;
import com.foodies.freshmeal.user.dto.UpdatePasswordInputDTO;
import com.foodies.freshmeal.user.dto.UpdateUserInputDTO;
import com.foodies.freshmeal.user.dto.UserIdRequest;
import com.foodies.freshmeal.user.dto.UserInputDTO;
import com.foodies.freshmeal.user.dto.UserNumberRequest;
import com.foodies.freshmeal.user.dto.UserRegistrationInputDTO;
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

        String normalizedEmail = FreshMealUtilities.normalizeEmail(userRequest.getEmail().getValue());

        userEntity.setEmail(EmailAddress.builder().value(normalizedEmail).build());

        userEntity.setPhoneNumber(userRequest.getPhoneNumber());

        /*
         * A newly registered user receives the normal USER domain role.
         *
         * Elevated roles must never be accepted directly from a normal registration
         * request.
         */
        // user role will be set in register user only
        // userEntity.setRoles(List.of(RoleType.USER));

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
    public IServiceOutput<UserResponse> updatePassword(final IServiceInput<UpdatePasswordInputDTO> input) {

        final UpdatePasswordInputDTO updatePasswordInput = input.getInput();

        final UserEntity userEntity = userRepository
                .findOne(Query.query(Criteria.where("userNumber").is(updatePasswordInput.getUserNumber())))
                .orElseThrow(() -> new ResourceNotFoundException(CommonErrorConstants.RESOURCE_NOT_FOUND));

        userEntity.setPassword(updatePasswordInput.getEncodedPassword());

        userEntity.setUpdatedAt(AppCalendar.getBusinessLocalDateTime());

        if (serviceContext.getUserProfile() != null) {
            userEntity.setUpdatedBy(serviceContext.getUserProfile().getUserNumber());
        } else {
            userEntity.setUpdatedBy(RoleType.ADMIN.getLabel());
        }

        userRepository.save(userEntity);

        return new ServiceOutput<>(toUserResponse(userEntity));
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

        String normalizedEmail = FreshMealUtilities.normalizeEmail(userRequest.getEmail().getValue());

        userEntity.setEmail(EmailAddress.builder().value(normalizedEmail).build());

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

        String normalizedEmail = FreshMealUtilities.normalizeEmail(userEntity.getEmail().getValue());

        response.setEmail(EmailAddress.builder().value(normalizedEmail).build());

        response.setPhoneNumber(userEntity.getPhoneNumber());

        response.setAddressNumbers(userEntity.getAddressNumbers());

        response.setRoles(userEntity.getRoles());

        return response;
    }

    @Override
    public IServiceOutput<UserEntity> loadUserByEmail(IServiceInput<EmailRequest> input) {

        EmailRequest request = input.getInput();

        if (request == null || request.getEmail() == null
                || !FreshMealUtilities.hasText(request.getEmail().getValue())) {

            throw new BusinessException(UserErrorConstants.EMAIL_REQUIRED);
        }

        String email = FreshMealUtilities.normalizeEmail(request.getEmail().getValue());

        Query query = Query.query(Criteria.where("email.value").is(email));

        UserEntity userEntity = userRepository.findOne(query)
                .orElseThrow(() -> new BusinessException(UserErrorConstants.USER_NOT_FOUND));

        return new ServiceOutput<>(userEntity);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Creates a new FreshMeal user account specifically through the public
     * registration workflow.
     * </p>
     *
     * <p>
     * Unlike {@link #addUser(IServiceInput)}, a newly registered account must
     * complete email verification before authentication is permitted. Therefore,
     * the account is created with {@code emailVerified = false} and
     * {@code enabled = false}.
     * </p>
     *
     * <h3>Registration Security</h3>
     * <ul>
     * <li>Username uniqueness is validated.</li>
     * <li>Email uniqueness is validated.</li>
     * <li>Phone-number uniqueness is validated.</li>
     * <li>The default {@link RoleType#USER} role is assigned.</li>
     * <li>The account remains disabled until email verification succeeds.</li>
     * <li>Password handling remains outside this User service.</li>
     * </ul>
     *
     * @param input service input containing registration information
     * @return newly created, unverified user entity
     */
    @Override
    public IServiceOutput<UserEntity> registerUser(IServiceInput<UserInputDTO> input) {

        UserInputDTO userInputDTO = input.getInput();
        UserRequest userRequest = userInputDTO.getUserRequest();

        /*
         * Validate unique username before creating the entity.
         */
        ensureUsernameAvailable(userRequest.getUsername(), null);

        /*
         * Validate unique email before creating the entity.
         */
        String normalizedEmail = FreshMealUtilities.normalizeEmail(userRequest.getEmail().getValue());
        ensureEmailAvailable(normalizedEmail, null);

        /*
         * Validate unique phone number before creating the entity.
         */
        ensurePhoneNumberAvailable(userRequest.getPhoneNumber(), null);

        /*
         * Create the user using the existing UserEntity creation infrastructure.
         */
        UserEntity userEntity = createUserEntity(input).getOutput();

        /*
         * Registration requires email verification before authentication.
         */
        userEntity.setEmailVerified(false);
        userEntity.setEnabled(false);

        /*
         * Persist the newly registered user.
         */
        userEntity = userRepository.save(userEntity);

        return new ServiceOutput<>(userEntity);
    }

    /**
     * =============================================================================
     * REGISTER USER
     * =============================================================================
     *
     * <p>
     * Registers a FreshMeal user using an already encoded password and the role
     * requested during the public registration workflow.
     * </p>
     *
     * <p>
     * The Authentication module is responsible for validating the raw password and
     * encoding it. This service is responsible for resolving the user identity,
     * creating or reusing the {@link UserEntity}, assigning the requested role,
     * maintaining registration state, and persisting the result.
     * </p>
     *
     * <p>
     * Registration supports the following scenarios:
     * </p>
     *
     * <ul>
     * <li>Create a new user when no existing identity matches.</li>
     * <li>Add a new role to an existing user without creating a duplicate
     * user.</li>
     * <li>Reject registration when the supplied identity information conflicts
     * with existing users.</li>
     * <li>Reject a role that is already assigned to the existing user.</li>
     * </ul>
     *
     * <p>
     * Existing users retain their existing password, verification state, account
     * state, identifiers, and creation audit information.
     * </p>
     *
     * @param input           service input containing registration information and
     *                        requested role
     * @param encodedPassword password already encoded by the Authentication module
     * @return service output containing the persisted {@link UserEntity}
     */
    @Override
    public IServiceOutput<UserEntity> registerUser(final IServiceInput<UserRegistrationInputDTO> input,
            final String encodedPassword) {

        if (encodedPassword == null || encodedPassword.isBlank()) {
            throw new IllegalArgumentException("Encoded password must not be null or blank.");
        }

        if (input == null || input.getInput() == null) {
            throw new BusinessException(UserErrorConstants.USER_INVALID);
        }

        final UserRegistrationInputDTO registrationInput = input.getInput();

        final RoleType requestedRole = registrationInput.getRequestedRole();

        validateRegistrationRole(requestedRole);

        final UserRequest userRequest = registrationInput.getUserRequest();

        if (userRequest == null) {
            throw new BusinessException(UserErrorConstants.USER_INVALID);
        }
        /*
         * Resolve whether the supplied identity belongs to an existing user.
         *
         * The resolution uses username, email and phone number together rather than
         * assuming that a match on a single field automatically represents the same
         * person.
         */
        final UserEntity existingUser = findExistingRegistrationUser(userRequest);

        if (existingUser == null) {

            /*
             * No existing identity was found.
             *
             * Reuse the existing entity creation infrastructure so ID generation,
             * user-number generation, profile mapping and audit initialization remain
             * centralized.
             */
            final UserInputDTO userInputDTO = new UserInputDTO();
            userInputDTO.setUserRequest(userRequest);

            final IServiceInput<UserInputDTO> userServiceInput = new ServiceInput<>() {
                private static final long serialVersionUID = 5943270153680134096L;

                @Override
                public UserInputDTO getInput() {
                    return userInputDTO;
                }

                @Override
                public IServiceContext getServiceContext() {
                    return input.getServiceContext();
                }

                @Override
                public IDataContext getDataContext() {
                    return input.getDataContext();
                }
            };

            final UserEntity userEntity = createUserEntity(userServiceInput).getOutput();

            /*
             * createUserEntity() currently initializes the default USER role.
             * Registration-specific role selection overrides that default for the newly
             * created registration account.
             */
            userEntity.addRole(requestedRole);

            /*
             * Authentication owns password encoding. UserService only persists the encoded
             * value.
             */
            userEntity.setPassword(encodedPassword);

            /*
             * A new self-registered account requires email verification before it becomes
             * eligible for authentication.
             */
            userEntity.setEmailVerified(false);
            userEntity.setEnabled(false);

            final UserEntity savedUserEntity = userRepository.save(userEntity);

            return new ServiceOutput<>(savedUserEntity);
        }

        /*
         * An existing user has been identified.
         *
         * The existing password and account state must never be overwritten by another
         * registration request.
         */
        final List<RoleType> existingRoles = existingUser.getRoles() == null ? new ArrayList<>()
                : new ArrayList<>(existingUser.getRoles());

        if (existingRoles.contains(requestedRole)) {
            throw new BusinessException(UserErrorConstants.USER_ROLE_ALREADY_ASSIGNED);
        }

        existingRoles.add(requestedRole);
        existingUser.setRoles(existingRoles);

        /*
         * Only the role assignment changes for an existing user. Existing
         * authentication credentials, verification state and account state are
         * deliberately preserved.
         */
        existingUser.setUpdatedAt(AppCalendar.getBusinessLocalDateTime());

        if (serviceContext.getUserProfile() != null) {
            existingUser.setUpdatedBy(serviceContext.getUserProfile().getUserNumber());
        } else {
            existingUser.setUpdatedBy(RoleType.ADMIN.getLabel());
        }

        final UserEntity savedUserEntity = userRepository.save(existingUser);

        return new ServiceOutput<>(savedUserEntity);
    }

    /**
     * =================================================================================================
     * ACTIVATE USER
     * =================================================================================================
     *
     * <p>
     * Activates a self-registered user after successful email verification.
     * </p>
     *
     * <p>
     * Activation marks the user's email address as verified and enables the account
     * for authentication.
     * </p>
     *
     * <p>
     * This operation is intentionally owned by the User module so that the
     * Authentication module does not directly modify or persist {@link UserEntity}.
     * </p>
     *
     * <p>
     * The operation is idempotent. If the user has already been verified and
     * enabled, the existing user entity is returned without performing another
     * persistence operation.
     * </p>
     *
     * @param input service input containing the user business identifier
     * @return service output containing the activated {@link UserEntity}
     */
    @Override
    public IServiceOutput<UserEntity> activateUser(final IServiceInput<UserNumberRequest> input) {

        final UserEntity userEntity = loadUserByUserNumber(input).getOutput();

        /*
         * Activation is idempotent.
         *
         * If the account has already completed email verification and is enabled, no
         * further state change is required.
         */
        if (userEntity.isEmailVerified() && userEntity.isEnabled()) {

            return new ServiceOutput<>(userEntity);
        }

        /*
         * Mark the email address as verified and enable the account.
         *
         * These two state changes together represent successful activation of a
         * self-registered user.
         */
        userEntity.setEmailVerified(true);
        userEntity.setEnabled(true);

        /*
         * Populate update audit information using the same convention already used
         * throughout this UserServiceImpl.
         */
        userEntity.setUpdatedAt(AppCalendar.getBusinessLocalDateTime());

        if (serviceContext.getUserProfile() != null) {

            userEntity.setUpdatedBy(serviceContext.getUserProfile().getUserNumber());

        } else {

            userEntity.setUpdatedBy(RoleType.ADMIN.getLabel());
        }

        /*
         * Persist the activated user.
         */
        final UserEntity savedUserEntity = userRepository.save(userEntity);

        return new ServiceOutput<>(savedUserEntity);
    }

    /**
     * Resolves an existing user for a registration request.
     *
     * <p>
     * Username, email and phone number are treated as identity attributes. If none
     * of them match an existing active user, {@code null} is returned and the
     * registration flow may create a new user.
     * </p>
     *
     * <p>
     * If one or more attributes match existing users, all matches must resolve to
     * the same {@link UserEntity}. The supplied identity information must also
     * correspond to that same user. This prevents accidental merging of different
     * users when only one identity attribute happens to match.
     * </p>
     *
     * @param userRequest registration identity information
     * @return matching existing user, or {@code null} when no identity exists
     */
    private UserEntity findExistingRegistrationUser(final UserRequest userRequest) {

        final String normalizedEmail = FreshMealUtilities.normalizeEmail(userRequest.getEmail().getValue());

        final List<UserEntity> matchedUsers = new ArrayList<>();

        userRepository.findOne(Query.query(Criteria.where("username").is(userRequest.getUsername())))
                .ifPresent(user -> addUniqueUserMatch(matchedUsers, user));

        userRepository.findOne(Query.query(Criteria.where("email.value").is(normalizedEmail)))
                .ifPresent(user -> addUniqueUserMatch(matchedUsers, user));

        userRepository.findOne(Query.query(Criteria.where("phoneNumber").is(userRequest.getPhoneNumber())))
                .ifPresent(user -> addUniqueUserMatch(matchedUsers, user));

        if (matchedUsers.isEmpty()) {
            return null;
        }

        final UserEntity existingUser = matchedUsers.get(0);

        /*
         * All identity attributes that match existing users must resolve to the same
         * persisted user.
         */
        for (final UserEntity matchedUser : matchedUsers) {

            if (!Objects.equals(existingUser.getId(), matchedUser.getId())) {
                throw new BusinessException(UserErrorConstants.USER_INVALID);
            }
        }

        /*
         * The registration request must describe the same identity. Registration is not
         * an implicit profile-update operation.
         */
        final boolean usernameMatches = Objects.equals(existingUser.getUsername(), userRequest.getUsername());

        final boolean emailMatches = existingUser.getEmail() != null
                && Objects.equals(existingUser.getEmail().getValue(), normalizedEmail);

        final boolean phoneMatches = existingUser.getPhoneNumber() != null
                && Objects.equals(existingUser.getPhoneNumber().getValue(), userRequest.getPhoneNumber().getValue());

        if (!usernameMatches || !emailMatches || !phoneMatches) {
            throw new BusinessException(UserErrorConstants.USER_ALREADY_EXISTS);
        }

        return existingUser;
    }

    /**
     * Validates whether a role is permitted through public registration.
     *
     * <p>
     * Administrative roles must never be granted through public self-registration.
     * Only roles explicitly supported by the public registration workflow are
     * accepted.
     * </p>
     *
     * @param requestedRole role requested during registration
     */
    private void validateRegistrationRole(final RoleType requestedRole) {

        if (requestedRole == null) {
            throw new BusinessException(UserErrorConstants.ROLE_REQUIRED);
        }

        if (requestedRole != RoleType.USER && requestedRole != RoleType.RESTAURANT_OWNER
                && requestedRole != RoleType.DELIVERY_PARTNER) {

            throw new BusinessException(UserErrorConstants.INVALID_USER_ROLE);
        }
    }

    /**
     * Adds a matched user to the identity-resolution result when the user has not
     * already been added.
     *
     * @param matchedUsers collection of matched users
     * @param user         matched user
     */
    private void addUniqueUserMatch(final List<UserEntity> matchedUsers, final UserEntity user) {

        final boolean alreadyMatched = matchedUsers
                .stream()
                .anyMatch(existing -> Objects.equals(existing.getId(), user.getId()));

        if (!alreadyMatched) {
            matchedUsers.add(user);
        }
    }

}