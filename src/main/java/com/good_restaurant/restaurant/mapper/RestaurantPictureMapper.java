	package com.good_restaurant.restaurant.mapper;
	
	import com.good_restaurant.restaurant.domain.RestaurantPicture;
	import com.good_restaurant.restaurant.dto.RestaurantPictureDto;
	import org.mapstruct.*;
	
	import java.util.List;
	
	@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
	public interface RestaurantPictureMapper {
		@Mapping(target = "restaurant.id", source = "restaurantId")
		RestaurantPicture toEntity(RestaurantPictureDto dto);
		
		@Mapping(target = "restaurantId", source = "restaurant.id")
		RestaurantPictureDto toDto(RestaurantPicture entity);
		
		List<RestaurantPicture> toEntity(List<RestaurantPictureDto> dto);
		
		List<RestaurantPictureDto> toDto(List<RestaurantPicture> entity);
	}