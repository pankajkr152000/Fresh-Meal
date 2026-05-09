package com.foodies.freshmeal.user.repository.impl;


import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.common.repository.base.impl.BaseRepositoryImpl;
import com.foodies.freshmeal.user.entity.impl.UserProfile;
import com.foodies.freshmeal.user.repository.UserProfileMongoRepository;




/**
 * =====================================================
 * User Profile Repository
 * =====================================================
 */
@Repository
public class UserProfileRepository
        extends BaseRepositoryImpl<UserProfile, String> {

    public UserProfileRepository(UserProfileMongoRepository repository) {

        super(repository);
    }
}
