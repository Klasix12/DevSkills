package com.klasix12.redis;

import reactor.core.publisher.Mono;

public interface RedisService {
    Mono<Boolean> save(String key, String value, long duration);

    Mono<Long> delete(String key);

    Mono<Boolean> exists(String key);
}
