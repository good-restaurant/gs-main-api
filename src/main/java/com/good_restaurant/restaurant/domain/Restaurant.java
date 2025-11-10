package com.good_restaurant.restaurant.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restaurants", schema = "good_restaurant")
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
	@Column(name = "address")
	private String address;
	
	@Size(max = 255)
	@Column(name = "category")
	private String category;
	
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
	
	@Size(max = 20)
	@Column(name = "phone_number", length = 20)
	private String phoneNumber;
	
	@Column(name = "registered_date")
	private LocalDate registeredDate;
	
	@Column(name = "canceled_date")
	private LocalDate canceledDate;
	
	@ColumnDefault("0.0")
	@Column(name = "rating", precision = 2, scale = 1)
	private BigDecimal rating;
	
	@OneToMany(mappedBy = "restaurant")
	private Set<RestaurantComment> restaurantComments = new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "restaurant")
	private Set<RestaurantMenu> restaurantMenus = new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
	private Set<RestaurantPicture> restaurantPictures = new LinkedHashSet<>();
	
}