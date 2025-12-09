package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.domain.RestaurantMenu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface RestaurantMenuService {
	
	RestaurantMenu uploadRestaurantPicture(RestaurantMenu restaurantMenu, MultipartFile file);
	
	Page<RestaurantMenu> getMenusAsPage(Pageable pageable);
	
	RestaurantMenu findMenuById(Long id);
	
	RestaurantMenu saveMenu(RestaurantMenu menu);
	
	RestaurantMenu updateMenu(Long id, RestaurantMenu newMenu);
	
	RestaurantMenu delete(Long id);
	
}
