package com.klasix12.service;

import com.klasix12.dto.AuthRequest;
import com.klasix12.dto.RefreshTokenRequest;
import com.klasix12.dto.TokenResponse;

public interface AuthService {
    TokenResponse login(AuthRequest req);

    TokenResponse refresh(String accessTokenHeader, RefreshTokenRequest req);

    void logout(String accessTokenHeader, RefreshTokenRequest req);
}
