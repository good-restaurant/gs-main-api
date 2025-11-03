package com.good_restaurant.restaurant.repository.querydsl;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.dto.RestaurantDetailResDto;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface RestaurantQueryDslRepository {
	List<Restaurant> findRandomRestaurants(Pageable pageable);
	
	List<Restaurant> findNearbyRestaurantsWithDetail(
			BigDecimal minLat, BigDecimal maxLat,
			BigDecimal minLon, BigDecimal maxLon,
			BigDecimal centerLat, BigDecimal centerLon,
			Pageable pageable
	);
	
	List<Restaurant> findRestaurantsByEmdRandom(
			String emd,
			Pageable pageable
	);
}
