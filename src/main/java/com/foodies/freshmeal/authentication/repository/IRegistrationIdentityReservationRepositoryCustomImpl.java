
package com.foodies.freshmeal.authentication.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.authentication.constants.RegistrationIdentityOwnerType;
import com.foodies.freshmeal.authentication.constants.RegistrationIdentityReservationConstants;
import com.foodies.freshmeal.authentication.entity.RegistrationIdentityReservationEntity;
import com.foodies.freshmeal.common.constants.EntityFieldConstants;
import com.foodies.freshmeal.common.constants.RepositoryConstants;
import com.mongodb.client.result.DeleteResult;

/**
 * Custom MongoDB repository implementation for registration identity
 * reservation operations.
 *
 * <p>
 * This implementation provides specialized database operations for
 * checking identity reservations, transferring reservation ownership,
 * and releasing pending registration reservations.
 * </p>
 *
 * <p>
 * Ownership-sensitive operations use atomic MongoDB operations to
 * protect reservation integrity under concurrent requests.
 * </p>
 *
 * <p>
 * All read and update operations explicitly enforce the active-record
 * policy. Pending reservations are physically deleted when released so
 * their unique identity indexes become available for future registrations.
 * </p>
 *
 * @author FreshMeal
 * @since 1.0
 */
@Repository
public class IRegistrationIdentityReservationRepositoryCustomImpl
        implements IRegistrationIdentityReservationRepositoryCustom {

    private final MongoOperations mongoOperations;

    /**
     * Constructs the custom repository implementation.
     *
     * @param mongoOperations MongoDB operations abstraction
     */
    public IRegistrationIdentityReservationRepositoryCustomImpl(
            final MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    /**
     * Finds an active reservation by username.
     *
     * <p>
     * The lookup considers reservations owned by both pending registrations
     * and permanent users.
     * </p>
     *
     * @param username normalized username
     * @return optional matching reservation
     */
    @Override
    public Optional<RegistrationIdentityReservationEntity> findActiveByUsername(
            final String username) {

        return findActiveReservation(
                RegistrationIdentityReservationConstants.FIELD_USERNAME,
                username);
    }

    /**
     * Finds an active reservation by normalized email address.
     *
     * <p>
     * The lookup considers reservations owned by both pending registrations
     * and permanent users.
     * </p>
     *
     * @param email normalized email address
     * @return optional matching reservation
     */
    @Override
    public Optional<RegistrationIdentityReservationEntity> findActiveByEmail(
            final String email) {

        return findActiveReservation(
                RegistrationIdentityReservationConstants.FIELD_EMAIL,
                email);
    }

    /**
     * Finds an active reservation by canonical phone number.
     *
     * <p>
     * The lookup considers reservations owned by both pending registrations
     * and permanent users.
     * </p>
     *
     * @param phoneNumber canonical phone number
     * @return optional matching reservation
     */
    @Override
    public Optional<RegistrationIdentityReservationEntity> findActiveByPhoneNumber(
            final String phoneNumber) {

        return findActiveReservation(
                RegistrationIdentityReservationConstants.FIELD_PHONE_NUMBER,
                phoneNumber);
    }

    /**
     * Transfers reservation ownership from a pending registration to
     * a permanent user.
     *
     * <p>
     * The operation succeeds only when the reservation is currently
     * owned by the supplied registration number and has no user owner.
     * </p>
     *
     * <p>
     * The ownership check and update execute atomically. The registration
     * number is removed after successful transfer, while the permanent
     * user number becomes the new owner reference.
     * </p>
     *
     * <p>
     * The version and audit fields are updated explicitly because this
     * operation uses a direct MongoDB update rather than entity persistence.
     * </p>
     *
     * @param registrationNumber expected pending registration owner
     * @param userNumber         permanent user number receiving ownership
     * @param updatedAt          business timestamp of the ownership transfer
     * @param updatedBy          actor performing the ownership transfer
     * @return optional updated reservation; empty when ownership conditions
     *         are not satisfied
     */
    @Override
    public Optional<RegistrationIdentityReservationEntity> transferOwnershipToUser(
            final String registrationNumber,
            final String userNumber,
            final LocalDateTime updatedAt,
            final String updatedBy) {

        final Query query = Query.query(
                Criteria.where(
                        RegistrationIdentityReservationConstants.FIELD_REGISTRATION_NUMBER)
                        .is(registrationNumber)
                        .and(RegistrationIdentityReservationConstants.FIELD_OWNER_TYPE)
                        .is(RegistrationIdentityOwnerType.PENDING_REGISTRATION)
                        .and(RegistrationIdentityReservationConstants.FIELD_USER_NUMBER)
                        .is(null)
                        .and(EntityFieldConstants.DELETED_FLAG)
                        .is(RepositoryConstants.ACTIVE));

        final Update update = new Update()
                .set(
                        RegistrationIdentityReservationConstants.FIELD_OWNER_TYPE,
                        RegistrationIdentityOwnerType.USER)
                .set(
                        RegistrationIdentityReservationConstants.FIELD_USER_NUMBER,
                        userNumber)
                .unset(
                        RegistrationIdentityReservationConstants.FIELD_REGISTRATION_NUMBER)
                .set("updatedAt", updatedAt)
                .set("updatedBy", updatedBy)
                .inc("version", 1);

        final FindAndModifyOptions options = FindAndModifyOptions.options()
                .returnNew(true);

        final RegistrationIdentityReservationEntity updatedReservation = mongoOperations.findAndModify(
                query,
                update,
                options,
                RegistrationIdentityReservationEntity.class);

        return Optional.ofNullable(updatedReservation);
    }

    /**
     * Physically deletes a reservation owned by the specified pending
     * registration.
     *
     * <p>
     * The operation deletes only when the reservation is still owned
     * by the supplied registration number, is of pending-registration
     * ownership type, has no permanent user owner, and represents an
     * active record.
     * </p>
     *
     * <p>
     * A reservation already transferred to a permanent user cannot
     * be deleted through this operation.
     * </p>
     *
     * @param registrationNumber pending registration owner
     * @return number of physically deleted reservation documents
     */
    @Override
    public long deletePendingReservation(final String registrationNumber) {

        final Query query = Query.query(
                Criteria.where(
                        RegistrationIdentityReservationConstants.FIELD_REGISTRATION_NUMBER)
                        .is(registrationNumber)
                        .and(RegistrationIdentityReservationConstants.FIELD_OWNER_TYPE)
                        .is(RegistrationIdentityOwnerType.PENDING_REGISTRATION)
                        .and(RegistrationIdentityReservationConstants.FIELD_USER_NUMBER)
                        .is(null)
                        .and(EntityFieldConstants.DELETED_FLAG)
                        .is(RepositoryConstants.ACTIVE));

        final DeleteResult deleteResult = mongoOperations.remove(
                query,
                RegistrationIdentityReservationEntity.class);

        return deleteResult.getDeletedCount();
    }

    /**
     * Performs an active reservation lookup using the supplied field.
     *
     * <p>
     * This private method centralizes the active-record condition so
     * all identity lookups follow the same repository policy.
     * </p>
     *
     * @param fieldName  MongoDB field name
     * @param fieldValue value to match
     * @return optional matching reservation
     */
    private Optional<RegistrationIdentityReservationEntity> findActiveReservation(
            final String fieldName,
            final String fieldValue) {

        final Query query = Query.query(
                Criteria.where(fieldName)
                        .is(fieldValue)
                        .and(EntityFieldConstants.DELETED_FLAG)
                        .is(RepositoryConstants.ACTIVE));

        final RegistrationIdentityReservationEntity reservation = mongoOperations.findOne(
                query,
                RegistrationIdentityReservationEntity.class);

        return Optional.ofNullable(reservation);
    }
}
