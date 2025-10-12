package com.klasix12.service;

import com.klasix12.dto.*;

public interface UserService {
    UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest);
    UserDto login(AuthRequest authRequest);
}
