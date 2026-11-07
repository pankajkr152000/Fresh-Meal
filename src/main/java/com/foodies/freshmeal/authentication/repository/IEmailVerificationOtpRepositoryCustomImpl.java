
package com.foodies.freshmeal.authentication.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationUpdate;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.authentication.entity.EmailVerificationOtpEntity;
import com.foodies.freshmeal.common.constants.EntityFieldConstants;
import com.foodies.freshmeal.common.constants.RepositoryConstants;

/**
 * Custom MongoDB repository implementation for email verification OTPs.
 *
 * <p>
 * Provides atomic persistence operations for OTP lifecycle management,
 * including consumption, failed-attempt tracking, dispatch tracking,
 * and revocation.
 * </p>
 *
 * <p>
 * All operations performed directly through {@link MongoOperations}
 * must explicitly respect the application's soft-delete policy.
 * </p>
 */
@Repository
public class IEmailVerificationOtpRepositoryCustomImpl
        implements IEmailVerificationOtpRepositoryCustom {

    private final MongoOperations mongoOperations;

    /**
     * Constructs the custom OTP repository implementation.
     *
     * @param mongoOperations MongoDB operations abstraction
     */
    public IEmailVerificationOtpRepositoryCustomImpl(
            final MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    /**
     * Atomically consumes an eligible email verification OTP.
     *
     * @param verificationNumber unique OTP verification number
     * @param now                current business timestamp
     * @param updatedBy          actor performing the operation
     * @return consumed OTP when the operation succeeds; otherwise empty
     */
    @Override
    public Optional<EmailVerificationOtpEntity> consumeEmailVerificationOtp(
            final String verificationNumber,
            final LocalDateTime now,
            final String updatedBy) {

        final Document criteria = new Document()
                .append("verificationNumber", verificationNumber)
                .append("used", false)
                .append("revoked", false)
                .append("sentAt", new Document("$ne", null))
                .append("expiresAt", new Document("$gt", now))
                .append("attemptsExceededAt", null)
                .append(EntityFieldConstants.DELETED_FLAG, RepositoryConstants.ACTIVE)
                .append("$expr", new Document(
                        "$lt",
                        List.of("$attemptCount", "$maxAttempts")));

        final Query query = new BasicQuery(criteria);

        final Update update = new Update()
                .set("used", true)
                .set("usedAt", now)
                .set("updatedAt", now)
                .set("updatedBy", updatedBy);

        final EmailVerificationOtpEntity consumedOtp = mongoOperations.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true),
                EmailVerificationOtpEntity.class);

        return Optional.ofNullable(consumedOtp);
    }

    /**
     * Atomically increments the failed verification attempt count.
     *
     * <p>
     * When the incremented attempt count reaches the configured maximum,
     * the OTP is placed into the attempt lockout state within the same
     * MongoDB update operation.
     * </p>
     *
     * @param verificationNumber unique OTP verification number
     * @param maxAttempts        maximum permitted verification attempts
     * @param now                current business timestamp
     * @param updatedBy          actor performing the operation
     * @return updated OTP when the operation succeeds; otherwise empty
     */
    @Override
    public Optional<EmailVerificationOtpEntity> incrementFailedAttempt(
            final String verificationNumber,
            final int maxAttempts,
            final LocalDateTime now,
            final String updatedBy) {

        final Document criteria = new Document()
                .append("verificationNumber", verificationNumber)
                .append("used", false)
                .append("revoked", false)
                .append("sentAt", new Document("$ne", null))
                .append("expiresAt", new Document("$gt", now))
                .append("attemptsExceededAt", null)
                .append("maxAttempts", maxAttempts)
                .append("attemptCount", new Document("$lt", maxAttempts))
                .append(EntityFieldConstants.DELETED_FLAG, RepositoryConstants.ACTIVE);

        final Query query = new BasicQuery(criteria);

        final AggregationUpdate update = AggregationUpdate.update()
                .set("attemptCount")
                .toValue(ArithmeticOperators.valueOf("attemptCount").add(1))
                .set("attemptsExceededAt")
                .toValue(
                        ConditionalOperators.when(
                                ComparisonOperators.valueOf("attemptCount")
                                        .greaterThanEqualToValue(maxAttempts - 1))
                                .then(now)
                                .otherwiseValueOf("attemptsExceededAt"))
                .set("updatedAt")
                .toValue(now)
                .set("updatedBy")
                .toValue(updatedBy);

        final EmailVerificationOtpEntity updatedOtp = mongoOperations.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true),
                EmailVerificationOtpEntity.class);

        return Optional.ofNullable(updatedOtp);
    }

    /**
     * Records the first successful dispatch timestamp for an email
     * verification OTP.
     *
     * <p>
     * The dispatch timestamp is set only once. Subsequent attempts to mark
     * the same OTP as sent will not overwrite the original timestamp.
     * </p>
     *
     * @param verificationNumber unique OTP verification number
     * @param sentAt             successful dispatch timestamp
     * @param updatedBy          actor performing the operation
     * @return updated OTP when the operation succeeds; otherwise empty
     */
    @Override
    public Optional<EmailVerificationOtpEntity> markEmailVerificationOtpSent(
            final String verificationNumber,
            final LocalDateTime sentAt,
            final String updatedBy) {

        final Document criteria = new Document()
                .append("verificationNumber", verificationNumber)
                .append("used", false)
                .append("revoked", false)
                .append("sentAt", null)
                .append("expiresAt", new Document("$gt", sentAt))
                .append("attemptsExceededAt", null)
                .append(EntityFieldConstants.DELETED_FLAG, RepositoryConstants.ACTIVE)
                .append("$expr", new Document(
                        "$lt",
                        List.of("$attemptCount", "$maxAttempts")));

        final Query query = new BasicQuery(criteria);

        final Update update = new Update()
                .set("sentAt", sentAt)
                .set("updatedAt", sentAt)
                .set("updatedBy", updatedBy);

        final EmailVerificationOtpEntity updatedOtp = mongoOperations.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true),
                EmailVerificationOtpEntity.class);

        return Optional.ofNullable(updatedOtp);
    }

    /**
     * Revokes all active email verification OTPs associated with an
     * existing user.
     *
     * <p>
     * All matching OTP records are updated using a single bulk database
     * operation. Previously consumed or revoked OTPs remain unchanged.
     * </p>
     *
     * <p>
     * The query explicitly excludes registration-associated OTPs.
     * </p>
     *
     * @param userNumber unique user number
     * @param revokedAt  revocation timestamp
     * @param updatedBy  actor performing the operation
     * @return number of OTP records successfully modified
     */
    @Override
    public long revokeActiveOtpsByUserNumber(
            final String userNumber,
            final LocalDateTime revokedAt,
            final String updatedBy) {

        final Document criteria = new Document()
                .append("userNumber", userNumber)
                .append("registrationNumber", null)
                .append("used", false)
                .append("revoked", false)
                .append(EntityFieldConstants.DELETED_FLAG, RepositoryConstants.ACTIVE);

        final Query query = new BasicQuery(criteria);

        final Update update = new Update()
                .set("revoked", true)
                .set("revokedAt", revokedAt)
                .set("updatedAt", revokedAt)
                .set("updatedBy", updatedBy);

        return mongoOperations.updateMulti(
                query,
                update,
                EmailVerificationOtpEntity.class).getModifiedCount();
    }

    /**
     * Revokes all active email verification OTPs associated with a
     * pending registration.
     *
     * <p>
     * All matching OTP records are updated using a single bulk database
     * operation. Previously consumed or revoked OTPs remain unchanged.
     * </p>
     *
     * <p>
     * The query explicitly excludes permanent-user-associated OTPs.
     * </p>
     *
     * @param registrationNumber unique pending registration number
     * @param revokedAt          revocation timestamp
     * @param updatedBy          actor performing the operation
     * @return number of OTP records successfully modified
     */
    @Override
    public long revokeActiveOtpsByRegistrationNumber(
            final String registrationNumber,
            final LocalDateTime revokedAt,
            final String updatedBy) {

        final Document criteria = new Document()
                .append("registrationNumber", registrationNumber)
                .append("userNumber", null)
                .append("used", false)
                .append("revoked", false)
                .append(EntityFieldConstants.DELETED_FLAG, RepositoryConstants.ACTIVE);

        final Query query = new BasicQuery(criteria);

        final Update update = new Update()
                .set("revoked", true)
                .set("revokedAt", revokedAt)
                .set("updatedAt", revokedAt)
                .set("updatedBy", updatedBy);

        return mongoOperations.updateMulti(
                query,
                update,
                EmailVerificationOtpEntity.class).getModifiedCount();
    }
}
