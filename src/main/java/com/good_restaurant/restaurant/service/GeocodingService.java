package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.dto.GeocodeResultDto;

public interface GeocodingService {

    GeocodeResultDto geocode(String address);
}
