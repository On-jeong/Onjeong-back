package com.example.onjeong.user.controller;

import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.result.ResultCode;
import com.example.onjeong.result.ResultResponse;
import com.example.onjeong.user.Auth.AuthConstants;
import com.example.onjeong.user.Auth.TokenUtils;
import com.example.onjeong.user.dto.*;
import com.example.onjeong.user.exception.UserNicknameDuplicationException;
import io.swagger.annotations.*;
import com.example.onjeong.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<ResultResponse> signUp(@RequestBody UserJoinDto userJoinDto){
        if(userService.isUserNicknameDuplicated(userJoinDto.getUserNickname())) {
            throw new UserNicknameDuplicationException("UserNickname Duplication", ErrorCode.USER_NICKNAME_DUPLICATION);
        }
        userService.signUp(userJoinDto);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.REGISTER_SUCCESS));
    }

    @ApiOperation(value="가족회원이 있는 회원 가입")
    @PostMapping(value="/accounts/joined", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> signUpJoined(@RequestBody UserJoinedDto userJoinedDto){
        if(userService.isUserNicknameDuplicated(userJoinedDto.getUserNickname())) {
            throw new UserNicknameDuplicationException("UserNickname Duplication", ErrorCode.USER_NICKNAME_DUPLICATION);
        }
        userService.signUpJoined(userJoinedDto);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.REGISTER_SUCCESS));
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
    public ResponseEntity<ResultResponse> logout(HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok(ResultResponse.of(ResultCode.LOGOUT_SUCCESS));
    }


    @ApiOperation(value="회원정보 수정")
    @PutMapping(value="/accounts/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> modifyUserInformation (@Validated  @RequestBody UserAccountDto userAccountsDto){
        userService.modifyUserInformation(userAccountsDto);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.MODIFY_USER_INFORMATION_SUCCESS));
    }

    @ApiOperation(value = "회원탈퇴")
    @DeleteMapping(value = "/accounts")
    public ResponseEntity<ResultResponse> deleteUser(@RequestBody UserDeleteDto userDeleteDto, HttpServletRequest httpServletRequest) throws Exception{
        userService.deleteUser(userDeleteDto, httpServletRequest);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_USER_SUCCESS));
    }

    @ApiOperation(value="유저 기본정보 알기")
    @GetMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> getUser (){
        UserDto data= userService.getUser();
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_USER_SUCCESS,data));
    }

    @ApiOperation(value="기본 홈")
    @GetMapping(value="/home", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> getHome (){
        return ResponseEntity.ok(ResultResponse.of(ResultCode.LOGOUT_SUCCESS));
    }


    @ApiOperation(value = "Access Token 재발급", notes = "Access Token을 재발급한다")
    @PostMapping(value = "/refresh")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AuthorizationAccess", value = "AuthorizationAccess", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "AuthorizationRefresh", value = "AuthorizationRefresh", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<ResultResponse> refreshToken(
            @RequestHeader(value="AuthorizationAccess") String token,
            @RequestHeader(value="AuthorizationRefresh") String refreshToken
            ) {
        String accessToken= userService.refreshToken(refreshToken);
        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthConstants.AUTH_HEADER_ACCESS, accessToken);
        return ResponseEntity.ok().headers(headers).body(ResultResponse.of(ResultCode.NEW_TOKEN_SUCCESS));
    }
}
