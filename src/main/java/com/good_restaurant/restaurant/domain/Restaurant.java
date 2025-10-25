package com.good_restaurant.restaurant.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Getter
@Table(name = "restaurants", schema = "good_restaurant")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "restaurant_id", nullable = false)
	private Long id;

	@Size(max = 100)
	@NotNull
	@Column(name = "restaurant_name", nullable = false, length = 100)
	private String restaurantName;

	@Size(max = 255)
	@NotNull
	@Column(name = "address", nullable = false)
	private String address;

	@Size(max = 255)
	@Column(name = "category")
	private String category;

	@Size(max = 255)
	@Column(name = "menu")
	private String menu;

	@Size(max = 20)
	@Column(name = "phone_number", length = 20)
	private String phoneNumber;

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

	@Size(max = 255)
	@Column(name = "name")
	private String name;

	@Size(max = 255)
	@Column(name = "number")
	private String number;
}