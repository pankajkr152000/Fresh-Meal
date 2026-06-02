package com.foodies.freshmeal.common.sequence.service;

import com.foodies.freshmeal.common.io.service.IServiceContext;

public interface IDatabaseSequenceService {

    /**
     * Generates next sequence value.
     *
     * IMAGE_SEQUENCE -> 1,2,3...
     * USER_SEQUENCE -> 1,2,3...
     */
    long generateSequence(IServiceContext serviceContext, String sequenceName);

    /**
     * Returns current value without incrementing.
     */
    long getCurrentSequence(IServiceContext serviceContext, String sequenceName);

    /**
     * Sets sequence to a specific value.
     */
    void setSequence(IServiceContext serviceContext, String sequenceName, long value);

    /**
     * Resets sequence to zero.
     */
    void resetSequence(IServiceContext serviceContext, String sequenceName);

    /**
     * Deletes sequence document.
     */
    void deleteSequence(IServiceContext serviceContext, String sequenceName);

    /**
     * Checks existence.
     */
    boolean sequenceExists(IServiceContext serviceContext, String sequenceName);

    /**
     * Creates sequence if missing.
     */
    void createSequence(IServiceContext serviceContext, String sequenceName, long initialValue);

    /**
     * =====================================================
     *
     * Get Next Sequence Value
     *
     * =====================================================
     *
     * Returns next sequence value as String.
     */
    public Long getNextSequenceValue(IServiceContext serviceContext, String sequenceName);
}
