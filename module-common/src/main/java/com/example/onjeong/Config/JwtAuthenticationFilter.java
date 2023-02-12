package com.example.onjeong.Config;

import com.example.onjeong.user.Auth.AuthConstants;
import com.example.onjeong.user.Auth.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilterInternal(final HttpServletRequest req, final HttpServletResponse res, final FilterChain chain) throws IOException, ServletException {
        try {
            String token = req.getHeader(AuthConstants.AUTH_HEADER_ACCESS);
            if (token != null) {
                String userNickname = TokenUtils.getUserNicknameFromToken(token);
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userNickname,
                        null,
                        AuthorityUtils.NO_AUTHORITIES
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (ExpiredJwtException ex) {
            if(!req.getRequestURI().equals("/refresh")){
                Map<String, String> map = new HashMap<>();
                res.setContentType("application/json");
                res.setCharacterEncoding("utf-8");
                map.put("status", "401");
                map.put("message", "ACCESS TOKEN EXPIRED");
                map.put("code", "A004");
                res.getWriter().write(objectMapper.writeValueAsString(map));
                res.setStatus(401);
                return;
            }
        } catch (MalformedJwtException | SignatureException e){
            Map<String, String> map = new HashMap<>();
            res.setContentType("application/json");
            res.setCharacterEncoding("utf-8");
            map.put("status", "401");
            map.put("message", "ACCESS TOKEN NOT SAME");
            map.put("code", "A005");
            res.getWriter().write(objectMapper.writeValueAsString(map));
            res.setStatus(401);
            return;
        }
        chain.doFilter(req, res);
    }
}