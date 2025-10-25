package com.klasix12.config.routes;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
@AllArgsConstructor
public class RoutesConfig {

    private final String authService = "auth-service";
    private final String questionService = "question-service";
    private final String authUri = "http://localhost:8082";
    private final String questionUri = "http://localhost:8084";

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(authService, r -> r.path("/auth/login")
                        .and().method(HttpMethod.POST)
                        .uri(authUri))

                .route(authService, r -> r.path("/auth/registration")
                        .and().method(HttpMethod.POST)
                        .uri(authUri))

                .route(authService, r -> r.path("/auth/refresh")
                        .and().method(HttpMethod.POST)
                        .uri(authUri))

                .route(authService, r -> r.path("/auth/login")
                        .and().method(HttpMethod.POST)
                        .uri(authUri))

                .route(authService, r -> r.path("/auth/logout")
                        .and().method(HttpMethod.POST)
                        .uri(authUri))

                .route(authService, r -> r.path("/auth/asdadadada")
                        .and().method(HttpMethod.GET)
                        .uri(authUri))

                .route(authService, r -> r.path("/test")
                        .and().method(HttpMethod.GET)
                        .uri(authUri))

                .route(questionService, r -> r.path("/question/{id}")
                        .and().method(HttpMethod.GET)
                        .uri(questionUri))

                .route(questionService, r -> r.path("/question")
                        .and().method(HttpMethod.POST)
                        .uri(questionUri))
                .build();
    }
}
