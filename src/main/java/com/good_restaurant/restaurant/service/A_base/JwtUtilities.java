package com.good_restaurant.restaurant.service.A_base;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class JwtUtilities {
	public Set<String> extractRoles(Jwt jwt) {
		Set<String> roles = new HashSet<>();
		
		Map<String, Object> realm = jwt.getClaim("realm_access");
		if (realm != null) {
			roles.addAll((List<String>) realm.get("roles"));
		}
		
		Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
		if (resourceAccess != null) {
			for (Object clientObj : resourceAccess.values()) {
				Map<String, Object> client = (Map<String, Object>) clientObj;
				List<String> clientRoles = (List<String>) client.get("roles");
				roles.addAll(clientRoles);
			}
		}
		
		return roles;
	}
}
