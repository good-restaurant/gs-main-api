package com.good_restaurant.restaurant.dto;

import com.good_restaurant.restaurant.domain.Category;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantCategoryReqDto {

	@NotNull
	private int limit;

	@NotNull
	private Category category;
}
