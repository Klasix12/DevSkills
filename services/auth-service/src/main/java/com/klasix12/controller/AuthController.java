package com.klasix12.controller;

import com.klasix12.dto.*;
import com.klasix12.service.AuthService;
import com.klasix12.service.UserService;
import com.klasix12.service.impl.JwtProvider;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody AuthRequest req) {
        return ResponseEntity
                .ok()
                .body(authService.login(req));
    }

    @PostMapping("/registration")
    public ResponseEntity<UserRegistrationResponse> register(@Valid @RequestBody UserRegistrationRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.registerUser(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestHeader(value = "Authorization", required = false) String accessToken,
                                                 @Valid @RequestBody RefreshTokenRequest req) {
        return ResponseEntity
                .ok(authService.refresh(accessToken, req));
    }

    @GetMapping("/asdadadada")
    public ResponseEntity<String> asdadadada(@RequestHeader("Authorization") String auth) {
        System.out.println(jwtProvider.extractUser(auth));
        return ResponseEntity.ok().body("asdadadada");
    }
}
