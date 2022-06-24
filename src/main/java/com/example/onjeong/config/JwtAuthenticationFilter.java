package com.example.onjeong.config;

import com.example.onjeong.user.Auth.AuthConstants;
import com.example.onjeong.user.Auth.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    public void doFilterInternal(final HttpServletRequest req, final HttpServletResponse res, final FilterChain chain) throws IOException, ServletException {
        try {
            String token = req.getHeader(AuthConstants.AUTH_HEADER);
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
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }
        chain.doFilter(req, res);
    }
}
