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
            // TODO: need custom exception
            throw new RuntimeException("Refresh token is blacklisted");
        }
        String refreshToken = req.getRefreshToken();
        blacklistRefreshToken(refreshToken);
        if (accessTokenHeader != null) {
            blacklistAccessToken(accessTokenHeader);
        }
        return TokenResponse.builder()
                .accessToken(jwtProvider.generateAccessToken(jwtProvider.extractUser(refreshToken)))
                .refreshToken(jwtProvider.generateRefreshToken(jwtProvider.extractUser(refreshToken)))
                .build();
    }

    @Override
    public void logout(String accessTokenHeader, RefreshTokenRequest req) {
        blacklistAccessToken(accessTokenHeader);
        blacklistRefreshToken(req.getRefreshToken());
    }

    private void blacklistAccessToken(String accessTokenHeader) {
        String accessToken = extractAccessTokenFromHeader(accessTokenHeader);
        long accessTtlSeconds = extractTtlSeconds(accessToken);
        redisService.blacklistToken(Constants.ACCESS_TOKEN_BLACKLIST_PREFIX + accessToken, accessTtlSeconds);
    }

    private void blacklistRefreshToken(String refreshToken) {
        long refreshTtlSeconds = extractTtlSeconds(refreshToken);
        redisService.blacklistToken(Constants.REFRESH_REDIS_BLACKLIST_PREFIX + refreshToken, refreshTtlSeconds);
    }

    private long extractTtlSeconds(String token) {
        long ttlSeconds = jwtProvider.extractExpirationEpochSeconds(token) - Instant.now().getEpochSecond();
        return ttlSeconds >= 0 ? ttlSeconds : 5;
    }

    private String extractAccessTokenFromHeader(String token) {
        String headerPrefix = "Bearer ";
        return token.substring(headerPrefix.length());
    }
}
