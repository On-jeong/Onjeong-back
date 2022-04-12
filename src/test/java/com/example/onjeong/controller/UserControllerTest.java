package com.example.onjeong.controller;


import com.example.onjeong.domain.User;
import com.example.onjeong.domain.UserRole;
import com.example.onjeong.dto.UserJoinDto;
import com.example.onjeong.repository.UserRepository;
import com.example.onjeong.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.assertj.core.api.Assertions;

import java.sql.Date;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class UserControllerTest {
    @Autowired UserController userController;
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @Test
    public void test(){
        /*
        UserJoinDto userJoinDto=new UserJoinDto();
        userJoinDto.setUserName("홍길동");
        userJoinDto.setUserNickname("abc");
        userJoinDto.setUserPassword("abc123");
        userJoinDto.setUserStatus("daughter");
        userJoinDto.setUserBirth(Date.valueOf(LocalDate.of(2022,7,7)));
        userController.join(userJoinDto);

        User user =new User();
        user.setUserName("홍길동");
        user.setUserNickname("abc");
        user.setUserPassword("abc123");
        user.setUserStatus("daughter");
        user.setUserBirth(Date.valueOf(LocalDate.of(2022,7,7)));
        user.setRole(UserRole.USER);
        userRepository.save(user);
        User result=userRepository.findById(user.getUserId()).get();
        System.out.println("user: "+user);
        System.out.println("result: "+result);
        assertThat(user.equals(result)).isTrue();

         */
    }

    @Test
    public void test2(){

    }
}
