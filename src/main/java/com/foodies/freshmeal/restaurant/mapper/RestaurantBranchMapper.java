package com.foodies.freshmeal.restaurant.mapper;

import org.springframework.stereotype.Component;

import com.foodies.freshmeal.common.util.DisplayOptionMapperUtil;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchCreateRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchDetailsResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchListResponse;
import com.foodies.freshmeal.restaurant.entity.RestaurantBranchEntity;

/**
 * ============================================================================
 * Mapper : Restaurant Branch
 * ============================================================================
 *
 * Provides pure object transformations between RestaurantBranchEntity and
 * Restaurant Branch API representations.
 *
 * ============================================================================
 */
@Component
public class RestaurantBranchMapper {

    /**
     * Maps RestaurantBranchCreateRequest to RestaurantBranchEntity.
     *
     * <p>
     * Server-managed fields such as branchNumber, status, availability,
     * and audit information are intentionally not populated here.
     * </p>
     *
     * @param request branch creation request
     *
     * @return branch entity
     */
    public RestaurantBranchEntity toEntity(
            RestaurantBranchCreateRequest request) {

        RestaurantBranchEntity entity = (RestaurantBranchEntity) RestaurantBranchEntity.create();

        entity.setRestaurantId(request.getRestaurantId());
        entity.setBranchName(request.getBranchName());
        entity.setAddress(request.getAddress());
        entity.setOperatingHours(request.getOperatingHours());

        return entity;
    }

    /**
     * Maps RestaurantBranchEntity to the lightweight list projection.
     *
     * @param entity branch entity
     *
     * @return branch list response
     */
    public RestaurantBranchListResponse toListResponse(
            RestaurantBranchEntity entity) {

        return RestaurantBranchListResponse.builder()
                .id(entity.getId())
                .branchNumber(entity.getBranchNumber())
                .restaurantId(entity.getRestaurantId())
                .branchName(entity.getBranchName())
                .addressSummary(buildAddressSummary(entity))
                .status(
                        DisplayOptionMapperUtil.from(
                                entity.getStatus()))
                .isAvailable(entity.isAvailable())
                .build();
    }

    /**
     * Maps RestaurantBranchEntity to the detailed branch projection.
     *
     * @param entity branch entity
     *
     * @return branch details response
     */
    public RestaurantBranchDetailsResponse toDetailsResponse(
            RestaurantBranchEntity entity) {

        return RestaurantBranchDetailsResponse.builder()
                .id(entity.getId())
                .branchNumber(entity.getBranchNumber())
                .restaurantId(entity.getRestaurantId())
                .branchName(entity.getBranchName())
                .address(entity.getAddress())
                .operatingHours(entity.getOperatingHours())
                .status(
                        DisplayOptionMapperUtil.from(
                                entity.getStatus()))
                .statusUpdatedAt(entity.getStatusUpdatedAt())
                .statusUpdatedBy(entity.getStatusUpdatedBy())
                .isAvailable(entity.isAvailable())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    /**
     * Builds a concise address representation for list responses.
     *
     * @param entity branch entity
     *
     * @return formatted address summary
     */
    private String buildAddressSummary(
            RestaurantBranchEntity entity) {

        if (entity.getAddress() == null) {
            return null;
        }

        var address = entity.getAddress();

        return java.util.stream.Stream.of(
                address.getHouseNumber(),
                address.getApartmentName(),
                address.getStreet(),
                address.getArea(),
                address.getCity(),
                address.getDistrict(),
                address.getState(),
                address.getPincode())
                .filter(value -> value != null && !value.isBlank())
                .collect(java.util.stream.Collectors.joining(", "));
    }
}