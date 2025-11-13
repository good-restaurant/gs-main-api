package com.good_restaurant.restaurant.dto;

import java.util.UUID;

public record UploadResult(
		UUID uuid,
		String objectKey, String uploadUrl) {
}
