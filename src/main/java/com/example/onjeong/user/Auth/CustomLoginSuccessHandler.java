package com.example.onjeong.user.Auth;

import com.example.onjeong.result.ResultCode;
import com.example.onjeong.result.ResultResponse;
import com.example.onjeong.user.domain.MyUserDetails;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.dto.UserDto;
import com.example.onjeong.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//토큰 인증이 성공될 경우 처리
@Log4j2
@RequiredArgsConstructor
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                                  final Authentication authentication) throws ServletException, IOException {
        final User user = ((MyUserDetails) authentication.getPrincipal()).getUser();
        final String accessToken = TokenUtils.generateJwtToken(user);
        final String refreshToken = TokenUtils.generateJwtRefreshToken(user);
        User getUser= userRepository.findByUserNickname(user.getUserNickname()).get();
        getUser.updateRefreshToken(refreshToken);
        userRepository.save(getUser);
        final UserDto userDto= UserDto.builder()
                .userId(getUser.getUserId())
                .userName(getUser.getUserName())
                .userStatus(getUser.getUserStatus())
                .userBirth(getUser.getUserBirth().toString())
                .userNickname(getUser.getUserNickname())
                .familyId(getUser.getFamily().getFamilyId())
                .build();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        String data = objectMapper.writeValueAsString(ResultResponse.of(ResultCode.LOGIN_SUCCESS,userDto));
        response.getWriter().write(data);
        response.addHeader(AuthConstants.AUTH_HEADER_ACCESS, accessToken);
        response.addHeader(AuthConstants.AUTH_HEADER_REFRESH, refreshToken);
    }
}

