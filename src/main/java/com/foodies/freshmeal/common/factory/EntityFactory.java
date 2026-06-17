package com.foodies.freshmeal.common.factory;

import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.common.entity.impl.LoginHistory;
import com.foodies.freshmeal.common.enums.EntityName;
import com.foodies.freshmeal.common.sequence.entity.impl.DatabaseSequence;
import com.foodies.freshmeal.food.entity.FoodEntity;
import com.foodies.freshmeal.image.entity.ImageEntity;
import com.foodies.freshmeal.user.entity.UserEntity;
import com.foodies.freshmeal.user.entity.UserProfile;

public final class EntityFactory {

    private EntityFactory() {
    }

    public static IEntity createEntity(EntityName entityType) {

        switch (entityType) {

            case USER_PROFILE -> {
                return UserProfile.create();
            }
            case DATABASE_SEQUENCE -> {
                return DatabaseSequence.create();
            }
            case USER_ENTITY -> {
                return UserEntity.create();
            }
            case IMAGE_ENTITY -> {
                return ImageEntity.create();
            }
            case LOGIN_HISTORY -> {
            	return LoginHistory.create();
            }
            case FOOD_ENTITY -> {
                return FoodEntity.create();
            }

            default -> throw new IllegalArgumentException(
                    "Unsupported entity type : " + entityType);
        }
    }
}