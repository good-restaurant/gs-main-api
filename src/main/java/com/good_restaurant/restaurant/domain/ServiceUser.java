package com.good_restaurant.restaurant.domain;

import com.good_restaurant.restaurant.domain.Record.TimeRecord;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "restaurants", schema = "good_restaurant")
public class ServiceUser {
	@Id
	@Column(name = "userId", nullable = false)
	private UUID userId;
	
	private String email;
	
	private String name;
	
	@Embedded
	private TimeRecord timeRecord;
	
	private LocalDateTime lastLogin;
	
	private boolean status;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UserRole> roles = new HashSet<>();
	
	public void addRole(UserRole role) {
		roles.add(role);
		// 양방향 관계 설정
		role.user = this;
	}
	
	public void removeRole(UserRole role) {
		roles.remove(role);
		role.user = null;
	}
}
