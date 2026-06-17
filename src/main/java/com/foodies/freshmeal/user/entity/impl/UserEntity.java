package com.foodies.freshmeal.user.entity.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "fm_users")
public class UserEntity extends ABaseEntity{

    /**
     *
     */
    private static final long serialVersionUID = 4377183688839034088L;

    /**
     * =====================================================
     * Constructor
     * =====================================================
     *
     * Package-private constructor.
     * Entity creation should happen only through factory.
     *
     * =====================================================
     */
    UserEntity() {
        // Package-private constructor.
        // Entity creation should happen only through factory.

    }

    /**
     * =====================================================
     * Factory Method
     * =====================================================
     */
    public static IEntity create() {
        return new UserEntity();
    }

    /**
     * =====================================================
     * Mongo Primary Key
     * =====================================================
     */
    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private List<String> address;
    private List<String> roles;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;


}
