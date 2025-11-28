package com.good_restaurant.restaurant.service;

import com.good_restaurant.restaurant.domain.ServiceUser;
import com.good_restaurant.restaurant.service.A_Exception.MergePropertyException;
import org.springframework.security.oauth2.jwt.Jwt;

public interface ServiceUserService {
	ServiceUser loadOrCreateUser(Jwt jwt);
}
