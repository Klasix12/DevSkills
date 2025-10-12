package com.klasix12.redis;

import reactor.core.publisher.Mono;

public interface RedisService {
    void blacklistToken(String key, long duration);
    boolean isBlacklisted(String key);
}
