package com.good_restaurant.restaurant.mapper;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.dto.RestaurantCoordinateResDto;
import com.good_restaurant.restaurant.dto.RestaurantDetailResDto;
import com.good_restaurant.restaurant.dto.RestaurantIdDto;
import com.good_restaurant.restaurant.dto.RestaurantDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RestaurantMapper {
	
	Restaurant toEntity(RestaurantDto restaurantDto);
	
	RestaurantDto toDto(Restaurant restaurant);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	Restaurant partialUpdate(RestaurantDto restaurantDto, @MappingTarget Restaurant restaurant);
	
	Restaurant toEntity(RestaurantIdDto restaurantIdDto);
	
	RestaurantIdDto toDto1(Restaurant restaurant);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	Restaurant partialUpdate(RestaurantIdDto restaurantIdDto, @MappingTarget Restaurant restaurant);
	
	Restaurant toEntity(RestaurantDetailResDto restaurantDetailResDto);
	
	RestaurantDetailResDto toDto2(Restaurant restaurant);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	Restaurant partialUpdate(RestaurantDetailResDto restaurantDetailResDto, @MappingTarget Restaurant restaurant);
	
	Restaurant toEntity(RestaurantCoordinateResDto restaurantCoordinateResDto);
	
	RestaurantCoordinateResDto toDto3(Restaurant restaurant);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	Restaurant partialUpdate(RestaurantCoordinateResDto restaurantCoordinateResDto, @MappingTarget Restaurant restaurant);
	
	List<RestaurantCoordinateResDto> toDto3(List<Restaurant> restaurants);
}