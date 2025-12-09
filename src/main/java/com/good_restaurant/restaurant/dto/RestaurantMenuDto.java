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
//		레스토랑 id 는 순환 문제 때문에 넣지 않습니다.
) implements Serializable {
}