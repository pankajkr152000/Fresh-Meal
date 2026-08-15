package com.foodies.freshmeal.common.io.service.impl;

import java.time.LocalDateTime;

import com.foodies.freshmeal.common.constants.RepositoryConstants;
import com.foodies.freshmeal.common.date.AppCalendar;

import lombok.Builder;

/**
 * ============================================================================
 * Record : RepositoryContext
 * ============================================================================
 *
 * Carries persistence-specific contextual information required by repository
 * operations.
 *
 * <p>
 * This context intentionally contains only repository-related information and
 * remains independent from HTTP, Spring MVC, validation, auditing,
 * UserProfile, and business-layer objects.
 * </p>
 *
 * <p>
 * Typical use cases include:
 * </p>
 *
 * <ul>
 * <li>Soft Delete</li>
 * <li>Restore</li>
 * <li>Future Bulk Updates</li>
 * <li>Future Audit Field Population</li>
 * <li>System initiated persistence operations</li>
 * </ul>
 *
 * <p>
 * The Service Layer creates this context from the current
 * {@code IServiceContext} and passes it to repository operations that
 * modify persistent state.
 * </p>
 *
 * ============================================================================
 *
 * Design Principle
 * ----------------
 *
 * IServiceContext belongs to the service/application layer.
 *
 * RepositoryContext belongs to the persistence layer.
 *
 * The repository must not depend directly on UserProfile or IServiceContext.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Builder
public record RepositoryContext(

        /**
         * Identifier/name of the user performing the persistence operation.
         *
         * <p>
         * This intentionally stores a lightweight identifier rather than the
         * complete UserProfile object.
         * </p>
         */
        String currentUser,

        /**
         * Business timestamp of the persistence operation.
         */
        LocalDateTime currentDateTime

) {

    /**
     * Creates a RepositoryContext for a system-initiated operation.
     *
     * <p>
     * Intended for scheduled jobs, background processing, startup routines
     * and other operations where no authenticated application user exists.
     * </p>
     *
     * @return system repository context
     */
    public static RepositoryContext system() {

        return RepositoryContext.builder()
                .currentUser(
                        RepositoryConstants.SYSTEM_USER)
                .currentDateTime(
                        AppCalendar.getBusinessLocalDateTime())
                .build();
    }

    /**
     * Creates a RepositoryContext using the supplied user and business time.
     *
     * <p>
     * This method is useful when the Service Layer already has the appropriate
     * user and business timestamp available.
     * </p>
     *
     * @param userName              user performing the operation
     * @param businessLocalDateTime business timestamp of the operation
     *
     * @return repository context
     */
    public static RepositoryContext of(
            String userName,
            LocalDateTime businessLocalDateTime) {

        return RepositoryContext.builder()
                .currentUser(userName)
                .currentDateTime(
                        businessLocalDateTime != null
                                ? businessLocalDateTime
                                : AppCalendar
                                        .getBusinessLocalDateTime())
                .build();
    }

    /**
     * Creates a RepositoryContext using the current business timestamp.
     *
     * <p>
     * Useful when the caller only needs to provide the user identity.
     * </p>
     *
     * @param userName user performing the operation
     *
     * @return repository context
     */
    public static RepositoryContext of(String userName) {

        return RepositoryContext.builder()
                .currentUser(userName)
                .currentDateTime(
                        AppCalendar.getBusinessLocalDateTime())
                .build();
    }

    /**
     * Creates a RepositoryContext using the system user while allowing an
     * explicitly supplied business timestamp.
     *
     * <p>
     * Useful for system-generated operations that need to preserve a specific
     * business timestamp.
     * </p>
     *
     * @param businessLocalDateTime business timestamp
     *
     * @return system repository context
     */
    public static RepositoryContext system(
            LocalDateTime businessLocalDateTime) {

        return RepositoryContext.builder()
                .currentUser(
                        RepositoryConstants.SYSTEM_USER)
                .currentDateTime(
                        businessLocalDateTime != null
                                ? businessLocalDateTime
                                : AppCalendar
                                        .getBusinessLocalDateTime())
                .build();
    }
}