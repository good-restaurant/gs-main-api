package com.good_restaurant.restaurant.mapper;

import com.good_restaurant.restaurant.domain.RestaurantComment;
import com.good_restaurant.restaurant.dto.RestaurantCommentDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RestaurantCommentMapper {
	RestaurantComment toEntity(RestaurantCommentDto restaurantCommentDto);
	
	RestaurantCommentDto toDto(RestaurantComment restaurantComment);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	RestaurantComment partialUpdate(RestaurantCommentDto restaurantCommentDto, @MappingTarget RestaurantComment restaurantComment);
}