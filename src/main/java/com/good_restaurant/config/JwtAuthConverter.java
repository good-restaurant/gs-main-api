package com.good_restaurant.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.*;
import java.util.stream.Collectors;

public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
	private static final String REALM_ACCESS = "realm_access";
	private static final String ROLES = "roles";
	
	@Override
	public AbstractAuthenticationToken convert(Jwt jwt) {
		
		Set<String> roles = new HashSet<>();
		
		// 1. Realm roles
		Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
		if (realmAccess != null) {
			List<String> realmRoles = (List<String>) realmAccess.get("roles");
			roles.addAll(realmRoles);
		}
		
		// 2. Client roles -> 서비스용 Role 확인
		Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
		if (resourceAccess != null) {
			resourceAccess.forEach((client, accessObj) -> {
				Map<String, Object> access = (Map<String, Object>) accessObj;
				List<String> clientRoles = (List<String>) access.get("roles");
				if (clientRoles != null) roles.addAll(clientRoles);
			});
		}
		
		// ROLE_ prefix 붙여서 Spring Security 권한 객체로 변환
		Collection<GrantedAuthority> authorities = roles.stream()
				                                           .map(r -> "ROLE_" + r)
				                                           .map(SimpleGrantedAuthority::new)
				                                           .collect(Collectors.toSet());
		
		return new JwtAuthenticationToken(jwt, authorities);
	}
}
