package com.good_restaurant.restaurant.domain.Record;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Embeddable
public record TimeRecord(
		
		@CreatedDate
		@Column(name = "created_at", updatable = false)
		LocalDateTime createdAt,
		
		@LastModifiedDate
		@Column(name = "updated_at")
		LocalDateTime updatedAt
		
) {
}
