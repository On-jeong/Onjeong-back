package com.example.onjeong.controller;

import com.example.onjeong.domain.User;
import com.example.onjeong.dto.UserJoinDto;
import com.example.onjeong.dto.UserJoinedDto;
import com.example.onjeong.dto.UserLoginDto;
import io.swagger.annotations.*;
import com.example.onjeong.repository.UserRepository;
import com.example.onjeong.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="User")
@RequiredArgsConstructor
@RestController
@Log4j2
public class UserController {
    private final UserService userService;

    @ApiOperation(value="가족회원이 없는 회원 가입")
    @PostMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signUp(@RequestBody UserJoinDto userJoinDto){
        return userService.isUserNicknameDuplicated(userJoinDto.getUserNickname())
                ? ResponseEntity.badRequest().build()
                : ResponseEntity.ok(TokenUtils.generateJwtToken(userService.signUp(userJoinDto)));
    }

    @ApiOperation(value="가족회원이 있는 회원 가입")
    @PostMapping(value="/accounts/joined", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signUpJoined(@RequestBody UserJoinedDto userJoinedDto){
        User user=userService.signUpJoined(userJoinedDto);
        return ResponseEntity.ok(TokenUtils.generateJwtToken(user));
    }

    @ApiOperation(value="로그인")
    @PostMapping(value="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto){
        try{
            return ResponseEntity.ok(TokenUtils.generateJwtToken(userService.login(userLoginDto)));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
