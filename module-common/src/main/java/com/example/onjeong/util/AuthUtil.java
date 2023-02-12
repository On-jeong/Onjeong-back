package com.example.onjeong.util;

import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.exception.UserNotExistException;
import com.example.onjeong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {
    private final UserRepository userRepository;

    public User getUserByUserId(final Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
    }

    public User getReceiveUserByUserId(final Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> new UserNotExistException("receive user not exist", ErrorCode.RECEIVEUSER_NOTEXIST));
    }

    public User getUserByAuthentication(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
    }
}