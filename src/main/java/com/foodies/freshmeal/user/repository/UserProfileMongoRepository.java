package com.foodies.freshmeal.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.foodies.freshmeal.user.entity.impl.UserProfile;




public interface UserProfileMongoRepository extends MongoRepository<UserProfile, String> {

}
