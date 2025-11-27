package com.good_restaurant.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
	private static final String REALM_ACCESS = "realm_access";
	private static final String ROLES = "roles";
	
	@Override
	public AbstractAuthenticationToken convert(Jwt jwt) {
		
		// Keycloak의 realm roles 추출
		Collection<String> roles = jwt.getClaimAsMap(REALM_ACCESS) != null
				                           ? (Collection<String>) ((Map<?, ?>) jwt.getClaim(REALM_ACCESS)).get(ROLES)
				                           : Collections.emptyList();
		
		// ROLE_ prefix 로 변환
		Collection<GrantedAuthority> authorities = roles.stream()
				                                           .map(role -> "ROLE_" + role)
				                                           .map(SimpleGrantedAuthority::new)
				                                           .collect(Collectors.toSet());
		
		return new JwtAuthenticationToken(jwt, authorities);
	}
}
