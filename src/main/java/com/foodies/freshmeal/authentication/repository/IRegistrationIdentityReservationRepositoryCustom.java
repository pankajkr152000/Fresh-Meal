
package com.foodies.freshmeal.authentication.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import com.foodies.freshmeal.authentication.entity.RegistrationIdentityReservationEntity;

/**
 * ============================================================================
 * Registration Identity Reservation Custom Repository
 * ============================================================================
 *
 * Defines specialized persistence operations for registration identity
 * reservation management.
 *
 * <p>
 * These operations support identity lookup, atomic ownership transfer,
 * and conditional physical deletion of pending reservations.
 * </p>
 *
 * <p>
 * Custom MongoDB operations must explicitly respect FreshMeal's
 * active-record filtering policy.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IRegistrationIdentityReservationRepositoryCustom {

    /**
     * Finds an active identity reservation by username.
     *
     * @param username normalized username.
     * @return matching active reservation; otherwise empty.
     */
    Optional<RegistrationIdentityReservationEntity> findActiveByUsername(
            String username);

    /**
     * Finds an active identity reservation by email address.
     *
     * @param email normalized email address.
     * @return matching active reservation; otherwise empty.
     */
    Optional<RegistrationIdentityReservationEntity> findActiveByEmail(
            String email);

    /**
     * Finds an active identity reservation by phone number.
     *
     * @param phoneNumber canonical phone number.
     * @return matching active reservation; otherwise empty.
     */
    Optional<RegistrationIdentityReservationEntity> findActiveByPhoneNumber(
            String phoneNumber);

    /**
     * Atomically transfers reservation ownership from a pending registration
     * to a permanently registered user.
     *
     * <p>
     * The operation succeeds only when the reservation is currently
     * owned by the specified pending registration and has not already
     * been transferred.
     * </p>
     *
     * <p>
     * The update must also maintain inherited audit and version fields.
     * </p>
     *
     * @param registrationNumber expected pending registration owner.
     * @param userNumber         permanent user business identifier.
     * @param updatedAt          ownership transfer timestamp.
     * @param updatedBy          actor performing the operation.
     * @return updated reservation when the conditional update succeeds;
     *         otherwise empty.
     */
    Optional<RegistrationIdentityReservationEntity> transferOwnershipToUser(
            String registrationNumber,
            String userNumber,
            LocalDateTime updatedAt,
            String updatedBy);

    /**
     * Physically deletes an identity reservation only when it is still
     * owned by the specified pending registration.
     *
     * <p>
     * The ownership condition prevents this operation from deleting
     * a reservation that has already been transferred to a permanent user.
     * </p>
     *
     * <p>
     * This operation must physically delete the document rather than
     * soft-delete it because its unique indexes must be released.
     * </p>
     *
     * @param registrationNumber pending registration business identifier.
     * @return number of reservation documents physically deleted.
     */
    long deletePendingReservation(String registrationNumber);
}
