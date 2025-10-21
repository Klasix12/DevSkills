package com.klasix12.service;

import com.klasix12.dto.AuthRequest;
import com.klasix12.dto.Role;
import com.klasix12.dto.UserDto;
import com.klasix12.dto.UserRegistrationRequest;
import com.klasix12.model.User;
import com.klasix12.repository.RoleRepository;
import com.klasix12.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserDto login(AuthRequest req) {
        User user = userRepository.findByUsername(req.getUsername());
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getFirstName())
                .roles(user.getRoles()
                        .stream()
                        .map(com.klasix12.model.Role::getName)
                        .toList())
                .build();
    }

    @Transactional
    public UserDto registerUser(UserRegistrationRequest request) {
        com.klasix12.model.Role role = roleRepository.findByName(Role.USER);
        User user = userRepository.save(User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .firstName(request.getName())
                .roles(Set.of(role))
                .isConfirmed(false)
                .createdAt(LocalDateTime.now())
                .build());
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getFirstName())
                .build();
    }
}
