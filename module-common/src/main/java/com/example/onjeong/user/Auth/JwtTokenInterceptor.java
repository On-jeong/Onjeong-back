package com.example.onjeong.user.Auth;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class JwtTokenInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) throws ServletException, IOException {
        final String token = request.getHeader(AuthConstants.AUTH_HEADER_ACCESS);
        if(token != null) return true;
        else{
            request.setAttribute("exception", "AuthenticationException");
            request.setAttribute("message", "token null");
            request.getRequestDispatcher("/api/error").forward(request, response);
            return false;
        }
    }
}
