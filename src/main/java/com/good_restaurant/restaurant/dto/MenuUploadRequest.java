package com.good_restaurant.restaurant.dto;

public record MenuUploadRequest(
		Long menuId,
		String originalName
) {
}

