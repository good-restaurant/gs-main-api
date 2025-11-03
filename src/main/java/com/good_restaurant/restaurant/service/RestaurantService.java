package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.dto.RestaurantCoordinateResDto;
import com.good_restaurant.restaurant.dto.RestaurantCreateReqDto;
import com.good_restaurant.restaurant.dto.RestaurantDetailResDto;
import java.util.List;

public interface RestaurantService {

    List<RestaurantCoordinateResDto> getEntireRestaurantCoordinates(int limit);

    List<RestaurantDetailResDto> getNearbyRestaurants(String address, double radius, int limit);

    List<RestaurantDetailResDto> findRestaurantsByEmd(String emd, int limit);

    void createRestaurantData(RestaurantCreateReqDto dto);
}
