package com.good_restaurant.restaurant.mapper;

import com.good_restaurant.restaurant.domain.RestaurantPicture;
import com.good_restaurant.restaurant.dto.RestaurantPictureDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {RestaurantMapper.class})
public interface RestaurantPictureMapper {
	RestaurantPicture toEntity(RestaurantPictureDto restaurantPictureDto);
	
	RestaurantPictureDto toDto(RestaurantPicture restaurantPicture);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	RestaurantPicture partialUpdate(RestaurantPictureDto restaurantPictureDto, @MappingTarget RestaurantPicture restaurantPicture);
}