package com.example.onjeong.user.controller;

import com.example.onjeong.user.Auth.TokenUtils;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.dto.*;
import io.swagger.annotations.*;
import com.example.onjeong.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
    @PostMapping(value="/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto, @ApiIgnore HttpSession session){
        try{
            return ResponseEntity.ok(TokenUtils.generateJwtToken(userService.login(userLoginDto, session)));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "로그아웃")
    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request,response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @ApiOperation(value="회원정보 수정")
    @PutMapping(value="/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> userInformationModify (@Validated  @RequestBody UserAccountsDto userAccountsDto){
        User user=userService.userInformationModify(userAccountsDto);
        return ResponseEntity.ok(TokenUtils.generateJwtToken(user));
    }

    @ApiOperation(value = "회원탈퇴")
    @DeleteMapping(value = "/accounts")
    public String userDelete(@RequestBody UserDeleteDto userDeleteDto, HttpSession session) throws Exception{
        String result="";
        if(userService.userDelete(userDeleteDto)=="true") {
            Object object=session.getAttribute("login");
            if(object!=null){
                session.removeAttribute("login");
                session.invalidate();

                result="true";
            }
            else result="false";
        }
        else result="false";

        return result;
    }
}
