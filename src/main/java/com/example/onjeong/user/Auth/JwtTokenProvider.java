package com.example.onjeong.user.Auth;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class JwtTokenProvider {
    public boolean validateTokenExceptExpiration(String jwtToken) {
        try {
            return TokenUtils.getClaimsFormToken(jwtToken).getExpiration().after(new Date());
        } catch(ExpiredJwtException e) {
            return false;
        }
    }

    public boolean validateRefreshTokenExceptExpiration(String jwtRefreshToken) {
        try {
            return TokenUtils.getClaimsFormToken(jwtRefreshToken).getExpiration().after(new Date());
        } catch(ExpiredJwtException e) {
            return false;
        }
    }
}
