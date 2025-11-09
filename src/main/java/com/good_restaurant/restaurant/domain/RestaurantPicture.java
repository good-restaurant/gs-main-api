package com.good_restaurant.restaurant.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restaurant_picture", schema = "good_restaurant")
public class RestaurantPicture {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@NotNull
	@Column(name = "picture_uuid", nullable = false)
	private UUID pictureUuid;
	
	// 실제 업로드된 원본 파일명 (예: food.jpg)
	@NotNull
	@Column(name = "original_filename")
	private String originalFilename;
	
	// S3 object key (예: 550e8400-e29b-41d4-a716-446655440000_food.jpg)
	@NotNull
	@Column(name = "s3_object_key")
	private String s3ObjectKey;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;
	
}