package com.example.onjeong.user.Auth;

import com.example.onjeong.result.ResultCode;
import com.example.onjeong.result.ResultResponse;
import com.example.onjeong.user.domain.MyUserDetails;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.dto.UserDto;
import com.example.onjeong.user.redis.RefreshToken;
import com.example.onjeong.user.redis.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Log4j2
@RequiredArgsConstructor
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                                  final Authentication authentication) throws IOException {
        final User user = ((MyUserDetails) authentication.getPrincipal()).getUser();
        final String accessToken = TokenUtils.generateJwtToken(user);
        final RefreshToken refreshToken = TokenUtils.generateJwtRefreshToken(user);
        refreshTokenRepository.save(refreshToken);
        final UserDto userDto= UserDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userStatus(user.getUserStatus())
                .userBirth(user.getUserBirth().toString())
                .userNickname(user.getUserNickname())
                .familyId(user.getFamily().getFamilyId())
                .userEmail(user.getUserEmail())
                .userNotification((user.getDeviceToken() != null))
                .build();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        String data = objectMapper.writeValueAsString(ResultResponse.of(ResultCode.LOGIN_SUCCESS,userDto));
        response.getWriter().write(data);
        response.addHeader(AuthConstants.AUTH_HEADER_ACCESS, accessToken);
        response.addHeader(AuthConstants.AUTH_HEADER_REFRESH, refreshToken.getRefreshToken());
    }
}

