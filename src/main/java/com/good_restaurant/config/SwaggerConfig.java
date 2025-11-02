package com.good_restaurant.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.components(new Components())
				.info(apiInfo())
				.servers(List.of(new Server().url("/")));
	}

	private Info apiInfo() {
		return new Info()
				.title("Good Restaurant") // API의 제목
				.description("This is Good Restaurant Swagger UI") // API에 대한 설명
				.version("1.0.0"); // API의 버전
	}
}
