package com.good_restaurant.restaurant.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.good_restaurant.restaurant.domain.Restaurant}
 */
public record RestaurantCoordinateResDto(
		Long id,
		@NotNull @Size(max = 100) String restaurantName,
		@Size(max = 255) String address,
		BigDecimal lon,
		BigDecimal lat
) implements Serializable {
}