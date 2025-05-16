package com.budget.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.budget")
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("auth_route", r -> r.path("/auth/**")
						.uri("lb://AUTH-SERVICE")
				)
				.route("ledger_route", r -> r.path("/ledger/**")
						.uri("lb://LEDGER-SERVICE")
				)
				.route("refreshtoken_route", r -> r.path("/refreshtoken/**")
						.uri("lb://AUTH-SERVICE"))
				.build();

	}
}
