package com.good_restaurant.restaurant.domain;

import com.good_restaurant.restaurant.domain.Record.TimeRecord;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
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
	
	@Builder.Default
	@Embedded
	private TimeRecord timeRecord = new TimeRecord();  // 기본값
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;
	
}