package com.foodies.freshmeal.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * ============================================================================
 * Configuration : MongoTransactionConfig
 * ============================================================================
 *
 * <p>
 * Configures MongoDB transaction management for FreshMeal.
 * </p>
 *
 * <p>
 * MongoDB transactions are required for business operations where multiple
 * related documents must be persisted atomically. For example, restaurant
 * onboarding creates both a {@code RestaurantEntity} and its initial
 * {@code RestaurantBranchEntity}. Both operations must succeed together.
 * </p>
 *
 * <p>
 * If any operation participating in a transaction fails, the transaction
 * manager allows the changes made within that transaction to be rolled back.
 * This prevents partially completed business operations.
 * </p>
 *
 * <p>
 * The configuration deliberately uses Spring Data MongoDB's native
 * {@link MongoTransactionManager} rather than introducing a custom transaction
 * abstraction.
 * </p>
 *
 * <p>
 * The MongoDB deployment must support transactions. In local development,
 * MongoDB must therefore run as a replica set or through a transaction-capable
 * deployment configuration.
 * </p>
 *
 * ============================================================================
 *
 * @author FreshMeal Development Team
 * @since 1.0
 */
@Configuration
@EnableTransactionManagement
public class MongoTransactionConfig {

    /**
     * Creates the MongoDB transaction manager.
     *
     * <p>
     * Spring uses this transaction manager when a service method is marked with
     * {@code @Transactional}. The transaction boundary is therefore kept at
     * the service layer rather than inside repositories or controllers.
     * </p>
     *
     * @param mongoDatabaseFactory MongoDB database factory managed by Spring.
     *
     * @return configured MongoDB transaction manager.
     */
    @Bean
    public MongoTransactionManager mongoTransactionManager(
            final MongoDatabaseFactory mongoDatabaseFactory) {

        return new MongoTransactionManager(mongoDatabaseFactory);
    }
}