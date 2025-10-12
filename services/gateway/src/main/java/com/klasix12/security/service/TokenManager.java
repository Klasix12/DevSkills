

package com.klasix12.security.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@AllArgsConstructor
public class TokenManager {

    private final JwtProvider jwtProvider;

    public Mono<Boolean> isAccessTokenValid(String token) {
        System.out.println("isAccessTokenValid");
        return Mono.fromCallable(() -> {
            if (jwtProvider.isTokenExpired(token)) {
                return false;
            }
            return jwtProvider.validateToken(token);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> isRefreshTokenValid(String refreshToken) {
        return Mono.fromCallable(() -> {
            if (jwtProvider.isTokenExpired(refreshToken) || !jwtProvider.validateToken(refreshToken)) {
                return false;
            }
            return jwtProvider.extractId(refreshToken);
        }).flatMap(tokenId -> {
            if (tokenId == null) {
                return Mono.just(false);
            }
            return Mono.just(true);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<String> extractId(String token) {
        return Mono.fromCallable(() -> jwtProvider.extractId(token))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<String> extractUsername(String token) {
        return Mono.fromCallable(() -> jwtProvider.extractUsername(token))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<List<String>> extractRoles(String token) {
        return Mono.fromCallable(() -> jwtProvider.extractAuthorities(token))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
