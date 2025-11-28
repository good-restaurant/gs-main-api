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
@Table(name = "ServiceUser", schema = "good_restaurant")
public class ServiceUser {
	@Id
	@Column(name = "userId", nullable = false)
	private UUID userId;
	
	private String email;
	
	private String name;
	
	@Builder.Default
	@Embedded
	private TimeRecord timeRecord = new TimeRecord();  // ★ 기본값
	
	private LocalDateTime lastLogin;
	
	private boolean status;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UserRole> roles = new HashSet<>();
	
	public void addRole(String roleName) {
		boolean exists = this.roles.stream()
				                 .anyMatch(r -> r.getRole().equals(roleName));
		
		if (exists) return;
		
		UserRole newRole = UserRole.builder()
				                   .role(roleName)
				                   .user(this)
				                   .build();
		
		this.roles.add(newRole);
	}
	
	public void removeRole(UserRole role) {
		roles.remove(role);
		role.user = null;
	}
	
	public void addRole(UserRole userRole) {
		roles.add(userRole);
		userRole.user = this;
	}
	
	public void removeRole(String roleName) {
		boolean exists = this.roles.stream()
				                 .anyMatch(r -> r.getRole().equals(roleName));
		
		if (exists) return;
		
		UserRole changedRole = UserRole.builder()
				                   .role(roleName)
				                   .user(this)
				                   .build();
		
		this.roles.remove(changedRole);
	}
}
