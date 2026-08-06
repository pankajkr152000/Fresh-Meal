package com.foodies.freshmeal.common.io.service.impl;

import java.time.LocalDateTime;

import com.foodies.freshmeal.common.constants.RepositoryConstants;
import com.foodies.freshmeal.common.date.AppCalendar;

import lombok.Builder;

/**
 * ============================================================================
 * Repository Context
 * ============================================================================
 *
 * Carries persistence-specific contextual information required by repository
 * operations.
 *
 * <p>
 * This context intentionally contains only repository-related metadata and
 * remains independent from HTTP, Spring MVC, validation, auditing,
 * and business layer concerns.
 * </p>
 *
 * <p>
 * Typical use cases:
 * </p>
 *
 * <ul>
 * <li>Soft Delete</li>
 * <li>Restore</li>
 * <li>Future Bulk Updates</li>
 * <li>Future Audit Field Population</li>
 * </ul>
 *
 * <p>
 * This object should be created by the Service Layer and passed only to
 * repository methods that modify persistent state.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Builder
public record RepositoryContext(

        /**
         * User performing the persistence operation.
         */
        String currentUser,

        /**
         * Business timestamp of the operation.
         */
        LocalDateTime currentDateTime

) {

    /**
     * Creates a repository context representing a system initiated operation.
     *
     * <p>
     * Intended for scheduled jobs, startup routines,
     * background processing and automated tasks.
     * </p>
     *
     * @return System repository context.
     */
    public static RepositoryContext system() {

        return RepositoryContext.builder()
                .currentUser(RepositoryConstants.SYSTEM_USER)
                .currentDateTime(AppCalendar.getBusinessLocalDateTime())
                .build();
    }

    public static RepositoryContext of(String userName, LocalDateTime businessLocalDateTime) {

        return RepositoryContext.builder()
                .currentUser(userName)
                .currentDateTime(AppCalendar.getBusinessLocalDateTime())
                .build();
    }

}
