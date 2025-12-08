package com.good_restaurant.restaurant.mapper;

import com.good_restaurant.restaurant.domain.RestaurantComment;
import com.good_restaurant.restaurant.domain.RestaurantCommentFullDto;
import com.good_restaurant.restaurant.dto.RestaurantCommentDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {RestaurantMapper.class})
public interface RestaurantCommentMapper {
	
	@Mapping(target = "timeRecord.createdAt", source = "createdAt")
	@Mapping(target = "timeRecord.updatedAt", source = "updatedAt")
	RestaurantComment toEntity(RestaurantCommentDto restaurantCommentDto);
	
	List<RestaurantComment> toEntity(List<RestaurantCommentDto> restaurantCommentDto);
	
	@Mapping(target = "createdAt", source = "timeRecord.createdAt")
	@Mapping(target = "updatedAt", source = "timeRecord.updatedAt")
	RestaurantCommentDto toDto(RestaurantComment restaurantComment);
	
	List<RestaurantCommentDto> toDto(List<RestaurantComment> restaurantComment);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "restaurant", ignore = true)
	RestaurantComment partialUpdate(RestaurantCommentDto restaurantCommentDto, @MappingTarget RestaurantComment restaurantComment);
	
	@Mapping(target = "restaurant", source = "restaurant")
	RestaurantComment toEntity(RestaurantCommentFullDto restaurantCommentFullDto);
	
	@Mapping(target = "restaurant", source = "restaurant")
	@Mapping(target = "createdAt", source = "timeRecord.createdAt")
	@Mapping(target = "updatedAt", source = "timeRecord.updatedAt")
	RestaurantCommentFullDto toFullDto(RestaurantComment restaurantComment);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "restaurant", ignore = true)
	RestaurantComment partialUpdate(RestaurantCommentFullDto restaurantCommentFullDto, @MappingTarget RestaurantComment restaurantComment);
}