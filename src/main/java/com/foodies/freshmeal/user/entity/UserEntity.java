package com.foodies.freshmeal.user.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.common.constants.RoleType;
import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.common.valueObject.EmailAddress;
import com.foodies.freshmeal.common.valueObject.PhoneNumber;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Entity : UserEntity
 * ============================================================================
 *
 * Represents a registered user of the FreshMeal application.
 *
 * <p>
 * {@code UserEntity} is the primary persisted business representation of a
 * FreshMeal user. It acts as the single source of truth for user identity,
 * contact information, account state, roles, credentials, and references to
 * user-owned addresses.
 * </p>
 *
 * <p>
 * Authentication and authorization frameworks such as Spring Security should
 * consume this entity through an appropriate service/projection rather than
 * making this entity itself dependent on framework-specific interfaces such as
 * {@code UserDetails} or {@code GrantedAuthority}.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Maintain the user's business identity.</li>
 * <li>Maintain authentication credentials.</li>
 * <li>Maintain contact information.</li>
 * <li>Maintain assigned FreshMeal roles.</li>
 * <li>Maintain account lifecycle and security state.</li>
 * <li>Maintain references to addresses owned by the user.</li>
 * </ul>
 *
 * <h3>Address Relationship</h3>
 * <p>
 * Address details are maintained separately by {@link AddressEntity}.
 * This entity stores only the corresponding business identifiers through
 * {@code addressNumbers}, avoiding unnecessary duplication of address data
 * inside the user document.
 * </p>
 *
 * <h3>Authentication</h3>
 * <p>
 * The {@code username} and {@code password} fields represent the persisted
 * authentication credentials. Password values must always be stored in an
 * appropriately encoded/hashed form and must never contain plain-text
 * passwords.
 * </p>
 *
 * <h3>Authorization</h3>
 * <p>
 * User roles are represented using {@link RoleType}. Spring Security
 * authorities can be derived from these roles at authentication time without
 * coupling the persistence entity to Spring Security.
 * </p>
 *
 * <h3>Account Lifecycle</h3>
 * <p>
 * The account-state flags provide explicit control over whether an account
 * is enabled, locked, expired, or has expired credentials. They also provide
 * a stable domain representation that can later be mapped to Spring
 * Security's account-state model.
 * </p>
 *
 * <h3>Business Identifier</h3>
 * <p>
 * {@code userNumber} is the external/business identifier of the user.
 * It is intended for business-facing operations, APIs, logs, reports, and
 * inter-domain references instead of exposing MongoDB's internal identifier.
 * </p>
 *
 * <p>
 * Example:
 * </p>
 *
 * <pre>
 * FM - USR - 0000001
 * </pre>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@Document(collection = "fm_users")
public class UserEntity extends ABaseEntity {

    private static final long serialVersionUID = 4377183688839034088L;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Package-private constructor.
     *
     * <p>
     * Entity creation should happen through the factory method so that object
     * creation remains controlled and can evolve without exposing the
     * persistence constructor publicly.
     * </p>
     */
    UserEntity() {
        // Package-private constructor.
    }

    // =========================================================================
    // Factory
    // =========================================================================

    /**
     * Creates a new {@link UserEntity}.
     *
     * @return new user entity instance.
     */
    public static IEntity create() {
        return new UserEntity();
    }

    // =========================================================================
    // Business Identifier
    // =========================================================================

    /**
     * Unique business-facing identifier of the user.
     *
     * <p>
     * This identifier may be exposed through APIs, reports, logs, and
     * inter-domain relationships without exposing the internal MongoDB
     * identifier.
     * </p>
     */
    @Indexed(unique = true)
    private String userNumber;

    // =========================================================================
    // Authentication Credentials
    // =========================================================================

    /**
     * Unique username used for authentication.
     *
     * <p>
     * The username should remain stable enough to serve as the primary
     * authentication identifier unless FreshMeal later introduces a dedicated
     * login-identity model.
     * </p>
     */
    @Indexed(unique = true)
    private String username;

    /**
     * Encoded password associated with the user account.
     *
     * <p>
     * Plain-text passwords must never be persisted.
     * Password encoding should be handled by the authentication/service layer.
     * </p>
     */
    private String password;

    // =========================================================================
    // Personal Information
    // =========================================================================

    /**
     * User's email address.
     *
     * <p>
     * Uses the common {@link EmailAddress} value object to keep email
     * representation and validation consistent across FreshMeal.
     * </p>
     */
    @Indexed(unique = true)
    private EmailAddress email;

    private boolean emailVerified = false;

    /**
     * User's first name.
     */
    private String firstName;

    /**
     * User's last name.
     */
    private String lastName;

    // =========================================================================
    // Contact Information
    // =========================================================================

    /**
     * User's phone number.
     *
     * <p>
     * Uses the common {@link PhoneNumber} value object for consistent
     * representation across the application.
     * </p>
     */
    @Indexed(unique = true)
    private PhoneNumber phoneNumber;

    // =========================================================================
    // Address References
    // =========================================================================

    /**
     * Business identifiers of addresses owned by this user.
     *
     * <p>
     * Complete address details are maintained by {@link AddressEntity}.
     * Only business identifiers are stored here to keep the user document
     * focused and avoid duplicating address information.
     * </p>
     */
    private List<String> addressNumbers = new ArrayList<>();

    // =========================================================================
    // Authorization
    // =========================================================================

    /**
     * Roles assigned to the user.
     *
     * <p>
     * These roles represent FreshMeal's domain-level authorization model.
     * Framework-specific authorities should be derived from these values by
     * the security layer.
     * </p>
     */
    private List<RoleType> roles = new ArrayList<>();

    // =========================================================================
    // Account State
    // =========================================================================

    /**
     * Indicates whether the user account has expired.
     *
     * <p>
     * {@code true} indicates that the account has not expired.
     * </p>
     */
    private boolean accountNonExpired = true;

    /**
     * Indicates whether the user account is locked.
     *
     * <p>
     * {@code true} indicates that the account is not locked.
     * </p>
     */
    private boolean accountNonLocked = true;

    /**
     * Indicates whether the user's credentials have expired.
     *
     * <p>
     * {@code true} indicates that the credentials are still valid.
     * </p>
     */
    private boolean credentialsNonExpired = true;

    /**
     * Indicates whether the user account is enabled.
     */
    private boolean enabled = true;
}