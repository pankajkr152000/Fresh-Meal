package com.foodies.freshmeal.delivery.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.foodies.freshmeal.delivery.dto.DeliveryPartnerRegistrationRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerResponse;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerUpdateRequest;
import com.foodies.freshmeal.delivery.entity.DeliveryPartnerEntity;

/**
 * ============================================================================
 * Mapper : Delivery Partner
 * ============================================================================
 *
 * <p>
 * Centralizes entity-to-DTO and DTO-to-entity transformations for the Delivery
 * Partner domain.
 * </p>
 *
 * <p>
 * This mapper is intentionally limited to data transformation. Business rules,
 * authorization, repository access, identifier generation, verification logic,
 * status transitions, and availability decisions remain outside this component.
 * </p>
 *
 * <h3>Supported Transformations</h3>
 *
 * <ul>
 * <li>Registration request → entity editable fields</li>
 * <li>Update request → entity editable fields</li>
 * <li>Entity → response</li>
 * <li>Entity list → response list</li>
 * </ul>
 *
 * <p>
 * System-controlled fields such as partner number, partner code, user number,
 * verification status, business status, availability, and audit information
 * are not populated from client request DTOs.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Component
public class DeliveryPartnerMapper {

    // =========================================================================
    // Request → Entity
    // =========================================================================

    /**
     * Maps delivery-partner registration data to an entity's editable fields.
     *
     * <p>
     * System-controlled fields are intentionally excluded.
     * </p>
     *
     * @param request registration request
     * @param entity  target delivery-partner entity
     */
    public void mapRegistrationRequestToEntity(
            final DeliveryPartnerRegistrationRequest request,
            final DeliveryPartnerEntity entity) {

        if (request == null || entity == null) {
            return;
        }

        entity.setVehicleNumber(request.getVehicleNumber());
        entity.setVehicleType(request.getVehicleType());
    }

    /**
     * Maps delivery-partner update data to an existing entity.
     *
     * @param request update request
     * @param entity  target delivery-partner entity
     */
    public void mapUpdateRequestToEntity(
            final DeliveryPartnerUpdateRequest request,
            final DeliveryPartnerEntity entity) {

        if (request == null || entity == null) {
            return;
        }

        entity.setVehicleNumber(request.getVehicleNumber());
        entity.setVehicleType(request.getVehicleType());
    }

    // =========================================================================
    // Entity → Response
    // =========================================================================

    /**
     * Maps a delivery-partner entity to its API response representation.
     *
     * @param entity delivery-partner entity
     * @return delivery-partner response
     */
    public DeliveryPartnerResponse toResponse(
            final DeliveryPartnerEntity entity) {

        if (entity == null) {
            return null;
        }

        return DeliveryPartnerResponse.builder()
                .partnerNumber(entity.getPartnerNumber())
                .userNumber(entity.getUserNumber())
                .partnerCode(entity.getPartnerCode())
                .vehicleNumber(entity.getVehicleNumber())
                .vehicleType(entity.getVehicleType())
                .verificationStatus(entity.getVerificationStatus())
                .status(entity.getStatus())
                .available(entity.isAvailable())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Maps a list of delivery-partner entities to response DTOs.
     *
     * @param entities delivery-partner entities
     * @return delivery-partner responses
     */
    public List<DeliveryPartnerResponse> toResponseList(
            final List<DeliveryPartnerEntity> entities) {

        if (entities == null || entities.isEmpty()) {
            return List.of();
        }

        return entities.stream()
                .map(this::toResponse)
                .toList();
    }
}