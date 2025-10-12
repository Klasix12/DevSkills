package com.klasix12.redis;

public interface RedisService {
    void blacklistToken(String key, long duration);
    boolean isBlacklisted(String key);
}
