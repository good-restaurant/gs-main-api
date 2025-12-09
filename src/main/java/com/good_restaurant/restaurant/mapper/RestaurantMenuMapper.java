package com.good_restaurant.restaurant.mapper;

import com.good_restaurant.restaurant.domain.RestaurantMenu;
import com.good_restaurant.restaurant.dto.RestaurantMenuDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RestaurantMenuMapper {
	@Mapping(source = "restaurantId", target = "restaurant.id")
	RestaurantMenu toEntity(RestaurantMenuDto restaurantMenuDto);
	
	@Mapping(source = "restaurant.id", target = "restaurantId")
	RestaurantMenuDto toDto(RestaurantMenu restaurantMenu);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	RestaurantMenu partialUpdate(RestaurantMenuDto restaurantMenuDto, @MappingTarget RestaurantMenu restaurantMenu);
}