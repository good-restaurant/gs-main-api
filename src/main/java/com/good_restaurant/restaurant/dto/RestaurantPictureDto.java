package com.good_restaurant.restaurant.dto;

import com.good_restaurant.restaurant.domain.RestaurantPicture;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link RestaurantPicture}
 */
public record RestaurantPictureDto(
		Long id,
		@NotNull UUID pictureUuid,
		Long restaurantId
) implements Serializable {
}