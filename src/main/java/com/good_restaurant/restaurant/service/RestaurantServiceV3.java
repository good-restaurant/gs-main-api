package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.service.A_Exception.MergePropertyException;
import com.good_restaurant.restaurant.service.A_base.BaseCRUD;

import java.util.List;


/**
 * 새 DTO와 스키마를 위해 재작성한 RestaurantService
 */
public interface RestaurantServiceV3 extends BaseCRUD<Restaurant, Long> {
	
	List<Restaurant> randomLimit(int limit);
	
	List<Restaurant> getnearRestaurants(String address, double radius, int limit);
	
	List<Restaurant> getEmdRestaurants(String emd);
	
	List<Restaurant> limitFilter(Integer limit, List<Restaurant> restaurants);
	
	
	List<Restaurant> getLocatedRestaurants(Double lat, Double lon, Double radius, Integer limit);
}
