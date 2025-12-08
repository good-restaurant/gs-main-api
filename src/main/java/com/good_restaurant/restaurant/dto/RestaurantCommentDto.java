package com.good_restaurant.restaurant.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.good_restaurant.restaurant.domain.RestaurantComment}
 */
public record RestaurantCommentDto(
		Long id,
		Long authorId,
		@Size(max = 50) String displayName,
		@NotNull String content,
		BigDecimal rating,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
) implements Serializable {
}