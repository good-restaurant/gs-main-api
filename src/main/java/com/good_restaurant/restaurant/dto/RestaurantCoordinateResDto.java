package com.good_restaurant.restaurant.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantCoordinateResDto {

	Long id;

	String restaurantName;
	String address;
	String category;

	BigDecimal lon;
	BigDecimal lat;
}