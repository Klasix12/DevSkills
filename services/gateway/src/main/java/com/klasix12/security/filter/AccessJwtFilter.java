package com.klasix12.security.filter;

import com.klasix12.config.PublicEndpointsConfig;
import com.klasix12.redis.ReactiveRedisService;
import com.klasix12.redis.RedisService;
import com.klasix12.security.service.TokenManager;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@AllArgsConstructor
public class AccessJwtFilter implements GlobalFilter {

    private final TokenManager tokenManager;
    private final PublicEndpointsConfig publicEndpointsConfig;
    private final String authHeaderKey = "Authorization";
    private final String tokenPrefix = "Bearer ";
    private final PathPatternParser parser = new PathPatternParser();
    private final RedisService redisService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeaderValue = exchange.getRequest().getHeaders().getFirst(this.authHeaderKey);

        if (isPublicEndpoint(exchange)) {
            return chain.filter(exchange);
        }

        if (authHeaderValue == null || !authHeaderValue.startsWith(tokenPrefix)) {
            return unauthorized(exchange, "Where is the Authorization header???");
        }

        String token = authHeaderValue.substring(tokenPrefix.length());

        return tokenManager.isAccessTokenValid(token)
                .flatMap(isValid -> {
                    if (Boolean.TRUE.equals(isValid)) {
                        return Mono.zip(
                                tokenManager.extractId(token),
                                tokenManager.extractUsername(token),
                                tokenManager.extractRoles(token)
                        ).flatMap(tuple -> {
                            String userId = tuple.getT1();
                            String username = tuple.getT2();
                            List<String> userRoles = tuple.getT3();

                            ServerWebExchange mutated = exchange.mutate()
                                    .request(req -> req.headers(headers -> {
                                        headers.add("X-User-Id", userId);
                                        headers.add("X-User-Name", username);
                                        headers.add("X-User-Roles", String.join(",", userRoles));
                                    }))
                                    .build();
                            return chain.filter(mutated);
                        });
                    } else {
                        return unauthorized(exchange, "Invalid or expired JWT token");
                    }
                })
                .onErrorResume(throwable -> unauthorized(exchange, "Token validation error: " + throwable.getMessage()));
    }

    private boolean isPublicEndpoint(ServerWebExchange exchange) {
        String method = exchange.getRequest().getMethod().name();
        PathContainer path = exchange.getRequest().getPath().pathWithinApplication();

        boolean anyMethodMatch = publicEndpointsConfig.getAnyMethod().stream()
                .map(parser::parse)
                .anyMatch(pattern -> pattern.matches(path));

        if (anyMethodMatch) {
            return true;
        }

        if (HttpMethod.GET.name().equalsIgnoreCase(method)) {
            return publicEndpointsConfig.getGetOnly().stream()
                    .map(parser::parse)
                    .anyMatch(pattern -> pattern.matches(path));
        }
        return true;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        byte[] bytes = ("{\"error\":\"" + message + "\"}").getBytes(StandardCharsets.UTF_8);
        response.getHeaders().add("Content-Type", "application/json");
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }
}
