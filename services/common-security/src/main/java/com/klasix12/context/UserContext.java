package com.klasix12.context;

import com.klasix12.dto.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserContext {
    private Long userId;
    private String username;
    private List<Role> roles;
}
