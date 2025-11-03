package com.good_restaurant.restaurant.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "restaurant_detail", schema = "good_restaurant")
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class RestaurantDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "detail_id", nullable = false)
	private Long id;

	@NotNull
	@OneToOne(optional = false, fetch = FetchType.LAZY )
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "restaurant_id", nullable = false, unique = true)    // TODO: check unique
	private Restaurant restaurant;

	@Size(max = 255)
	@Column(name = "menu")
	private String menu;

	@Size(max = 20)
	@Column(name = "phone_number", length = 20)
	private String phoneNumber;
}
