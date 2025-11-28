package com.good_restaurant.restaurant.dto;

import com.good_restaurant.restaurant.domain.ServiceUser;
import com.good_restaurant.restaurant.domain.UserRole;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link ServiceUser}
 */
public record ServiceUserDto(
		UUID user_id,
		String email,
		String name,
		Set<UserRole> roles
) implements Serializable {
}