package com.klasix12.redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@AllArgsConstructor
public class ReactiveRedisService implements RedisService {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    @Override
    public Mono<Boolean> save(String key, String value, long duration) {
        return reactiveRedisTemplate.opsForValue().set(key, value, Duration.ofMillis(duration));
    }

    @Override
    public Mono<Long> delete(String key) {
        return reactiveRedisTemplate.delete(key);
    }

    @Override
    public Mono<Boolean> exists(String key) {
        return reactiveRedisTemplate.hasKey(key);
    }
}
