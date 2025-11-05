package com.good_restaurant.restaurant.dto;

import com.good_restaurant.restaurant.domain.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link com.good_restaurant.restaurant.domain.Restaurant}
 */
public record RestaurantDetailResDto(
		Long id,
		@NotNull @Size(max = 100) String restaurantName,
		@Size(max = 255) String address,
		@Size(max = 255) String category,
		BigDecimal lon,
		BigDecimal lat,
		@Size(max = 50) String ctpKorNm,
		@Size(max = 50) String sigKorNm,
		@Size(max = 50) String emdKorNm,
		@Size(max = 20) String phoneNumber,
		LocalDate registeredDate,
		LocalDate canceledDate,
		BigDecimal rating
) implements Serializable {
}