package com.good_restaurant.restaurant.dto;

import com.good_restaurant.restaurant.domain.Restaurant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link Restaurant}
 */
public record RestaurantIdDto(Long id, @NotNull @Size(max = 100) String restaurantName) implements Serializable {
}