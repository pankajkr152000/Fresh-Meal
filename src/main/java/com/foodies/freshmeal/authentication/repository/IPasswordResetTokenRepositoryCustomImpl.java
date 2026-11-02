package com.foodies.freshmeal.authentication.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.foodies.freshmeal.authentication.entity.PasswordResetTokenEntity;
import com.foodies.freshmeal.common.constants.EntityFieldConstants;
import com.foodies.freshmeal.common.constants.RepositoryConstants;

/**
 * ============================================================================
 * Password Reset Token Custom Repository Implementation
 * ============================================================================
 *
 * Provides atomic MongoDB operations for password-reset token lifecycle
 * management.
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public class IPasswordResetTokenRepositoryCustomImpl implements IPasswordResetTokenRepositoryCustom {

    private final MongoOperations mongoOperations;

    /**
     * Creates the custom password-reset token repository.
     *
     * @param mongoOperations MongoDB operations.
     */
    public IPasswordResetTokenRepositoryCustomImpl(
            final MongoOperations mongoOperations) {

        this.mongoOperations = mongoOperations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PasswordResetTokenEntity> claimPasswordResetToken(
            final String tokenHash,
            final LocalDateTime now,
            final String updatedBy) {

        /*
         * Only eligible, active reset tokens can be claimed.
         */
        final Query query = Query.query(
                Criteria.where("tokenHash").is(tokenHash)
                        .and("used").is(false)
                        .and("revoked").is(false)
                        .and("sentAt").ne(null)
                        .and("expiresAt").gt(now)
                        .and(EntityFieldConstants.DELETED_FLAG)
                        .is(RepositoryConstants.ACTIVE));

        /*
         * Claim the token and update its audit information atomically.
         */
        final Update update = new Update()
                .set("used", true)
                .set("usedAt", now)
                .set("updatedAt", now)
                .set("updatedBy", updatedBy);

        /*
         * Return the updated document.
         *
         * MongoDB guarantees atomicity for this single-document operation.
         */
        final PasswordResetTokenEntity claimedToken = mongoOperations.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true),
                PasswordResetTokenEntity.class);

        return Optional.ofNullable(claimedToken);
    }
}