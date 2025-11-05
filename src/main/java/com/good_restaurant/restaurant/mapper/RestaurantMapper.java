package com.good_restaurant.restaurant.mapper;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.dto.RestaurantIdDto;
import com.good_restaurant.restaurant.dto.RestaurantDto;
import org.mapstruct.*;

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
}