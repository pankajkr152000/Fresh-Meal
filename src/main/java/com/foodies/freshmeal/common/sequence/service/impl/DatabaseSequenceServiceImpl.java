package com.foodies.freshmeal.common.sequence.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.foodies.freshmeal.common.enums.EntityName;
import com.foodies.freshmeal.common.factory.EntityFactory;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.sequence.entity.IDatabaseSequence;
import com.foodies.freshmeal.common.sequence.entity.impl.DatabaseSequence;
import com.foodies.freshmeal.common.sequence.service.IDatabaseSequenceService;

@Service
public class DatabaseSequenceServiceImpl implements IDatabaseSequenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSequenceServiceImpl.class);

    private final MongoOperations mongoOperations;

    public DatabaseSequenceServiceImpl(MongoOperations mongoOperations) {

        this.mongoOperations = mongoOperations;
    }

    /**
     * =====================================================
     * Generate Next Sequence
     * =====================================================
     *
     * Atomically increments sequence and returns
     * the next value.
     *
     * Example:
     *
     * IMAGE_SEQUENCE
     * 1 -> 2 -> 3 -> 4
     *
     * =====================================================
     */
    @Override
    public long generateSequence(IServiceContext serviceContext, String sequenceName) {
        LOGGER.info(
                "Generating next sequence for [{}]",
                sequenceName);

        Query query = new Query(
                Criteria.where("_id")
                        .is(sequenceName));

        Update update = new Update()
                .inc("seq", 1);

        DatabaseSequence counter = mongoOperations.findAndModify(
                query,
                update,
                FindAndModifyOptions.options()
                        .returnNew(true)
                        .upsert(true),
                DatabaseSequence.class);

        long nextValue = counter != null
                ? counter.getSeq()
                : 1L;

        LOGGER.info(
                "Generated sequence [{}] value [{}]",
                sequenceName,
                nextValue);
        return nextValue;
    }

    /**
     * =====================================================
     * Get Current Sequence
     * =====================================================
     *
     * Returns current value without incrementing.
     *
     * =====================================================
     */
    @Override
    public long getCurrentSequence(IServiceContext serviceContext,
            String sequenceName) {

        LOGGER.info(
                "Fetching current sequence [{}]",
                sequenceName);

        DatabaseSequence sequence = mongoOperations.findById(
                sequenceName,
                DatabaseSequence.class);

        if (sequence == null) {

            LOGGER.warn(
                    "Sequence [{}] not found",
                    sequenceName);

            return 0L;
        }

        return sequence.getSeq();
    }

    /**
     * =====================================================
     * Set Sequence
     * =====================================================
     *
     * Updates sequence value.
     *
     * =====================================================
     */
    @Override
    public void setSequence(IServiceContext serviceContext, 
            String sequenceName,
            long value) {

        LOGGER.info(
                "Setting sequence [{}] to [{}]",
                sequenceName,
                value);

        Query query = new Query(
                Criteria.where("_id")
                        .is(sequenceName));

        Update update = new Update()
                .set("seq", value);

        mongoOperations.upsert(
                query,
                update,
                DatabaseSequence.class);
    }

    /**
     * =====================================================
     * Reset Sequence
     * =====================================================
     *
     * Sets sequence value to zero.
     *
     * =====================================================
     */
    @Override
    public void resetSequence(IServiceContext serviceContext, 
            String sequenceName) {

        LOGGER.info(
                "Resetting sequence [{}]",
                sequenceName);

        setSequence(
                serviceContext,
                sequenceName,
                0L);
    }

    /**
     * =====================================================
     * Delete Sequence
     * =====================================================
     *
     * Removes sequence document.
     *
     * =====================================================
     */
    @Override
    public void deleteSequence(IServiceContext serviceContext, 
            String sequenceName) {

        LOGGER.info(
                "Deleting sequence [{}]",
                sequenceName);

        Query query = new Query(
                Criteria.where("_id")
                        .is(sequenceName));

        mongoOperations.remove(
                query,
                DatabaseSequence.class);
    }

    /**
     * =====================================================
     * Sequence Exists
     * =====================================================
     *
     * Checks whether sequence exists.
     *
     * =====================================================
     */
    @Override
    public boolean sequenceExists(IServiceContext serviceContext, 
            String sequenceName) {

        LOGGER.info(
                "Checking sequence existence [{}]",
                sequenceName);

        Query query = new Query(
                Criteria.where("_id")
                        .is(sequenceName));

        return mongoOperations.exists(
                query,
                DatabaseSequence.class);
    }

    /**
     * =====================================================
     * Create Sequence
     * =====================================================
     *
     * Creates sequence if not present.
     *
     * =====================================================
     */
    @Override
    public void createSequence(IServiceContext serviceContext, 
        String sequenceName,
        long initialValue) {
            
            LOGGER.info(
                "Creating sequence [{}] with initial value [{}]",
                sequenceName,
                initialValue);

        if (sequenceExists(serviceContext, sequenceName)) {
            
            LOGGER.warn(
                "Sequence [{}] already exists",
                sequenceName);
                
            return;
        }

        IDatabaseSequence sequence = (IDatabaseSequence) EntityFactory.createEntity(EntityName.DATABASE_SEQUENCE);
        
        sequence.setId(sequenceName);
        sequence.setSeq(
            initialValue);
            
            mongoOperations.save(sequence);
        }
        
        
        /**
         * =====================================================
         * Get Next Sequence Value
         * =====================================================
         *
         * Returns the next sequence value.
         *
         * =====================================================
         */
        @Override
        public Long getNextSequenceValue(IServiceContext serviceContext, String sequenceName) {

        return getCurrentSequence(serviceContext, sequenceName) + 1;
    }
}