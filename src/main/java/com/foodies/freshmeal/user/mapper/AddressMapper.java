package com.foodies.freshmeal.user.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.foodies.freshmeal.user.dto.AddressRequest;
import com.foodies.freshmeal.user.dto.AddressResponse;
import com.foodies.freshmeal.user.dto.AddressUpdateRequest;
import com.foodies.freshmeal.user.entity.AddressEntity;

/**
 * ============================================================================
 * Mapper : AddressMapper
 * ============================================================================
 *
 * Responsible for converting Address DTOs to AddressEntity instances and
 * AddressEntity instances to AddressResponse objects.
 *
 * <p>
 * This mapper is intentionally limited to data transformation. Business
 * rules such as ownership validation, pincode resolution, address lifecycle,
 * default-address management and identifier generation belong to the service
 * layer.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Component
public class AddressMapper {

    /**
     * Maps an address creation request to an existing AddressEntity.
     *
     * <p>
     * System-controlled fields such as ID, address number, user number,
     * created-by information and pincode-derived location details are not
     * populated here.
     * </p>
     *
     * @param request address creation request
     * @param entity  address entity
     */
    public void mapRequestToEntity(
            final AddressRequest request,
            final AddressEntity entity) {

        if (request == null || entity == null) {
            return;
        }

        entity.setAddressType(request.getAddressType());
        entity.setDefaultAddress(request.isDefaultAddress());
        entity.setRecipientName(request.getRecipientName());
        entity.setPhoneNumber(request.getPhoneNumber());
        entity.setAddressLine1(request.getAddressLine1());
        entity.setAddressLine2(request.getAddressLine2());
        entity.setLandmark(request.getLandmark());
        entity.setCity(request.getCity());
        entity.setPostalCode(request.getPostalCode());
    }

    /**
     * Maps an address update request to an existing AddressEntity.
     *
     * <p>
     * The address number is used to identify the existing entity and is
     * therefore not overwritten by the mapper.
     * </p>
     *
     * <p>
     * System-controlled fields and pincode-derived location information are
     * intentionally not handled by this mapper.
     * </p>
     *
     * @param request address update request
     * @param entity  existing address entity
     */
    public void mapUpdateRequestToEntity(
            final AddressUpdateRequest request,
            final AddressEntity entity) {

        if (request == null || entity == null) {
            return;
        }

        entity.setAddressType(request.getAddressType());
        entity.setRecipientName(request.getRecipientName());
        entity.setPhoneNumber(request.getPhoneNumber());
        entity.setAddressLine1(request.getAddressLine1());
        entity.setAddressLine2(request.getAddressLine2());
        entity.setLandmark(request.getLandmark());
        entity.setCity(request.getCity());
        entity.setPostalCode(request.getPostalCode());
    }

    /**
     * Converts an AddressEntity to AddressResponse.
     *
     * @param entity address entity
     * @return mapped address response, or {@code null} when the entity is null
     */
    public AddressResponse toResponse(final AddressEntity entity) {

        if (entity == null) {
            return null;
        }

        return AddressResponse.builder()
                .addressNumber(entity.getAddressNumber())
                .addressType(entity.getAddressType())
                .defaultAddress(entity.isDefaultAddress())
                .recipientName(entity.getRecipientName())
                .phoneNumber(entity.getPhoneNumber())
                .addressLine1(entity.getAddressLine1())
                .addressLine2(entity.getAddressLine2())
                .landmark(entity.getLandmark())
                .city(entity.getCity())
                .district(entity.getDistrict())
                .state(entity.getState())
                .country(entity.getCountry())
                .postalCode(entity.getPostalCode())
                .location(entity.getLocation())
                .build();
    }

    /**
     * Converts a list of AddressEntity objects to AddressResponse objects.
     *
     * @param entities address entities
     * @return mapped address responses
     */
    public List<AddressResponse> toResponseList(
            final List<AddressEntity> entities) {

        if (entities == null || entities.isEmpty()) {
            return List.of();
        }

        return entities.stream()
                .map(this::toResponse)
                .toList();
    }
}