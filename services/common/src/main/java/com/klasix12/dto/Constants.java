package com.klasix12.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String ALGORITHM = "RSA";
    public static final String REFRESH_REDIS_BLACKLIST_PREFIX = "BL:refresh:";
    public static final String ACCESS_TOKEN_BLACKLIST_PREFIX = "BL:access:";
    public static final String X_USER_ID = "X-User-Id";
    public static final String X_USERNAME = "X-User-Name";
    public static final String X_USER_ROLES = "X-User-Roles";
}
