package com.good_restaurant.restaurant.mapper;

import com.good_restaurant.config.MapStructConfig;
import com.good_restaurant.restaurant.domain.Restaurant;
import com.good_restaurant.restaurant.dto.RestaurantCoordinateResDto;
import com.good_restaurant.restaurant.dto.RestaurantDetailResDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface RestaurantMapper {

    @Mapping(source = "detail.menu", target = "menu")
    @Mapping(source = "detail.phoneNumber", target = "phoneNumber")
    RestaurantDetailResDto toDetailDto(Restaurant restaurant);

    List<RestaurantDetailResDto> toDetailDtoList(List<Restaurant> restaurants);

    RestaurantCoordinateResDto toCoordinateDto(Restaurant restaurant);

    List<RestaurantCoordinateResDto> toCoordinateDtoList(List<Restaurant> restaurants);
}
