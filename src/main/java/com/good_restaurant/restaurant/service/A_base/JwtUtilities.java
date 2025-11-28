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
		
		// 1. Realm Roles - Keycloak role >> 이걸로 서비스 role 결정하면 별로 안좋음
		Map<String, Object> realm = jwt.getClaim("realm_access");
		if (realm != null) {
			roles.addAll((List<String>) realm.get("roles"));
		}
		
		
		// 2. Client roles - 서비스용 role
		Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
		if (resourceAccess != null) {
			resourceAccess.forEach((client, accessObj) -> {
				Map<String, Object> access = (Map<String, Object>) accessObj;
				List<String> clientRoles = (List<String>) access.get("roles");
				if (clientRoles != null) roles.addAll(clientRoles);
			});
		}
		
		return roles;
	}
	
	// 서비스용 역할 목록만 가져오는 더 나은 케이스
	public Set<String> extractServiceRoles(Jwt jwt) {
		Set<String> roles = new HashSet<>();
		
		Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
		if (resourceAccess == null) return roles;
		
		Map<String, Object> serviceClient = (Map<String, Object>) resourceAccess.get("good-restaurant-web");
		if (serviceClient == null) return roles;
		
		List<String> clientRoles = (List<String>) serviceClient.get("roles");
		if (clientRoles != null) roles.addAll(clientRoles);
		
		return roles;
	}
	
}
