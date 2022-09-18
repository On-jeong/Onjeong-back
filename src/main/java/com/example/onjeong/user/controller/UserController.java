package com.example.onjeong.user.controller;

import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.user.Auth.AuthConstants;
import com.example.onjeong.user.Auth.TokenUtils;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.dto.*;
import com.example.onjeong.user.exception.UserNicknameDuplicationException;
import io.swagger.annotations.*;
import com.example.onjeong.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Api(tags="User")
@RequiredArgsConstructor
@RestController
@Log4j2
public class UserController {
    private final UserService userService;

    @ApiOperation(value="가족회원이 없는 회원 가입")
    @PostMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signUp(@RequestBody UserJoinDto userJoinDto){
        if(userService.isUserNicknameDuplicated(userJoinDto.getUserNickname())) {
            throw new UserNicknameDuplicationException("UserNickname Duplication", ErrorCode.USER_NICKNAME_DUPLICATION);
        }
        else return ResponseEntity.ok(TokenUtils.generateJwtToken(userService.signUp(userJoinDto)));
    }

    @ApiOperation(value="가족회원이 있는 회원 가입")
    @PostMapping(value="/accounts/joined", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signUpJoined(@RequestBody UserJoinedDto userJoinedDto){
        if(userService.isUserNicknameDuplicated(userJoinedDto.getUserNickname())) {
            throw new UserNicknameDuplicationException("UserNickname Duplication", ErrorCode.USER_NICKNAME_DUPLICATION);
        }
        else return ResponseEntity.ok(TokenUtils.generateJwtToken(userService.signUpJoined(userJoinedDto)));
    }

    @ApiOperation(value="로그인")
    @PostMapping(value="/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto, @ApiIgnore HttpSession session){
        try{
            return ResponseEntity.ok(TokenUtils.generateJwtToken(userService.login(userLoginDto, session)));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "로그아웃")
    @PostMapping(value = "/logout")
    public ResponseEntity<HttpStatus> logout(HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ApiOperation(value="회원정보 수정")
    @PutMapping(value="/accounts/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> userInformationModify (@Validated  @RequestBody UserAccountsDto userAccountsDto){
        String result= userService.userInformationModify(userAccountsDto);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "회원탈퇴")
    @DeleteMapping(value = "/accounts")
    public ResponseEntity<HttpStatus> userDelete(@RequestBody UserDeleteDto userDeleteDto, HttpServletRequest httpServletRequest) throws Exception{
        if(userService.userDelete(userDeleteDto, httpServletRequest)) {
            return ResponseEntity.ok(HttpStatus.OK);
        }
        else return ResponseEntity.ok(HttpStatus.BAD_GATEWAY);
    }

    @ApiOperation(value="유저 기본정보 알기")
    @GetMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> userGet (){
        UserDto userDto= userService.userGet();
        return ResponseEntity.ok(userDto);
    }

    @ApiOperation(value="기본 홈")
    @GetMapping(value="/home", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> homeGet (){
        return ResponseEntity.ok("home");
    }


    @ApiOperation(value = "Access Token 재발급", notes = "Access Token을 재발급한다")
    @PostMapping(value = "/refresh")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AuthorizationAccess", value = "AuthorizationAccess", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "AuthorizationRefresh", value = "AuthorizationRefresh", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<String> refreshToken(
            @RequestHeader(value="AuthorizationAccess") String token,
            @RequestHeader(value="AuthorizationRefresh") String refreshToken
            ) {
        String accessToken= userService.refreshToken(token, refreshToken);
        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthConstants.AUTH_HEADER_ACCESS, accessToken);
        return ResponseEntity.ok().headers(headers).body("New Access Token 발급");
    }
}
