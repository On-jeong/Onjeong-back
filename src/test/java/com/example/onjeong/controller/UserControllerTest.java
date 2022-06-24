package com.example.onjeong.controller;


import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.user.service.UserService;
import com.example.onjeong.user.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class UserControllerTest {
    @Autowired
    UserController userController;
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @Test
    public void test(){

        UserJoinDto userJoinDto=new UserJoinDto();
        userJoinDto.setUserName("홍길동");
        userJoinDto.setUserNickname("abc");
        userJoinDto.setUserPassword("abc123");
        userJoinDto.setUserStatus("daughter");


    }

    @Test
    public void test2(){

    }
}
