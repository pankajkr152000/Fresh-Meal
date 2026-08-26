package com.foodies.freshmeal.common.factory;

import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.common.enums.EntityName;
import com.foodies.freshmeal.common.sequence.entity.impl.DatabaseSequence;
import com.foodies.freshmeal.food.entity.FoodEntity;
import com.foodies.freshmeal.image.entity.ImageEntity;
import com.foodies.freshmeal.order.entity.OrderEntity;
import com.foodies.freshmeal.user.entity.AddressEntity;
import com.foodies.freshmeal.user.entity.LoginHistoryEntity;
import com.foodies.freshmeal.user.entity.UserEntity;
import com.foodies.freshmeal.user.entity.UserProfile;

/**
 * ============================================================================
 * Class : EntityFactory
 * ============================================================================
 *
 * Factory responsible for creating entity instances based on the specified
 * {@link EntityName}.
 *
 * <p>
 * This factory centralizes entity creation and provides a single entry point
 * for creating application entities.
 * </p>
 *
 * <p>
 * New entities should be registered here as they are introduced into the
 * application.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 *        ============================================================================
 */
public final class EntityFactory {

    /**
     * Private constructor.
     */
    private EntityFactory() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Creates a new entity instance for the given entity type.
     *
     * @param entityType entity to create
     * @return newly created entity instance
     * @throws IllegalArgumentException if the entity type is not supported
     */
    public static IEntity createEntity(EntityName entityType) {

        if (entityType == null) {
            throw new IllegalArgumentException("Entity type must not be null.");
        }

        return switch (entityType) {

            case DATABASE_SEQUENCE ->
                DatabaseSequence.create();

            case USER_ENTITY ->
                UserEntity.create();

            case USER_PROFILE_ENTITY ->
                UserProfile.create();

            case LOGIN_HISTORY_ENTITY ->
                LoginHistoryEntity.create();

            case FOOD_ENTITY ->
                FoodEntity.create();

            case ORDER_ENTITY ->
                OrderEntity.create();

            case IMAGE_ENTITY ->
                ImageEntity.create();

            case ADDRESS_ENTITY ->
                AddressEntity.create();

            default ->
                throw new IllegalArgumentException(
                        "Unsupported entity type : " + entityType);
        };
    }

}