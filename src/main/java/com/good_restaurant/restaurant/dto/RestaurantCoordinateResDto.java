package com.good_restaurant.restaurant.dto;

import com.good_restaurant.restaurant.domain.Category;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
public class RestaurantCoordinateResDto {

	private final Long id;

	private final String restaurantName;
	private final String address;
	private final Category category;

	private final BigDecimal lon;
	private final BigDecimal lat;
}