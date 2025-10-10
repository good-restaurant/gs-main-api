package com.good_restaurant.service;

import com.good_restaurant.dto.GoodRestaurantRes;
import com.good_restaurant.entity.GoodRestaurant;
import com.good_restaurant.repository.GoodRestaurantRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoodRestaurantService {
	private final GoodRestaurantRepository goodRestaurantRepository;

	public List<GoodRestaurantRes> getGoodRestaurants(){
		List<GoodRestaurant> findGoodRestaurants = goodRestaurantRepository.findAll();

		return findGoodRestaurants.stream()
				.map(findGoodRestaurant -> GoodRestaurantRes.builder()
						.id(findGoodRestaurant.getId())
						.address(findGoodRestaurant.getAddress())
						.menu(findGoodRestaurant.getMenu())
						.name(findGoodRestaurant.getName())
						.number(findGoodRestaurant.getNumber())
						.category(findGoodRestaurant.getCategory())
						.x(findGoodRestaurant.getX())
						.y(findGoodRestaurant.getY())
						.build()
				).collect(Collectors.toList());
	}
}
