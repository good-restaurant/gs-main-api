package com.good_restaurant.restaurant.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurants", schema = "good_restaurant")
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class Restaurant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "restaurant_id")
	private Long id;

	@NotNull
	@Size(max = 100)
	@Column(name = "restaurant_name", nullable = false, length = 100)
	private String restaurantName;

	@NotNull
	@Size(max = 255)
	@Column(name = "address", nullable = false)
	private String address;

	@Size(max = 255)
	@Enumerated(EnumType.STRING)
	@Column(name = "category")      // TODO: nullable = false?
	private Category category;

	@Column(name = "lon", precision = 10, scale = 6)
	private BigDecimal lon;

	@Column(name = "lat", precision = 10, scale = 6)
	private BigDecimal lat;

	@Size(max = 50)
	@Column(name = "ctp_kor_nm", length = 50)
	private String ctpKorNm;

	@Size(max = 50)
	@Column(name = "sig_kor_nm", length = 50)
	private String sigKorNm;

	@Size(max = 50)
	@Column(name = "emd_kor_nm", length = 50)
	private String emdKorNm;

	@OneToOne(mappedBy = "restaurant", fetch = FetchType.LAZY,
			  cascade = CascadeType.ALL, orphanRemoval = true)
	private RestaurantDetail detail;
}
