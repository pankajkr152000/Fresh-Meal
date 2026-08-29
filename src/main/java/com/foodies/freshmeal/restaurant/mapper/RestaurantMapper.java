package com.foodies.freshmeal.restaurant.mapper;

import org.springframework.stereotype.Component;

import com.foodies.freshmeal.common.enums.EntityName;
import com.foodies.freshmeal.common.factory.EntityFactory;
import com.foodies.freshmeal.common.util.DisplayOptionMapperUtil;
import com.foodies.freshmeal.common.valueObject.EmailAddress;
import com.foodies.freshmeal.common.valueObject.PhoneNumber;
import com.foodies.freshmeal.restaurant.dto.CreateRestaurantInputDTO;
import com.foodies.freshmeal.restaurant.dto.RestaurantDetailsResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantListResponse;
import com.foodies.freshmeal.restaurant.entity.RestaurantEntity;

/**
 * ============================================================================
 * Mapper : Restaurant
 * ============================================================================
 *
 * Provides pure object transformations between Restaurant entities and
 * Restaurant API representations.
 *
 * <p>
 * The mapper does not perform repository access, business validation, sequence
 * generation, image resolution, or lifecycle operations.
 * </p>
 *
 * ============================================================================
 */
@Component
public class RestaurantMapper {

	/**
	 * Maps RestaurantCreateRequest to RestaurantEntity.
	 *
	 * <p>
	 * Server-managed fields such as restaurantNumber, status, availability, and
	 * audit information are intentionally not populated here.
	 * </p>
	 *
	 * @param request restaurant creation request
	 *
	 * @return restaurant entity
	 */
	public RestaurantEntity toEntity(CreateRestaurantInputDTO request) {

		RestaurantEntity entity = (RestaurantEntity) EntityFactory.createEntity(EntityName.RESTAURANT_ENTITY);
		entity.setRestaurantName(request.getRestaurantRequest().getRestaurantName());
		entity.setDescription(request.getRestaurantRequest().getDescription());

		entity.setPhoneNumber(PhoneNumber.builder().value(request.getRestaurantRequest().getPhoneNumber()).build());

		entity.setEmailAddress(EmailAddress.builder().value(request.getRestaurantRequest().getEmailAddress()).build());

		entity.setWebsite(request.getRestaurantRequest().getWebsite());
		entity.setCuisineTypes(request.getRestaurantRequest().getCuisineTypes());
    	
		entity.setRestaurantCoverImage(request.getCoverImage());
		entity.setRestaurantLogoImage(request.getLogoImage());;

		return entity;
	}

	/**
	 * Maps RestaurantEntity to the restaurant list projection.
	 *
	 * @param entity restaurant entity
	 *
	 * @return restaurant list response
	 */
	public RestaurantListResponse toListResponse(RestaurantEntity entity) {

		return RestaurantListResponse.builder().id(entity.getId()).restaurantNumber(entity.getRestaurantNumber())
				.restaurantName(entity.getRestaurantName())
				.description(entity.getDescription())
				.cuisineTypes(DisplayOptionMapperUtil.fromSet(entity.getCuisineTypes()))
				.status(DisplayOptionMapperUtil.from(entity.getStatus()))
				.isAvailable(entity.isAvailable()).build();
	}

	/**
	 * Maps RestaurantEntity to the detailed restaurant projection.
	 *
	 * @param entity restaurant entity
	 *
	 * @return restaurant details response
	 */
	public RestaurantDetailsResponse toDetailsResponse(RestaurantEntity entity) {

		return RestaurantDetailsResponse.builder().id(entity.getId()).restaurantNumber(entity.getRestaurantNumber())
				.restaurantName(entity.getRestaurantName())
				.description(entity.getDescription())

				.phoneNumber(entity.getPhoneNumber() != null ? entity.getPhoneNumber().getValue() : null)

				.emailAddress(entity.getEmailAddress() != null ? entity.getEmailAddress().getValue() : null)

				.website(entity.getWebsite())

				.cuisineTypes(DisplayOptionMapperUtil.fromSet(entity.getCuisineTypes()))

				.status(DisplayOptionMapperUtil.from(entity.getStatus()))

				.statusUpdatedAt(entity.getUpdatedAt())
				.statusUpdatedBy(entity.getUpdatedBy())
				.isAvailable(entity.isAvailable())

				.createdAt(entity.getCreatedAt())
				.createdBy(entity.getCreatedBy())
				.updatedAt(entity.getUpdatedAt())
				.updatedBy(entity.getUpdatedBy())
				.build();
	}
}