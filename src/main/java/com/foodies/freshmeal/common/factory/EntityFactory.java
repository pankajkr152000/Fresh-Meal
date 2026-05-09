package com.foodies.freshmeal.common.factory;

import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.common.enums.EntityName;
import com.foodies.freshmeal.common.sequence.entity.impl.DatabaseSequence;
import com.foodies.freshmeal.user.entity.impl.UserEntity;
import com.foodies.freshmeal.user.entity.impl.UserProfile;

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

            default -> throw new IllegalArgumentException(
                    "Unsupported entity type : " + entityType);
        }
    }
}