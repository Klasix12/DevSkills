package com.klasix12.config.routes;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
@AllArgsConstructor
public class AuthRoutesConfig {

    private final String service = "auth-service";
    private final String uri = "http://localhost:8082";

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(service, r -> r.path("/auth/login")
                        .and().method(HttpMethod.POST)
                        .uri(uri))

                .route(service, r -> r.path("/auth/registration")
                        .and().method(HttpMethod.POST)
                        .uri(uri))

                .route(service, r -> r.path("/auth/refresh")
                        .and().method(HttpMethod.POST)
                        .uri(uri))

                .route(service, r -> r.path("/auth/login")
                        .and().method(HttpMethod.POST)
                        .uri(uri))

                .route(service, r -> r.path("/auth/asdadadada")
                        .and().method(HttpMethod.GET)
                        .uri(uri))
                .build();
    }
}
