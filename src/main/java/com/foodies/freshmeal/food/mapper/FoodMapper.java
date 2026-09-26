package com.foodies.freshmeal.food.mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.foodies.freshmeal.common.dto.DisplayOptionResponse;
import com.foodies.freshmeal.common.util.DisplayOptionMapperUtil;
import com.foodies.freshmeal.food.constants.FoodStatusConstant;
import com.foodies.freshmeal.food.dto.FoodRequest;
import com.foodies.freshmeal.food.dto.FoodResponse;
import com.foodies.freshmeal.food.dto.FoodStatusResponse;
import com.foodies.freshmeal.food.entity.FoodEntity;

/**
 * ============================================================================
 * Mapper : FoodMapper
 * ============================================================================
 *
 * <p>
 * Responsible for converting between Food request/response DTOs and the
 * corresponding Food entity.
 * </p>
 *
 * <p>
 * This mapper contains only transformation logic. It must not contain business
 * rules, authorization logic, repository access, sequence generation, image
 * upload operations, restaurant resolution or persistence operations.
 * </p>
 *
 * <p>
 * Restaurant ownership is intentionally resolved by the service layer. The
 * mapper only transfers the already-persisted restaurant business identifier
 * from {@link FoodEntity} to {@link FoodResponse}.
 * </p>
 *
 * <p>
 * System-managed entity fields such as the database identifier, food number,
 * restaurant ownership, lifecycle status and audit information are not
 * populated when mapping a {@link FoodRequest} to a new entity.
 * </p>
 *
 * @author Pankaj Kumar
 *         ============================================================================
 */
@Component
public class FoodMapper {

    /**
     * =========================================================================
     * Request → Entity
     * =========================================================================
     */

    /**
     * Creates a new {@link FoodEntity} from a {@link FoodRequest}.
     *
     * <p>
     * Only client-editable food fields are mapped. System-managed fields are
     * intentionally left for the service/domain layer.
     * </p>
     *
     * @param request food request
     * @return newly created food entity
     */
    public FoodEntity toEntity(final FoodRequest request) {

        Objects.requireNonNull(request, "Food request must not be null.");

        final FoodEntity foodEntity = (FoodEntity) FoodEntity.create();

        mapEditableFields(request, foodEntity);

        return foodEntity;
    }

    /**
     * Updates the editable fields of an existing {@link FoodEntity}.
     *
     * <p>
     * This method intentionally preserves system-managed fields such as:
     * </p>
     *
     * <ul>
     * <li>id</li>
     * <li>foodNumber</li>
     * <li>restaurantNumber</li>
     * <li>status</li>
     * <li>availability</li>
     * <li>audit information</li>
     * <li>image information</li>
     * </ul>
     *
     * @param request    food request
     * @param foodEntity existing food entity
     */
    public void updateEntity(final FoodRequest request, final FoodEntity foodEntity) {

        Objects.requireNonNull(request, "Food request must not be null.");
        Objects.requireNonNull(foodEntity, "Food entity must not be null.");

        mapEditableFields(request, foodEntity);
    }

    /**
     * Maps client-editable food fields to the entity.
     *
     * @param request    food request
     * @param foodEntity target food entity
     */
    private void mapEditableFields(final FoodRequest request, final FoodEntity foodEntity) {

        foodEntity.setFoodName(request.getFoodName());
        foodEntity.setDescription(request.getDescription());
        foodEntity.setPrice(request.getPrice());
        foodEntity.setFoodCategories(request.getFoodCategories());
        foodEntity.setDietCategory(request.getDietCategory());
        foodEntity.setCuisineType(request.getCuisineType());
        foodEntity.setCategoryGroups(request.getFoodCategories()
                .stream()
                .filter(Objects::nonNull)
                .map(fc -> Objects.requireNonNull(fc).getGroup())
                .collect(Collectors.toSet()));

    }

    /**
     * =========================================================================
     * Entity → Response
     * =========================================================================
     */

    /**
     * Converts a Food entity to a Food API response.
     *
     * <p>
     * The current status is used as the default previous-status representation for
     * normal food reads where there is no status transition context.
     * </p>
     *
     * @param foodEntity food entity
     * @return food response
     */
    public FoodResponse toResponse(final FoodEntity foodEntity) {

        Objects.requireNonNull(foodEntity, "Food entity must not be null.");

        return toResponse(foodEntity, foodEntity.getStatus());
    }

