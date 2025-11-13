package com.good_restaurant.restaurant.mapper;

import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.domain.RestaurantComment;
import com.good_restaurant.restaurant.domain.RestaurantMenu;
import com.good_restaurant.restaurant.domain.RestaurantPicture;
import com.good_restaurant.restaurant.dto.RestaurantFullDto;
import com.good_restaurant.restaurant.dto.RestaurantCoordinateResDto;
import com.good_restaurant.restaurant.dto.RestaurantDetailResDto;
import com.good_restaurant.restaurant.dto.RestaurantIdDto;
import com.good_restaurant.restaurant.dto.RestaurantDto;
import org.mapstruct.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {
//		RestaurantCommentMapper.class,
		RestaurantMenuMapper.class,
		RestaurantPictureMapper.class
})
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
	
	List<RestaurantCoordinateResDto> toDto(List<Restaurant> limitedRestaurants);
	
	List<RestaurantDetailResDto> toDto2(List<Restaurant> nearbyRestaurantsWithDetail);
	
	@Mapping(source = "restaurantPictures", target = "restaurantPictures")
	Restaurant toEntity(RestaurantFullDto restaurantFullDto);
	
	@Mapping(source = "restaurantPictures", target = "restaurantPictures")
	RestaurantFullDto toDto4(Restaurant restaurant);
	
	List<RestaurantFullDto> toDto4(List<Restaurant> restaurant);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	Restaurant partialUpdate(RestaurantFullDto restaurantFullDto, @MappingTarget Restaurant restaurant);
	
	@AfterMapping
	default void linkRestaurantMenus(@MappingTarget Restaurant restaurant) {
		Set<RestaurantMenu> cur = restaurant.getRestaurantMenus();
		if (cur == null || cur.isEmpty()) return;
		Restaurant parent = restaurant;
		Set<RestaurantMenu> rebuilt = cur.stream()
				                              .map(m -> m.toBuilder().restaurant(parent).build())
				                              .collect(Collectors.toCollection(LinkedHashSet::new));
		cur.clear();
		cur.addAll(rebuilt);
	}
	
	@AfterMapping
	default void linkRestaurantPictures(@MappingTarget Restaurant restaurant) {
		Set<RestaurantPicture> cur = restaurant.getRestaurantPictures();
		if (cur == null || cur.isEmpty()) return;
		
		Set<RestaurantPicture> rebuilt = cur.stream()
				                                 .map(pic -> RestaurantPicture.withParent(pic, restaurant))
				                                 .collect(Collectors.toCollection(LinkedHashSet::new));
		
		restaurant.getRestaurantPictures().clear();
		restaurant.getRestaurantPictures().addAll(rebuilt);
	}
	
//	@AfterMapping
//	default void linkRestaurantComments(@MappingTarget Restaurant restaurant) {
//		Set<RestaurantComment> cur = restaurant.getRestaurantComments();
//		if (cur == null || cur.isEmpty()) return;
//		Restaurant parent = restaurant;
//		Set<RestaurantComment> rebuilt = cur.stream()
//				                                 .map(m -> m.toBuilder().restaurant(parent).build())
//				                                 .collect(Collectors.toCollection(LinkedHashSet::new));
//		cur.clear();
//		cur.addAll(rebuilt);
//	}
}