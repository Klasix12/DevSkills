package com.klasix12.redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public void blacklistToken(String key, long duration) {
        redisTemplate.opsForValue().set(key, "1", Duration.ofMillis(duration));
    }

    @Override
    public boolean isBlacklisted(String key) {
        return redisTemplate.hasKey(key);
    }
}

