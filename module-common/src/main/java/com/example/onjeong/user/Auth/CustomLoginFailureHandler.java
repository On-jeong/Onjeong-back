package com.example.onjeong.user.Auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class CustomLoginFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String exceptionMsg = "";

        if (exception instanceof AuthenticationServiceException) {
            exceptionMsg="존재하지 않는 사용자입니다";

        } else if(exception instanceof BadCredentialsException) {
            exceptionMsg="아이디 또는 비밀번호가 틀립니다";

        } else if(exception instanceof LockedException) {
            exceptionMsg="잠긴 계정입니다";

        } else if(exception instanceof DisabledException) {
            exceptionMsg="비활성화된 계정입니다";

        } else if(exception instanceof AccountExpiredException) {
            exceptionMsg="만료된 계정입니다";
            
        } else if(exception instanceof CredentialsExpiredException) {
            exceptionMsg="비밀번호가 만료되었습니다";
        }

        Map<String, String> map = new HashMap<>();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        map.put("status", "404");
        map.put("code", "Login fail");
        map.put("message", exceptionMsg);
        response.getWriter().write(objectMapper.writeValueAsString(map));
        response.setStatus(404);

    }
}