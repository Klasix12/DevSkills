

package com.klasix12.security.service;

import com.klasix12.redis.RedisService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@AllArgsConstructor
public class TokenManager {

    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    public Mono<Boolean> isAccessTokenValid(String token) {
        return Mono.fromCallable(() -> {
            if (jwtProvider.isTokenExpired(token)) {
                return false;
            }
            return jwtProvider.validateToken(token);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> isRefreshTokenValid(String refreshToken) {
        return Mono.fromCallable(() -> {
            if (jwtProvider.isTokenExpired(refreshToken)) {
                return false;
            }
            return jwtProvider.extractId(refreshToken);
        }).flatMap(tokenId -> redisService.exists((String) tokenId));
    }

    public Mono<String> extractUsername(String token) {
        return Mono.fromCallable(() -> jwtProvider.extractUsername(token))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<List<String>> extractAuthorities(String token) {
        return Mono.fromCallable(() -> jwtProvider.extractAuthorities(token))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
