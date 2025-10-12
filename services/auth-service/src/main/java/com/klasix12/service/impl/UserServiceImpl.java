package com.klasix12.service.impl;

import com.klasix12.dto.*;
import com.klasix12.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final WebClient webClient;

    public UserServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8083")
                .build();
    }

    @Override
    public UserDto login(AuthRequest authRequest) {
        return webClient.post()
                .uri("/users/login")
                .bodyValue(authRequest)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest) {
        return webClient
                .post()
                .uri("/users/register")
                .bodyValue(userRegistrationRequest)
                .retrieve()
                .bodyToMono(UserRegistrationResponse.class)
                .block();
    }
}
