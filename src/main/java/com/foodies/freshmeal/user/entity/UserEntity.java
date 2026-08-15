package com.foodies.freshmeal.user.entity;

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "fm_users")
public class UserEntity extends ABaseEntity {

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
    // @Id
    // private String id;

    /**
     * External/business identifier of the food.
     *
     * <p>
     * This identifier is unique and may be exposed to the frontend,
     * URLs, reports, and other business-facing operations.
     * </p>
     */
    @Indexed(unique = true)
    private String userNumber;

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    /**
     * Business identifiers of addresses owned by this user.
     *
     * <p>
     * Address details are maintained in AddressEntity.
     * </p>
     */
    private List<String> addressNumbers;
    private List<String> roles;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

}
