package com.supung.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableEurekaClient
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator orphanageCustomRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p
						.path("/supung/orphanage/**")
						.filters(f -> f.rewritePath("/supung/orphanage/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://orphanage")).
						route(p -> p
								.path("/supung/requirement/**")
								.filters(f -> f.rewritePath("/supung/requirement/(?<segment>.*)","/${segment}")
										.addResponseHeader("X-Response-Time",LocalDateTime.now().toString()))
								.uri("lb://requirement")).
						route(p -> p
								.path("/supung/donation/**")
								.filters(f -> f.rewritePath("/supung/donation/(?<segment>.*)","/${segment}")
										.addResponseHeader("X-Response-Time",LocalDateTime.now().toString()))
								.uri("lb://donation")).build();
	}

}
