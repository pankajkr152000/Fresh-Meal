package com.foodies.freshmeal.user.service;

import com.foodies.freshmeal.authentication.dto.LoginHistoryInputDTO;
import com.foodies.freshmeal.authentication.dto.LoginHistoryNumberRequest;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.user.entity.LoginHistoryEntity;

/**
 * ============================================================================
 * Service : ILoginHistoryService
 * ============================================================================
 *
 * <p>
 * Defines authentication login-history operations for FreshMeal.
 * </p>
 *
 * <p>
 * Login history records authentication attempts and session lifecycle
 * information independently from
 * {@link com.foodies.freshmeal.user.entity.UserEntity}.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Create authentication history records.</li>
 * <li>Generate login-history business identifiers.</li>
 * <li>Load active login-history records.</li>
 * <li>Find authentication history by user.</li>
 * <li>Find successful authentication sessions.</li>
 * <li>Record session logout.</li>
 * <li>Perform logical deletion.</li>
 * <li>Permanently delete records when explicitly required.</li>
 * </ul>
 *
 * <h3>Security Boundary</h3>
 * <p>
 * Login history must never contain passwords, password hashes, access tokens,
 * refresh tokens, API keys, or other authentication secrets.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface ILoginHistoryService {

    /**
     * Generates a business-facing login-history number.
     *
     * @param input service input carrying service context
     * @return generated login-history number
     */
    IServiceOutput<String> generateLoginHistoryNumber(
            IServiceInput<LoginHistoryInputDTO> input);

    /**
     * Creates and persists a login-history record.
     *
     * @param input login-history information
     * @return persisted login-history entity
     */
    IServiceOutput<LoginHistoryEntity> createLoginHistory(
            IServiceInput<LoginHistoryInputDTO> input);

    /**
     * Loads an active login-history record using its internal identifier.
     *
     * @param input service input containing the database identifier
     * @return active login-history entity
     */
    IServiceOutput<LoginHistoryEntity> loadLoginHistory(
            IServiceInput<String> input);

    /**
     * Loads an active login-history record using its business identifier.
     *
     * @param input service input containing login-history number
     * @return active login-history entity
     */
    IServiceOutput<LoginHistoryEntity> loadLoginHistoryByNumber(
            IServiceInput<LoginHistoryNumberRequest> input);

    /**
     * Loads active login-history records belonging to a user.
     *
     * @param input service input containing user number
     * @return matching login-history entities
     */
    IServiceOutput<java.util.List<LoginHistoryEntity>> loadLoginHistoriesByUserNumber(
            IServiceInput<com.foodies.freshmeal.user.dto.UserNumberRequest> input);

    /**
     * Loads successful login-history records belonging to a user.
     *
     * @param input service input containing user number
     * @return successful login-history entities
     */
    IServiceOutput<java.util.List<LoginHistoryEntity>> loadSuccessfulLoginHistories(
            IServiceInput<com.foodies.freshmeal.user.dto.UserNumberRequest> input);

    /**
     * Loads currently active authenticated sessions belonging to a user.
     *
     * <p>
     * A session is considered active when authentication succeeded and
     * {@code logoutTime} has not yet been populated.
     * </p>
     *
     * @param input service input containing user number
     * @return active login-history entities
     */
    IServiceOutput<java.util.List<LoginHistoryEntity>> loadActiveLoginHistories(
            IServiceInput<com.foodies.freshmeal.user.dto.UserNumberRequest> input);

    /**
     * Records logout information for a login-history record.
     *
     * @param input service input containing login-history number
     * @return updated login-history entity
     */
    IServiceOutput<LoginHistoryEntity> recordLogout(
            IServiceInput<LoginHistoryNumberRequest> input);

    /**
     * Soft-deletes a login-history record.
     *
     * @param input service input containing the database identifier
     * @return {@code true} when deletion succeeds
     */
    IServiceOutput<Boolean> deleteLoginHistory(
            IServiceInput<String> input);

    /**
     * Permanently deletes a login-history record.
     *
     * <p>
     * This operation bypasses logical deletion and should be used only for
     * explicit administrative or data-retention operations.
     * </p>
     *
     * @param input service input containing the database identifier
     * @return {@code true} when deletion succeeds
     */
    IServiceOutput<Boolean> permanentlyDeleteLoginHistory(
            IServiceInput<String> input);

    IServiceOutput<LoginHistoryEntity> loadLoginHistoryBySessionId(
            IServiceInput<String> input);
}
