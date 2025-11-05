package com.good_restaurant.restaurant.dto;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.good_restaurant.restaurant.domain.RestaurantMenu}
 */
public record RestaurantMenuDto(
		Long id,
		@Size(max = 255) String name,
		String description,
		Integer price,
		UUID pictureUuid
) implements Serializable {
}