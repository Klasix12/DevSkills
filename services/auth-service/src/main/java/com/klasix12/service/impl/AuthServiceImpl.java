package com.klasix12.service.impl;

import com.klasix12.dto.*;
import com.klasix12.redis.RedisService;
import com.klasix12.service.AuthService;
import com.klasix12.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    @Override
    public TokenResponse login(AuthRequest req) {
        log.info("Login attempt. Username: {}", req.getUsername());
        UserDto userDto = userService.login(req);
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(jwtProvider.generateAccessToken(userDto));
        tokenResponse.setRefreshToken(jwtProvider.generateRefreshToken(userDto));
        return tokenResponse;
    }

    @Override
    public TokenResponse refresh(String accessTokenHeader, RefreshTokenRequest req) {
        if (req.getRefreshToken() == null) {
            // TODO: need custom exception
            throw new RuntimeException("Refresh token is null");
        }
        if (redisService.isBlacklisted(Constants.REFRESH_REDIS_BLACKLIST_PREFIX + req.getRefreshToken())) {
            throw new RuntimeException("Refresh token is blacklisted");
        }
        String refreshToken = req.getRefreshToken();
        long refreshTtlSeconds = extractTtlSeconds(refreshToken);
        TokenResponse tokenResponse = new TokenResponse();

        redisService.blacklistToken(Constants.REFRESH_REDIS_BLACKLIST_PREFIX + refreshToken, refreshTtlSeconds);

        if (accessTokenHeader != null) {
            String accessToken = extractAccessTokenFromHeader(accessTokenHeader);
            long accessTtlSeconds = extractTtlSeconds(accessToken);
            redisService.blacklistToken(Constants.ACCESS_TOKEN_BLACKLIST_PREFIX + accessToken, accessTtlSeconds);
        }
        String newAccessToken = jwtProvider.generateAccessToken(jwtProvider.extractUser(refreshToken));
        tokenResponse.setAccessToken(newAccessToken);
        String newRefreshToken = jwtProvider.generateRefreshToken(jwtProvider.extractUser(refreshToken));
        tokenResponse.setRefreshToken(newRefreshToken);
        return tokenResponse;
    }

    private long extractTtlSeconds(String token) {
        long ttlSeconds = jwtProvider.extractExpirationEpochSeconds(token) - Instant.now().getEpochSecond();
        return ttlSeconds >= 0 ? ttlSeconds : 5;
    }

    private String extractAccessTokenFromHeader(String token) {
        String headerPrefix = "Bearer ";
        System.out.println(token.substring(headerPrefix.length()));
        return token.substring(headerPrefix.length());
    }
}
