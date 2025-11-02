package com.good_restaurant.restaurant.dto;

import com.good_restaurant.restaurant.domain.Category;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class RestaurantUpdateReqDto {

	// restaurant info
	@Size(max = 100)
	private String restaurantName;
	@Size(max = 255)
	private String address;
	@Size(max = 255)
	private Category category;  // TODO: NotNull 검토 필요

	private BigDecimal lon;
	private BigDecimal lat;

	@Size(max = 50)
	private String ctpKorNm;
	@Size(max = 50)
	private String sigKorNm;
	@Size(max = 50)
	private String emdKorNm;

	// restaurant_detail info
	@Size(max = 255)
	private String menu;
	@Size(max = 20)
	private String phoneNumber;
}
