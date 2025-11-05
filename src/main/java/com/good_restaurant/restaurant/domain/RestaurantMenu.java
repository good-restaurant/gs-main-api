package com.good_restaurant.restaurant.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "restaurant_menu", schema = "good_restaurant")
public class RestaurantMenu {
	@Id
	@Column(name = "id", nullable = false)
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
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;
	
}