package com.good_restaurant.controller;

import com.good_restaurant.dto.GoodRestaurantRes;
import com.good_restaurant.service.GoodRestaurantService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GoodRestaurantController {
	private final GoodRestaurantService goodRestaurantService;

	@GetMapping("/good/restaurant")
	public ResponseEntity<List<GoodRestaurantRes>> getGoodRestaurants() {
		return ResponseEntity.ok(goodRestaurantService.getGoodRestaurants());
	}
}
