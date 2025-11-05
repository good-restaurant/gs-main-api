package com.good_restaurant.restaurant.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "restaurant_comment", schema = "good_restaurant")
public class RestaurantComment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(name = "author_id")
	private Long authorId;
	
	@Size(max = 50)
	@Column(name = "display_name", length = 50)
	private String displayName;
	
	@NotNull
	@Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
	private String content;
	
	@ColumnDefault("0.0")
	@Column(name = "rating", precision = 2, scale = 1)
	private BigDecimal rating;
	
	@ColumnDefault("now()")
	@Column(name = "created_at")
	private Instant createdAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;
	
}