package com.example.onjeong.user.service;

import com.example.onjeong.family.domain.Family;

import com.example.onjeong.profile.domain.Profile;
import com.example.onjeong.profile.repository.ProfileRepository;

import com.example.onjeong.home.domain.Flower;
import com.example.onjeong.home.domain.FlowerColor;
import com.example.onjeong.home.domain.FlowerKind;
import com.example.onjeong.home.repository.FlowerRepository;

import com.example.onjeong.user.Auth.JwtTokenProvider;
import com.example.onjeong.user.Auth.TokenUtils;
import com.example.onjeong.user.domain.MyUserDetails;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.domain.UserRole;
import com.example.onjeong.family.repository.FamilyRepository;
import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final ProfileRepository profileRepository;
    private final FlowerRepository flowerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    //가족회원이 없는 회원 가입
    @Transactional
    public User signUp(final UserJoinDto userJoinDto){
        final Family family= Family.builder()
                .familyCoin(0)
                .build();
        final User user= User.builder()
                .userName(userJoinDto.getUserName())
                .userNickname(userJoinDto.getUserNickname())
                .userPassword(passwordEncoder.encode(userJoinDto.getUserPassword()))
                .userStatus(userJoinDto.getUserStatus())
                .userBirth(userJoinDto.getUserBirth())
                .role(UserRole.ROLE_USER)
                .family(familyRepository.save(family))
                .build();               
        User savedUser= userRepository.save(user);
        profileRepository.save(Profile.builder().user(savedUser).checkProfileImage(false).checkProfileUpload(false).family(family).build());
        
        Flower newFlower = Flower.builder()
                .flowerBloom(false)
                .flowerKind(FlowerKind.values()[new Random().nextInt(FlowerKind.values().length)])
                .flowerColor(FlowerColor.values()[new Random().nextInt(FlowerColor.values().length)])
                .flowerLevel(1)
                .family(family)
                .build();
        flowerRepository.save(newFlower);
        return savedUser;
    }

    //가족회원이 있는 회원 가입
    @Transactional
    public User signUpJoined(final UserJoinedDto userJoinedDto){
        String joinedNickname=userJoinedDto.getJoinedNickname();
        Optional<User> joinedUser=userRepository.findByUserNickname(joinedNickname);

        if(joinedUser.isPresent()){
            final User user= User.builder()
                    .userName(userJoinedDto.getUserName())
                    .userNickname(userJoinedDto.getUserNickname())
                    .userPassword(passwordEncoder.encode(userJoinedDto.getUserPassword()))
                    .userStatus(userJoinedDto.getUserStatus())
                    .userBirth(userJoinedDto.getUserBirth())
                    .role(UserRole.ROLE_USER)
                    .family(joinedUser.get().getFamily())
                    .build();
            User savedUser= userRepository.save(user);
            profileRepository.save(Profile.builder().user(savedUser).checkProfileImage(false).checkProfileUpload(false)
                    .family(joinedUser.get().getFamily()).build());
            return savedUser;
        }
        else{
            return null;
        }
    }

    //로그인
    @Transactional
    public User login(final UserLoginDto userLoginDto, @ApiIgnore HttpSession session) throws Exception{
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.getUserNickname(), userLoginDto.getUserPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MyUserDetails principal = (MyUserDetails) authentication.getPrincipal();

        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,SecurityContextHolder.getContext());
        return principal.getUser();
    }

    //회원정보 수정
    @Transactional
    public String userInformationModify(final UserAccountsDto userAccountsDto){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName()).get();

        user.updateUserName(userAccountsDto.getUserName());
        user.updateUserNickname(user.getUserNickname());    //변경 x
        user.setEncryptedPassword(passwordEncoder.encode(userAccountsDto.getUserPassword()));
        user.updateUserStatus(userAccountsDto.getUserStatus());
        user.updateUserBirth(userAccountsDto.getUserBirth());      //변경 x
        user.updateRole(UserRole.ROLE_USER);
        user.updateFamily(user.getFamily());            //변경 x
        return "true";
    }

    //회원탈퇴
    @Transactional
    public String userDelete(final UserDeleteDto userDeleteDto) throws Exception{
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName()).get();
        if(passwordEncoder.matches(userDeleteDto.getUserPassword(),user.getUserPassword())){
            userRepository.delete(user);
            return "true";
        }
        else return "false";
    }

    public boolean isUserNicknameDuplicated(final String userNickname) {
        return userRepository.existsByUserNickname(userNickname);
    }

    //유저 기본정보 알기
    @Transactional
    public UserDto userGet(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName()).get();
        return UserDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userStatus(user.getUserStatus())
                .userBirth(user.getUserBirth().toString())
                .build();
    }


    @Transactional
    public String refreshToken(String token, String refreshToken) {
        String accessToken= null;
        try {
            User user = userRepository.findByUserNickname(TokenUtils.getUserNicknameFromToken(token)).orElseThrow();
            if(user.getRefreshToken().equals(refreshToken)&&jwtTokenProvider.validateRefreshTokenExceptExpiration(refreshToken)){   //만료기간 안지남-> access만
                if(!jwtTokenProvider.validateTokenExceptExpiration(token)){
                    accessToken = TokenUtils.generateJwtToken(user);
                }
                else{
                    System.out.println("access 토큰이 만료되지 않았습니다.");
                }
            }
            else if(user.getRefreshToken().equals(refreshToken)&&!jwtTokenProvider.validateRefreshTokenExceptExpiration(refreshToken)){ //만료기간 지남->다시 로그인
                //로그인페이지로 이동
            }
            else{
                System.out.println("refresh token이 다릅니다.");
            }

        }catch (Exception e) {
            System.out.println(e);
        }
        return accessToken;
    }

}
