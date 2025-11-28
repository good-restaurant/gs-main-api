package com.good_restaurant.config;

import com.good_restaurant.restaurant.domain.ServiceUser;
import com.good_restaurant.restaurant.service.ServiceUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServiceUserSyncFilter extends OncePerRequestFilter {
	
	private final ServiceUserService userService;
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest req,
			HttpServletResponse res,
			FilterChain chain
	) throws IOException, ServletException {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		// JWT 인증이 완료된 상태에서만 실행
		if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
			
			UUID sub = UUID.fromString(jwt.getSubject());
			String email = jwt.getClaim("email");
			String name = jwt.getClaim("name");
			
			// 여기서 최초 등록 or 업데이트 실행
			ServiceUser authorizedUserInfo = userService.loadOrCreateUser(jwt);
		}
		
		chain.doFilter(req, res);
	}
}
