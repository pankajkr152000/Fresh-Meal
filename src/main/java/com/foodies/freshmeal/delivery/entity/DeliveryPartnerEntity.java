package com.foodies.freshmeal.delivery.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.delivery.constants.DeliveryPartnerStatus;
import com.foodies.freshmeal.delivery.constants.DeliveryPartnerVerificationStatus;
import com.foodies.freshmeal.delivery.constants.VehicleType;
import com.foodies.freshmeal.user.entity.UserEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Entity : Delivery Partner
 * ============================================================================
 *
 * Represents a delivery partner registered on the FreshMeal platform.
 *
 * <p>
 * A {@code DeliveryPartnerEntity} represents the current business and
 * operational profile of a delivery partner. The underlying user identity,
 * authentication credentials, account state, and roles remain managed by
 * {@link com.foodies.freshmeal.user.entity.UserEntity}.
 * </p>
 *
 * <p>
 * This entity is intentionally separate from
 * {@code DeliveryPartnerSnapshot}. The snapshot is used by the Order module
 * to preserve historical delivery-partner information at the time an order
 * is assigned, whereas this entity represents the partner's current state.
 * </p>
 *
 * <h3>Relationship with UserEntity</h3>
 *
 * <p>
 * Each delivery partner is associated with exactly one FreshMeal user through
 * {@code userNumber}. A user may later hold multiple FreshMeal roles, but the
 * delivery-partner profile remains a separate domain representation.
 * </p>
 *
 * <h3>Status and Availability</h3>
 *
 * <p>
 * {@code verificationStatus} represents the verification lifecycle of the
 * delivery partner, while {@code status} represents the business lifecycle.
 * {@code isAvailable} represents the partner's current operational
 * availability and is therefore intentionally kept separate from status.
 * </p>
 *
 * <p>
 * A newly created delivery-partner profile starts in a pending and unavailable
 * state. Activation and availability are controlled by the delivery-partner
 * business workflow.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@Document(collection = "fm_delivery_partner")
public class DeliveryPartnerEntity extends ABaseEntity {

    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Business Identity
    // =========================================================================

    /**
     * External/business identifier of the delivery partner.
     *
     * <p>
     * This identifier may be exposed to APIs, frontend applications,
     * operational workflows, reports, and other business-facing processes.
     * </p>
     *
     * <p>
     * Example:
     * {@code FM-DP-0000001}
     * </p>
     */
    @Indexed(unique = true)
    private String partnerNumber;

    /**
     * Business identifier of the FreshMeal user associated with this delivery
     * partner profile.
     *
     * <p>
     * References the business identifier of the associated
     * {@link UserEntity} rather than duplicating
     * user identity information in this entity.
     * </p>
     */
    @Indexed(unique = true)
    private String userNumber;

    /**
     * Operational/business code assigned to the delivery partner.
     *
     * <p>
     * This code may be used by restaurant operations, delivery operations,
     * customer support, reports, and internal workflows.
     * </p>
     *
     * <p>
     * Example:
     * {@code DP-KOL-00001}
     * </p>
     */
    @Indexed(unique = true)
    private String partnerCode;

    // =========================================================================
    // Vehicle Information
    // =========================================================================

    /**
     * Vehicle registration number used by the delivery partner.
     */
    private String vehicleNumber;

    /**
     * Type of vehicle currently used by the delivery partner.
     */
    private VehicleType vehicleType;

    // =========================================================================
    // Verification
    // =========================================================================

    /**
     * Current verification state of the delivery partner.
     *
     * <p>
     * Newly created delivery-partner profiles remain pending until the
     * required verification workflow is completed.
     * </p>
     */
    @Field("verification_status")
    private DeliveryPartnerVerificationStatus verificationStatus = DeliveryPartnerVerificationStatus.PENDING;

    // =========================================================================
    // Business Status
    // =========================================================================

    /**
     * Current business lifecycle status of the delivery partner.
     *
     * <p>
     * This is intentionally separate from verification status and operational
     * availability.
     * </p>
     */
    @Field("status")
    private DeliveryPartnerStatus status = DeliveryPartnerStatus.PENDING;

    /**
     * Indicates whether the delivery partner is currently available for
     * delivery assignments.
     *
     * <p>
     * A partner may be {@code ACTIVE} while temporarily unavailable.
     * </p>
     */
    @Field("delivery_partner_availability")
    private boolean isAvailable = false;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Package-private constructor.
     *
     * <p>
     * Entity creation should happen through the entity factory.
     * </p>
     */
    DeliveryPartnerEntity() {
        // Package-private constructor.
    }

    // =========================================================================
    // Factory
    // =========================================================================

    /**
     * Creates a new {@code DeliveryPartnerEntity} instance.
     *
     * @return new delivery partner entity.
     */
    public static IEntity create() {
        return new DeliveryPartnerEntity();
    }
}