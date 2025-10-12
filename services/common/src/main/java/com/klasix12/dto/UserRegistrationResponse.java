package com.klasix12.dto;

import lombok.Data;

@Data
public class UserRegistrationResponse {
    private Long id;
    private String username;
    private String email;
    private String name;
}
