package com.good_restaurant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
	
//	// private final PasswordEncoder passwordEncoder;
//
//	// public SpringSecurityConfig() {
//		this.passwordEncoder = new BCryptPasswordEncoder();
//	}
//
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ServiceUserSyncFilter serviceUserSyncFilter) throws Exception {
	    http
	            .csrf(csrf -> csrf.disable()) // csrf 해제
			    .oauth2ResourceServer(
					    oauth2 -> oauth2.jwt(
							    jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(new JwtAuthConverter())
					    )
			    )
	            .authorizeHttpRequests(auth -> auth
//                               .requestMatchers("/v3/**").authenticated()  // 테스트용 v3 인증요구
                               .requestMatchers("/v1/restaurant-admin/**").hasRole("GoodService_ADMIN")   // admin role 필요
	                    .anyRequest().permitAll() // Allow all requests without authentication
	            )
			    .addFilterAfter(serviceUserSyncFilter, BearerTokenAuthenticationFilter.class)
	            .formLogin(login -> login.disable()) // Disable form login
	            .httpBasic(basic -> basic.disable()) // Disable HTTP Basic authentication
	            .logout(logout -> logout.disable()) // Disable logout
	            .cors(cors -> cors.configurationSource(request -> {
	                        CorsConfiguration configuration = new CorsConfiguration();
	                        configuration.setAllowedOrigins(Arrays.asList(
	                                "http://localhost",
		                            "https://goodservice.i4624.info",
			                        "https://dev-naver.i4624.info:3000",
			                        "https://dev-naver.i4624.info:5173",
			                        "https://localhost:18993",
			                        "https://localhost:3000",
			                        "https://localhost:5173",
			                        "http://gs-main-api.i4624.info"
	                        )); // Update with your client's origins
		                    configuration.addAllowedOriginPattern("https://*.i4624.info");
	                        configuration.setAllowedMethods(Arrays.asList(
			                        "GET",
			                        "POST",
			                        "PUT",
			                        "DELETE",
			                        "OPTIONS",
			                        "PATCH"
	                        ));
	                        configuration.setAllowedHeaders(Arrays.asList("*"));
	                        configuration.setAllowCredentials(true); // Allow credentials if needed
		                    
	                        return configuration;
	                    })
	            )
	    ;
	
	    return http.build();
    }

}