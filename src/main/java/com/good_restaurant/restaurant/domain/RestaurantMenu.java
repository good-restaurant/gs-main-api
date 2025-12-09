package com.good_restaurant.restaurant.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restaurant_menu", schema = "good_restaurant")
public class RestaurantMenu {
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Size(max = 255)
	@Column(name = "name")
	private String name;
	
	@Column(name = "description", length = Integer.MAX_VALUE)
	private String description;
	
	@Column(name = "price")
	private Integer price;
	
	@Column(name = "picture_uuid")
	private UUID pictureUuid;
	
	@Size(max = 512)
	@Column(name = "s3_object_key", length = 512)
	private String s3ObjectKey;
	
	@Size(max = 255)
	@Column(name = "original_filename")
	private String originalFilename;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;
}