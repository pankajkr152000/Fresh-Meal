package com.foodies.freshmeal.common.config;

import org.bson.BsonDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;

/**
 * =====================================================
 * Mongo Command Listener
 * =====================================================
 *
 * This listener logs every MongoDB operation:
 *
 * - find
 * - insert
 * - update
 * - delete
 * - aggregate
 *
 * Helps track:
 *
 * - which service triggered query
 * - actual Mongo query
 * - execution timing
 * - debugging
 *
 * =====================================================
 */
public class MongoLoggingListener implements CommandListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoLoggingListener.class);

    /**
     * Called before Mongo command execution.
     */
    @Override
    public void commandStarted(
            CommandStartedEvent event) {

        String commandName = event.getCommandName();

        BsonDocument command = event.getCommand();

        LOGGER.info("""
                \n====================================================\n\
                MONGO COMMAND STARTED\n\
                COMMAND : {}\n\
                QUERY   : {}\n\
                ====================================================\
                """,
                commandName,
                command.toJson());
    }

    /**
     * Called after successful execution.
     */
    @Override
    public void commandSucceeded(
            CommandSucceededEvent event) {

        LOGGER.info("""
                \n====================================================\n\
                MONGO COMMAND SUCCESS\n\
                COMMAND  : {}\n\
                DURATION : {} ms\n\
                ====================================================\
                """,
                event.getCommandName(),
                event.getElapsedTime(
                        java.util.concurrent.TimeUnit.MILLISECONDS));
    }

    /**
     * Called when Mongo command fails.
     */
    @Override
    public void commandFailed(
            CommandFailedEvent event) {

        LOGGER.error("""
                \n====================================================\n\
                MONGO COMMAND FAILED\n\
                COMMAND : {}\n\
                ERROR   : {}\n\
                ====================================================\
                """,
                event.getCommandName(),
                event.getThrowable().getMessage());
    }
}