package com.foodies.freshmeal.user.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.common.valueObject.GeoLocation;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Entity : AddressEntity
 * ============================================================================
 *
 * Represents a customer's saved delivery address.
 *
 * <p>
 * Addresses are maintained separately from UserEntity so that a customer can
 * maintain multiple addresses and each address can have its own lifecycle.
 * </p>
 *
 * <p>
 * An Order must never depend on the mutable AddressEntity after the order has
 * been placed. The Order service creates an immutable address snapshot inside
 * the OrderEntity.
 * </p>
 *
 * ============================================================================
 *
 * Address Lifecycle
 * -----------------
 *
 * User
 * |
 * +---- Home Address
 * |
 * +---- Work Address
 * |
 * +---- Other Address
 *
 * Order
 * |
 * +---- AddressSnapshot
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@Document(collection = "fm_addresses")
public class AddressEntity extends ABaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Package-private constructor.
     *
     * <p>
     * Entity creation should happen through the factory method.
     * </p>
     */
    AddressEntity() {
        // Package-private constructor.
    }

    /**
     * Factory method.
     *
     * @return new AddressEntity instance.
     */
    public static IEntity create() {
        return new AddressEntity();
    }

    // =========================================================================
    // Business Identifier
    // =========================================================================

    /**
     * Business-facing address identifier.
     *
     * Example:
     *
     * FM-ADR-0000001
     */
    @Indexed(unique = true)
    private String addressNumber;

    // =========================================================================
    // Ownership
    // =========================================================================

    /**
     * Business identifier of the user who owns this address.
     */
    @Indexed
    private String userNumber;

    // =========================================================================
    // Address Classification
    // =========================================================================

    /**
     * User-defined address label.
     *
     * Examples:
     *
     * HOME
     * WORK
     * OTHER
     */
    private String addressType;

    /**
     * Indicates whether this is the customer's default address.
     */
    private boolean defaultAddress;

    // =========================================================================
    // Address Details
    // =========================================================================

    /**
     * Recipient / contact person name.
     */
    private String recipientName;

    /**
     * Contact phone number for delivery.
     */
    private String phoneNumber;

    /**
     * Flat, apartment, house or building number.
     */
    private String addressLine1;

    /**
     * Street, road, locality or area.
     */
    private String addressLine2;

    /**
     * Landmark near the address.
     */
    private String landmark;

    /**
     * City.
     */
    private String city;

    /**
     * State.
     */
    private String state;

    /**
     * Country.
     */
    private String country;

    /**
     * Postal / PIN code.
     */
    private String postalCode;

    // =========================================================================
    // Location
    // =========================================================================

    /**
     * GeoLocation
     * Latitude & Longitude of the delivery location.
     *
     * <p>
     * Optional because exact coordinates may not always be available.
     * </p>
     */
    private GeoLocation location;

}