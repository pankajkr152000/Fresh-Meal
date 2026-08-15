package com.foodies.freshmeal.user.entity;

import java.util.Collection;
import java.util.Collections;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import com.foodies.freshmeal.common.constants.RoleType;
import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.image.entity.ImageEntity;
import com.foodies.freshmeal.user.valueObject.LoginHistorySnapshot;

import lombok.Getter;
import lombok.Setter;

/**
 * =====================================================
 * User Profile Entity
 * =====================================================
 *
 * MongoDB collection for application users.
 *
 * Handles:
 * - Authentication
 * - Authorization
 * - Login History
 * - User Metadata
 *
 * =====================================================
 */
@Getter
@Setter
@Document(collection = "fm_user_profiles")
public class UserProfile extends ABaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 4377183688839034087L;

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
    UserProfile() {
        // Package-private constructor.
        // Entity creation should happen only through factory.
    }

    /**
     * =====================================================
     * Factory Method
     * =====================================================
     */
    public static IEntity create() {
        return new UserProfile();
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
    private String userProfileNumber;

    /**
     * =====================================================
     * Authentication Fields
     * =====================================================
     */
    private String userName;

    private String password;

    private boolean authenticated;

    /**
     * =====================================================
     * User Details
     * =====================================================
     */
    private UserEntity userEntity;

    private LoginHistorySnapshot loginHistory;

    private ImageEntity profileImage;

    private RoleType userRoleType;

    /**
     * =====================================================
     * Roles / Authorities
     * =====================================================
     */
    private Collection<? extends GrantedAuthority> authorities = Collections.emptyList();

    /**
     * =====================================================
     * IEntity
     * =====================================================
     */

    /**
     * =====================================================
     * Authentication
     * =====================================================
     */

    public ImageEntity getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ImageEntity profileImage) {
        this.profileImage = profileImage;
    }

}