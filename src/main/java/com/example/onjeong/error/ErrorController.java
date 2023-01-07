package com.example.onjeong.error;

import com.example.onjeong.user.exception.TokenNullException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ErrorController {

    @GetMapping("/api/error")
    public void error(HttpServletRequest request)  {
        String message = (String) request.getAttribute("message");
        String exception = (String) request.getAttribute("exception");

        if ("AuthenticationException".equals(exception)) {
            throw new TokenNullException(message, ErrorCode.TOKEN_NULL);
        }
    }
}