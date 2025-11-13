package com.good_restaurant.restaurant.domain;

import com.good_restaurant.restaurant.dto.RestaurantIdDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link RestaurantComment}
 */
public record RestaurantCommentFullDto(
		Long id,
		Long authorId,
		@Size(max = 50) String displayName,
		@NotNull String content,
		BigDecimal rating,
		Instant createdAt,
		RestaurantIdDto restaurant
) implements Serializable {
}