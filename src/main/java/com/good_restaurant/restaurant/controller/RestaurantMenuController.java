package com.good_restaurant.restaurant.controller;


import com.good_restaurant.restaurant.service.RestaurantMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/restaurant-menu")
@RequiredArgsConstructor
public class RestaurantMenuController {
	
	private final RestaurantMenuService service;
	
}
