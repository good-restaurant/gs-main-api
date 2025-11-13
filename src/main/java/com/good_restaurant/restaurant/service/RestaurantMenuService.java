package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.domain.RestaurantMenu;
import org.springframework.web.multipart.MultipartFile;

public interface RestaurantMenuService {
	
	RestaurantMenu uploadRestaurantPicture(RestaurantMenu restaurantMenu, MultipartFile file);
}
