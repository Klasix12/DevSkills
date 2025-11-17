package com.klasix12.mapper;

import com.klasix12.context.UserContext;
import com.klasix12.dto.UserDto;
import com.klasix12.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getUsername());
    }

    public static User toEntity(UserContext userContext) {
        return new User(userContext.getUserId(), userContext.getUsername(), false);
    }
}
