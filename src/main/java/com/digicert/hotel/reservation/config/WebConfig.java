package com.digicert.hotel.reservation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

	// This method is used to setup that any other domain can access this service.
	// This is for testing only
	@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry.addMapping("/**")
				.allowedOrigins(
						"http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
				.allowCredentials(true);

	}

}
