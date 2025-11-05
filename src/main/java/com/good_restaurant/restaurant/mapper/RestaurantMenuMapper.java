package com.good_restaurant.restaurant.mapper;

import com.good_restaurant.restaurant.domain.RestaurantMenu;
import com.good_restaurant.restaurant.dto.RestaurantMenuDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RestaurantMenuMapper {
	RestaurantMenu toEntity(RestaurantMenuDto restaurantMenuDto);
	
	RestaurantMenuDto toDto(RestaurantMenu restaurantMenu);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	RestaurantMenu partialUpdate(RestaurantMenuDto restaurantMenuDto, @MappingTarget RestaurantMenu restaurantMenu);
}