package com.good_restaurant.restaurant.dto;

import com.good_restaurant.restaurant.domain.Restaurant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

/**
 * 연관 항목까지 조회하거나 하는 항목
 *
 * DTO for {@link Restaurant}
 */
public record RestaurantFullDto(
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
		BigDecimal rating,
		Set<RestaurantCommentDto> restaurantComments,
		Set<RestaurantMenuDto> restaurantMenus,
		Set<RestaurantPictureDto> restaurantPictures
) implements Serializable {
}