package com.good_restaurant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
	
	private final PasswordEncoder passwordEncoder;
	
	public SpringSecurityConfig() {
		this.passwordEncoder = new BCryptPasswordEncoder();
	}
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	            .csrf(csrf -> csrf.disable()) // csrf 해제
	            .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("admin")   // admin role 필요
	                    .anyRequest().permitAll() // Allow all requests without authentication
	            )
			    .oauth2ResourceServer(
						oauth2 -> oauth2.jwt(
								jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(new JwtAuthConverter())
						)
			    )
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
	                        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	                        configuration.setAllowedHeaders(Arrays.asList("*"));
	                        configuration.setAllowCredentials(true); // Allow credentials if needed
		                    
	                        return configuration;
	                    })
	            )
	    ;
	
	    return http.build();
    }
}