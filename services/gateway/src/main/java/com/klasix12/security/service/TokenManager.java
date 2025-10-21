

package com.klasix12.security.service;

import com.klasix12.dto.Role;
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
        return Mono.fromCallable(() -> {
            if (jwtProvider.isTokenExpired(token)) {
                return false;
            }
            return jwtProvider.validateToken(token);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Long> extractId(String token) {
        return Mono.fromCallable(() -> jwtProvider.extractId(token))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<String> extractUsername(String token) {
        return Mono.fromCallable(() -> jwtProvider.extractUsername(token))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<List<Role>> extractRoles(String token) {
        return Mono.fromCallable(() -> jwtProvider.extractAuthorities(token))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
