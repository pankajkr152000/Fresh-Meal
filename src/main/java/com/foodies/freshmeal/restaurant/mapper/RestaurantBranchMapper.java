package com.foodies.freshmeal.restaurant.mapper;

import java.util.stream.Collectors;
import java.util.stream.Stream;

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
 * <p>
 * Provides pure object transformations between {@link RestaurantBranchEntity}
 * and Restaurant Branch API representations.
 * </p>
 *
 * <p>
 * The mapper is responsible only for object transformation and presentation
 * formatting. Business rules and persistence operations remain outside this
 * class.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Component
public class RestaurantBranchMapper {

    /**
     * Maps a {@link RestaurantBranchCreateRequest} to a
     * {@link RestaurantBranchEntity}.
     *
     * <p>
     * Server-managed fields such as <code>branchNumber</code> and common audit
     * information are intentionally not populated here. They are assigned by
     * the service/domain infrastructure.
     * </p>
     *
     * @param request branch creation request
     *
     * @return branch entity
     */
    public RestaurantBranchEntity toEntity(RestaurantBranchCreateRequest request) {

        RestaurantBranchEntity entity = (RestaurantBranchEntity) RestaurantBranchEntity.create();

        entity.setRestaurantId(request.getRestaurantId());
        entity.setBranchName(request.getBranchName());
        entity.setAddress(request.getAddress());
        entity.setOperatingHours(request.getOperatingHours());

        return entity;
    }

    /**
     * Maps a {@link RestaurantBranchEntity} to the lightweight list
     * representation.
     *
     * @param entity branch entity
     *
     * @return branch list response
     */
    public RestaurantBranchListResponse toListResponse(RestaurantBranchEntity entity) {

        return RestaurantBranchListResponse.builder()
                .id(entity.getId())
                .branchNumber(entity.getBranchNumber())
                .restaurantId(entity.getRestaurantId())
                .branchName(entity.getBranchName())
                .addressSummary(buildAddressSummary(entity))
                .status(DisplayOptionMapperUtil.from(entity.getRecordStatus()))
                .isAvailable(!entity.isDeleted())
                .build();
    }

    /**
     * Maps a {@link RestaurantBranchEntity} to the detailed branch
     * representation.
     *
     * @param entity branch entity
     *
     * @return branch details response
     */
    public RestaurantBranchDetailsResponse toDetailsResponse(RestaurantBranchEntity entity) {

        return RestaurantBranchDetailsResponse.builder()
                .id(entity.getId())
                .branchNumber(entity.getBranchNumber())
                .restaurantId(entity.getRestaurantId())
                .branchName(entity.getBranchName())
                .address(entity.getAddress())
                .operatingHours(entity.getOperatingHours())
                .status(DisplayOptionMapperUtil.from(entity.getRecordStatus()))
                .statusUpdatedAt(entity.getUpdatedAt())
                .statusUpdatedBy(entity.getUpdatedBy())
                .isAvailable(!entity.isDeleted())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    /**
     * Builds a concise human-readable address representation for list
     * responses.
     *
     * <p>
     * Empty address components are ignored so that the resulting summary
     * remains readable even when optional address fields are not supplied.
     * </p>
     *
     * @param entity branch entity
     *
     * @return formatted address summary, or <code>null</code> when address is
     *         unavailable
     */
    private String buildAddressSummary(RestaurantBranchEntity entity) {

        if (entity.getAddress() == null) {
            return null;
        }

        var address = entity.getAddress();

        return Stream.of(
                address.getHouseNumber(),
                address.getApartmentName(),
                address.getStreet(),
                address.getArea(),
                address.getCity(),
                address.getDistrict(),
                address.getState(),
                address.getPincode())
                .filter(value -> value != null && !value.isBlank())
                .collect(Collectors.joining(", "));
    }
}