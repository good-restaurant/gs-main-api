package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.domain.ServiceUser;
import com.good_restaurant.restaurant.domain.UserRole;

public interface UserRoleService {
	UserRole addRole(ServiceUser saved, String roleUser);
	
	UserRole removeRole(ServiceUser user, String role);
}
