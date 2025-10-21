package com.klasix12.resolver;

import com.klasix12.context.UserContext;
import com.klasix12.dto.Constants;
import com.klasix12.dto.Role;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;

public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class) &&
                parameter.getParameterType().equals(UserContext.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        String userIdHeader = webRequest.getHeader(Constants.X_USER_ID);
        String usernameHeader = webRequest.getHeader(Constants.X_USERNAME);
        String rolesHeader = webRequest.getHeader(Constants.X_USER_ROLES);

        UserContext userContext = new UserContext();
        if (userIdHeader != null && !userIdHeader.isEmpty()) {
            userContext.setUserId(Long.parseLong(userIdHeader));
        }
        if (usernameHeader != null && !usernameHeader.isEmpty()) {
            userContext.setUsername(usernameHeader);
        }
        if (rolesHeader != null && !rolesHeader.isEmpty()) {
            userContext.setRoles(Arrays.stream(rolesHeader.split(","))
                    .map(Role::valueOf)
                    .toList());
        }
        return userContext;
    }
}
