package com.foodies.freshmeal.user.service.impl;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.foodies.freshmeal.authentication.dto.LoginHistoryInputDTO;
import com.foodies.freshmeal.authentication.dto.LoginHistoryNumberRequest;
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
import com.foodies.freshmeal.user.constants.LoginStatus;
import com.foodies.freshmeal.user.dto.UserNumberRequest;
import com.foodies.freshmeal.user.entity.LoginHistoryEntity;
import com.foodies.freshmeal.user.repository.ILoginHistoryRepository;
import com.foodies.freshmeal.user.service.ILoginHistoryService;

/**
 * ============================================================================
 * Service : LoginHistoryServiceImpl
 * ============================================================================
 *
 * <p>
 * Provides the business implementation for FreshMeal authentication
 * login-history operations.
 * </p>
 *
 * <p>
 * Login history is maintained independently from
 * {@link com.foodies.freshmeal.user.entity.UserEntity}
 * so that the user entity represents current account state while authentication
 * history represents historical login and session activity.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Generate login-history business identifiers.</li>
 * <li>Create authentication history records.</li>
 * <li>Load active authentication history.</li>
 * <li>Load history by user.</li>
 * <li>Identify successful authentication sessions.</li>
 * <li>Record logout information.</li>
 * <li>Perform logical deletion.</li>
 * <li>Perform explicit permanent deletion.</li>
 * </ul>
 *
 * <h3>Security Boundary</h3>
 * <p>
 * This service never persists passwords, password hashes, JWTs, refresh
 * tokens, API keys, or other authentication secrets.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Service
public class LoginHistoryServiceImpl implements ILoginHistoryService {

    // =========================================================================
    // Dependencies
    // =========================================================================

    /**
     * Database sequence service.
     */
    private final IDatabaseSequenceService databaseSequenceService;

    /**
     * Login-history repository.
     */
    private final ILoginHistoryRepository loginHistoryRepository;

    /**
     * Current service context.
     */
    private final IServiceContext serviceContext;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Creates a LoginHistoryServiceImpl.
     *
     * @param databaseSequenceService database sequence service
     * @param loginHistoryRepository  login-history repository
     * @param serviceContext          current service context
     * @param tokenRevocationService  token revocation service
     */
    public LoginHistoryServiceImpl(
            final IDatabaseSequenceService databaseSequenceService,
            final ILoginHistoryRepository loginHistoryRepository,
            final IServiceContext serviceContext) {

        this.databaseSequenceService = databaseSequenceService;
        this.loginHistoryRepository = loginHistoryRepository;
        this.serviceContext = serviceContext;
    }

    // =========================================================================
    // Business Identifier
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<String> generateLoginHistoryNumber(
            final IServiceInput<LoginHistoryInputDTO> input) {

        final long sequence = databaseSequenceService.generateSequence(
                input.getServiceContext(),
                SequenceConstants.LOGIN_HISTORY_ENTITY_SEQUENCE);

        input.getServiceContext().setAttribute(
                DataContext.LOGIN_HISTORY_SEQUENCE,
                sequence);

        final String loginHistoryNumber = String.format(
                SequenceConstants.LOGIN_HISTORY_NUMBER_PATTERN,
                sequence);

        return new ServiceOutput<>(
                loginHistoryNumber);
    }

    // =========================================================================
    // Create
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<LoginHistoryEntity> createLoginHistory(
            final IServiceInput<LoginHistoryInputDTO> input) {

        final LoginHistoryInputDTO inputDTO = input.getInput();

        if (inputDTO == null) {

            throw new BusinessException(
                    CommonErrorConstants.INVALID_REQUEST);
        }

        if (inputDTO.getUserNumber() == null
                || inputDTO.getUserNumber().isBlank()) {

            throw new BusinessException(
                    CommonErrorConstants.INVALID_REQUEST);
        }

        if (inputDTO.getLoginStatus() == null) {

            throw new BusinessException(
                    CommonErrorConstants.INVALID_REQUEST);
        }

        final LoginHistoryEntity loginHistoryEntity = (LoginHistoryEntity) EntityFactory.createEntity(
                EntityName.LOGIN_HISTORY_ENTITY);

        /*
         * Generate business-facing login-history number.
         */
        final String loginHistoryNumber = generateLoginHistoryNumber(input).getOutput();

        loginHistoryEntity.setLoginHistoryNumber(
                loginHistoryNumber);

        /*
         * Copy authentication audit information.
         */
        loginHistoryEntity.setUserNumber(
                inputDTO.getUserNumber());

        loginHistoryEntity.setLoginStatus(
                inputDTO.getLoginStatus());

        loginHistoryEntity.setLoginTime(
                inputDTO.getLoginTime() != null
                        ? inputDTO.getLoginTime()
                        : AppCalendar.getBusinessLocalDateTime());

        loginHistoryEntity.setSessionId(
                inputDTO.getSessionId());

        loginHistoryEntity.setIpAddress(
                inputDTO.getIpAddress());

        loginHistoryEntity.setUserAgent(
                inputDTO.getUserAgent());

        loginHistoryEntity.setLoginServerName(
                inputDTO.getLoginServerName());

        /*
         * Populate creation audit information.
         */
        loginHistoryEntity.setCreatedAt(
                AppCalendar.getBusinessLocalDateTime());

        if (serviceContext.getUserProfile() != null) {

            loginHistoryEntity.setCreatedBy(
                    serviceContext.getUserProfile().getUserNumber());

        } else {

            loginHistoryEntity.setCreatedBy(
                    RoleType.ADMIN.getLabel());
        }

        final LoginHistoryEntity savedEntity = loginHistoryRepository.save(loginHistoryEntity);

        return new ServiceOutput<>(
                savedEntity);
    }

    // =========================================================================
    // Load
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<LoginHistoryEntity> loadLoginHistory(
            final IServiceInput<String> input) {

        final String id = input.getInput();

        final LoginHistoryEntity entity = loginHistoryRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        CommonErrorConstants.RESOURCE_NOT_FOUND));

        return new ServiceOutput<>(
                entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<LoginHistoryEntity> loadLoginHistoryByNumber(
            final IServiceInput<LoginHistoryNumberRequest> input) {

        final LoginHistoryNumberRequest request = input.getInput();

        final Query query = Query.query(
                Criteria.where("loginHistoryNumber")
                        .is(request.getLoginHistoryNumber()));

        final LoginHistoryEntity entity = loginHistoryRepository.findOne(query)
                .orElseThrow(() -> new ResourceNotFoundException(
                        CommonErrorConstants.RESOURCE_NOT_FOUND));

        return new ServiceOutput<>(
                entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<List<LoginHistoryEntity>> loadLoginHistoriesByUserNumber(
            final IServiceInput<UserNumberRequest> input) {

        final UserNumberRequest request = input.getInput();

        final Query query = Query.query(
                Criteria.where("userNumber")
                        .is(request.getUserNumber()));

        final List<LoginHistoryEntity> histories = loginHistoryRepository.findAll(query);

        return new ServiceOutput<>(
                histories);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<List<LoginHistoryEntity>> loadSuccessfulLoginHistories(
            final IServiceInput<UserNumberRequest> input) {

        final UserNumberRequest request = input.getInput();

        final Query query = Query.query(
                Criteria.where("userNumber")
                        .is(request.getUserNumber())
                        .and("loginStatus")
                        .is(LoginStatus.SUCCESS));

        final List<LoginHistoryEntity> histories = loginHistoryRepository.findAll(query);

        return new ServiceOutput<>(
                histories);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<List<LoginHistoryEntity>> loadActiveLoginHistories(
            final IServiceInput<UserNumberRequest> input) {

        final UserNumberRequest request = input.getInput();

        final Query query = Query.query(
                Criteria.where("userNumber")
                        .is(request.getUserNumber())
                        .and("loginStatus")
                        .is(LoginStatus.SUCCESS)
                        .and("logoutTime")
                        .exists(false));

        final List<LoginHistoryEntity> histories = loginHistoryRepository.findAll(query);

        return new ServiceOutput<>(
                histories);
    }

    // =========================================================================
    // Logout
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<LoginHistoryEntity> recordLogout(
            final IServiceInput<LoginHistoryNumberRequest> input) {

        final LoginHistoryEntity loginHistoryEntity = loadLoginHistoryByNumber(input).getOutput();

        /*
         * A logout timestamp must not be overwritten.
         */
        if (loginHistoryEntity.getLogoutTime() != null) {

            return new ServiceOutput<>(
                    loginHistoryEntity);
        }

        loginHistoryEntity.setLogoutTime(
                AppCalendar.getBusinessLocalDateTime());

        loginHistoryEntity.setUpdatedAt(
                AppCalendar.getBusinessLocalDateTime());

        if (serviceContext.getUserProfile() != null) {

            loginHistoryEntity.setUpdatedBy(
                    serviceContext.getUserProfile().getUserNumber());

        } else {

            loginHistoryEntity.setUpdatedBy(
                    RoleType.ADMIN.getLabel());
        }

        final LoginHistoryEntity savedEntity = loginHistoryRepository.save(
                loginHistoryEntity);

        return new ServiceOutput<>(
                savedEntity);
    }

    // =========================================================================
    // Delete
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<Boolean> deleteLoginHistory(
            final IServiceInput<String> input) {

        final RepositoryContext repositoryContext = buildRepositoryContext();

        loginHistoryRepository.softDelete(
                input.getInput(),
                repositoryContext);

        return new ServiceOutput<>(
                Boolean.TRUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<Boolean> permanentlyDeleteLoginHistory(
            final IServiceInput<String> input) {

        loginHistoryRepository.deletePermanently(
                input.getInput());

        return new ServiceOutput<>(
                Boolean.TRUE);
    }

    // =========================================================================
    // Private Helpers
    // =========================================================================

    /**
     * Creates repository context using the current service user.
     *
     * @return repository context
     */
    private RepositoryContext buildRepositoryContext() {

        String currentUser = RoleType.ADMIN.getLabel();

        if (serviceContext.getUserProfile() != null) {

            currentUser = serviceContext.getUserProfile().getUserNumber();
        }

        return RepositoryContext.of(
                currentUser,
                AppCalendar.getBusinessLocalDateTime());
    }

    @Override
    public IServiceOutput<LoginHistoryEntity> loadLoginHistoryBySessionId(
            IServiceInput<String> input) {

        String sessionId = input.getInput();

        if (!hasText(sessionId)) {
            throw new BusinessException(CommonErrorConstants.INVALID_REQUEST);
        }

        final Query query = Query.query(
                Criteria.where("sessionId")
                        .is(sessionId));

        final LoginHistoryEntity entity = loginHistoryRepository.findOne(query)
                .orElseThrow(() -> new ResourceNotFoundException(
                        CommonErrorConstants.RESOURCE_NOT_FOUND));

        return new ServiceOutput<>(entity);
    }

    /**
     * Determines whether a value contains meaningful text.
     *
     * @param value value to validate
     * @return {@code true} when the value contains non-whitespace characters
     */
    private boolean hasText(final String value) {

        return value != null
                && !value.isBlank();
    }
}
