package com.foodies.freshmeal.user.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.common.repository.base.IBaseRepository;
import com.foodies.freshmeal.user.entity.UserEntity;

/**
 * =============================================================================
 * Repository : IUserRepository
 * =============================================================================
 *
 * Purpose
 * -------
 * Provides persistence operations for UserEntity.
 *
 * <p>
 * The repository extends the common {@link IBaseRepository} so that all
 * standard FreshMeal persistence operations remain centralized in the common
 * repository infrastructure.
 * </p>
 *
 * <p>
 * Custom user queries should be added here only when required by UserService,
 * authentication, authorization, or other user-related business operations.
 * </p>
 *
 * =============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Repository
public interface IUserRepository extends IBaseRepository<UserEntity, String> {

    /**
     * Loads a user using the FreshMeal business-facing user number.
     *
     * <p>
     * The user number is the external/business identifier of the user and
     * should be preferred for business operations over the internal MongoDB
     * identifier.
     * </p>
     *
     * @param userNumber business user identifier.
     * @return matching user entity, if present.
     */
    Optional<UserEntity> findByUserNumber(String userNumber);

    /**
     * Loads a user using the username used for authentication.
     *
     * <p>
     * This query is required by the authentication layer when Spring Security
     * loads a user during the authentication process.
     * </p>
     *
     * @param username authentication username.
     * @return matching user entity, if present.
     */
    Optional<UserEntity> findByUsername(String username);
}