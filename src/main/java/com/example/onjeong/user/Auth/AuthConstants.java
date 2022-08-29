package com.example.onjeong.user.Auth;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthConstants {
    public static final String AUTH_HEADER_REFRESH = "Authorization-Refresh";
    public static final String AUTH_HEADER_ACCESS = "Authorization-Access";
    public static final String TOKEN_TYPE = "JWT";
}
