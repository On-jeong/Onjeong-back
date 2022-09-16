package com.example.onjeong.user.Auth;

import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.user.exception.UserUnauthorizedException;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//토큰 유효성 검사 -> 실패하면 예외 API 리다이렉션
@Log4j2
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                                       final Object handler) throws IOException {
        final String header = request.getHeader(AuthConstants.AUTH_HEADER_ACCESS);
        if (header != null) {
            if (TokenUtils.isValidToken(header)) {
                return true;
            }
        }
        response.sendRedirect("/error/unauthorized"); //예외 API
        return false;
        //throw new UserUnauthorizedException("User Unauthorized",ErrorCode.USER_UNAUTHORIZED);
    }
}
