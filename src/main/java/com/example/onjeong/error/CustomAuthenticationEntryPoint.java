package com.example.onjeong.error;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException{
        String exception = (String)request.getAttribute("exception");
        if(exception.equals(ErrorCode.ACCESS_TOKEN_EXPIRED.getErrorCode())) {
            setResponse(response, ErrorCode.ACCESS_TOKEN_EXPIRED);
        }
        else if(exception.equals(ErrorCode.ACCESS_TOKEN_NOT_SAME.getErrorCode())) {
            setResponse(response, ErrorCode.ACCESS_TOKEN_NOT_SAME);
        }
        else {
            setResponse(response, ErrorCode.INTER_SERVER_ERROR);
        }
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException{
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        try {
            responseJson.put("status", errorCode.getStatus());
            responseJson.put("message", errorCode.getMessage());
            responseJson.put("code", errorCode.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        response.getWriter().print(responseJson);
    }
}