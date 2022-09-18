package com.example.onjeong.user.error;

import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.user.exception.TokenExpiredJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping(value="/error")
public class ErrorController {

    @GetMapping(value = "/unauthorized")
    public void unauthorized() {
        throw new TokenExpiredJwtException("Token Expired Error", ErrorCode.TOKEN_EXPIRED);
    }
}
