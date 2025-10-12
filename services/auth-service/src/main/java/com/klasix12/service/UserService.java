package com.klasix12.service;

import com.klasix12.dto.AuthRequest;
import com.klasix12.dto.UserDto;
import com.klasix12.dto.UserRegistrationRequest;
import com.klasix12.dto.UserRegistrationResponse;

public interface UserService {
    UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest);
    UserDto login(AuthRequest authRequest);
}
