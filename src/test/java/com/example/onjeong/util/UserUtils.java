package com.example.onjeong.util;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.domain.UserRole;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.Random;

public class UserUtils {

    public static User getRandomUser(Family family){
        final Long userId= 50L;
        final String userName= RandomStringUtils.random(8, true, false);
        final String userNickname= RandomStringUtils.random(10, true, true);
        final String userPassword= RandomStringUtils.random(10, true, true);
        final String userStatus= RandomStringUtils.random(10, true, true);
        final LocalDate userBirth= LocalDate.now();
        return getUser(userId, userName, userNickname, userPassword, userStatus, userBirth, family);
    }


    public static User getUser(Long userId, String userName, String userNickname, String userPassword,
                               String userStatus, LocalDate userBirth, Family family){
        return User.builder()
                .userId(userId)
                .userName(userName)
                .userNickname(userNickname)
                .userPassword(userPassword)
                .userStatus(userStatus)
                .userBirth(userBirth)
                .role(UserRole.ROLE_USER)
                .family(family)
                .build();
    }
}