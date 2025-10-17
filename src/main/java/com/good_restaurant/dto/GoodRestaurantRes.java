package com.good_restaurant.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoodRestaurantRes {
	private Long id;
	private String name;
	private String address;
	private String number;
	private String menu;
	private String category;
	private double x;
	private double y;
}