    /**
     * Converts a Food entity to a Food API response while preserving the previous
     * status supplied by a status-transition operation.
     *
     * <p>
     * The previous status is supplied by the service because it represents
     * operation context rather than persistent Food entity state.
     * </p>
     *
     * @param foodEntity     food entity
     * @param previousStatus previous food status
     * @return food response
     */
    public FoodResponse toResponse(final FoodEntity foodEntity, final FoodStatusConstant previousStatus) {

        Objects.requireNonNull(foodEntity, "Food entity must not be null.");

        return FoodResponse.builder()
                .id(foodEntity.getId())
                .foodNumber(foodEntity.getFoodNumber())
                .restaurantNumber(foodEntity.getRestaurantNumber())
                .restaurantBranchNumber(foodEntity.getRestaurantBranchNumber())
                .imageName(getImageName(foodEntity))
                .imageUrl(getImageUrl(foodEntity))
                .foodName(foodEntity.getFoodName())
                .description(foodEntity.getDescription())
                .price(foodEntity.getPrice())
                .foodCategories(DisplayOptionMapperUtil.fromSet(foodEntity.getFoodCategories()))
                .dietCategory(DisplayOptionMapperUtil.from(foodEntity.getDietCategory()))
                .cuisineType(DisplayOptionMapperUtil.from(foodEntity.getCuisineType()))
                .categoryGroups(DisplayOptionMapperUtil.fromSet(foodEntity.getCategoryGroups()))
                .foodStatus(DisplayOptionMapperUtil.from(foodEntity.getStatus()))
                .isAvailable(FoodStatusConstant.AVAILABLE.equals(foodEntity.getStatus()))
                .allowedStatuses(getAllowedStatuses(foodEntity))
                .previousStatus(DisplayOptionMapperUtil.from(getPreviousStatus(previousStatus, foodEntity)))
                .updatedAt(foodEntity.getStatusUpdatedAt() != null ? foodEntity.getStatusUpdatedAt() : null)
                .updatedBy(foodEntity.getStatusUpdatedBy()).createdBy(foodEntity.getCreatedBy())
                .createdAt(foodEntity.getCreatedAt() != null ? foodEntity.getCreatedAt() : null)
                .build();
    }

    /**
     * Converts a collection of Food entities to Food responses.
     *
     * @param foodEntities food entities
     * @return list of food responses
     */
    public List<FoodResponse> toResponseList(final Collection<FoodEntity> foodEntities) {

        Objects.requireNonNull(foodEntities, "Food entities must not be null.");

        return foodEntities.stream().filter(Objects::nonNull).map(this::toResponse).toList();
    }

    /**
     * =========================================================================
     * Entity → Status Response
     * =========================================================================
     */

    /**
     * Builds a status-operation response from the supplied Food entity.
     *
     * <p>
     * The previous status is supplied separately because it represents the state
     * before the current transition and therefore cannot reliably be reconstructed
     * from the updated entity alone.
     * </p>
     *
     * @param foodId         food identifier
     * @param previousStatus previous food status
     * @param foodEntity     updated food entity
     * @return food status response
     */
    public FoodStatusResponse toStatusResponse(final String foodId, final FoodStatusConstant previousStatus,
            final FoodEntity foodEntity) {

        Objects.requireNonNull(foodEntity, "Food entity must not be null.");

        return FoodStatusResponse.builder()
                .foodId(foodId)
                .previousStatus(previousStatus)
                .foodStatus(foodEntity.getStatus())
                .updatedAt(foodEntity.getStatusUpdatedAt())
                .updatedBy(foodEntity.getStatusUpdatedBy()).build();
    }

    /**
     * =========================================================================
     * Internal Mapping Helpers
     * =========================================================================
     */

    /**
     * Returns the persisted image name.
     *
     * @param foodEntity food entity
     * @return image name, or {@code null} when no image is associated
     */
    private String getImageName(final FoodEntity foodEntity) {

        if (foodEntity.getFoodImage() == null) {
            return null;
        }

        return foodEntity.getFoodImage().getImageName();
    }

    /**
     * Returns the persisted image URL.
     *
     * @param foodEntity food entity
     * @return image URL, or {@code null} when no image is associated
     */
    private String getImageUrl(final FoodEntity foodEntity) {

        if (foodEntity.getFoodImage() == null) {
            return null;
        }

        return foodEntity.getFoodImage().getImageURL();
    }

    /**
     * Returns the currently allowed status transitions.
     *
     * @param foodEntity food entity
     * @return allowed status options
     */
    private Set<DisplayOptionResponse> getAllowedStatuses(final FoodEntity foodEntity) {

        if (foodEntity.getStatus() == null) {
            return Collections.emptySet();
        }

        return foodEntity.getStatus().getAllowedTransitionOptions();
    }

    /**
     * Resolves the response representation of the previous status.
     *
     * @param previousStatus previous status supplied by the service
     * @param foodEntity     current food entity
     * @return previous status label
     */
    private FoodStatusConstant getPreviousStatus(final FoodStatusConstant previousStatus, final FoodEntity foodEntity) {

        FoodStatusConstant status = previousStatus != null ? previousStatus : foodEntity.getStatus();

        status = status != null ? status : null;

        return status;
    }
}