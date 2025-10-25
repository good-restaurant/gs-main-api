package com.good_restaurant.restaurant.dto;

import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class RestaurantUpdateReqDto {

	@Size(max = 100)
	String restaurantName;
	@Size(max = 255)
	String address;
	@Size(max = 255)
	String category;
	@Size(max = 255)
	String menu;
	@Size(max = 20)
	String phoneNumber;

	BigDecimal lon;
	BigDecimal lat;

	@Size(max = 50)
	String ctpKorNm;
	@Size(max = 50)
	String sigKorNm;
	@Size(max = 50)
	String emdKorNm;
}
