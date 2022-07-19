package com.example.onjeong.user.service;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.home.domain.Flower;
import com.example.onjeong.home.domain.FlowerColor;
import com.example.onjeong.home.domain.FlowerKind;
import com.example.onjeong.home.repository.FlowerRepository;
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
    private final FlowerRepository flowerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    //가족회원이 없는 회원 가입
    @Transactional
    public User signUp(final UserJoinDto userJoinDto){
        Family family=new Family();

        final User user= User.builder()
                .userName(userJoinDto.getUserName())
                .userNickname(userJoinDto.getUserNickname())
                .userPassword(passwordEncoder.encode(userJoinDto.getUserPassword()))
                .userStatus(userJoinDto.getUserStatus())
                .userBirth(userJoinDto.getUserBirth())
                .role(UserRole.ROLE_USER)
                .family(familyRepository.save(family))
                .build();
        User getUser = userRepository.save(user);

        Flower newFlower = Flower.builder()
                .flowerBloom(false)
                .flowerKind(FlowerKind.values()[new Random().nextInt(FlowerKind.values().length)])
                .flowerColor(FlowerColor.values()[new Random().nextInt(FlowerColor.values().length)])
                .flowerLevel(1)
                .family(family)
                .build();
        flowerRepository.save(newFlower);

        return getUser;
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
            return userRepository.save(user);
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
    public User userInformationModify(final UserAccountsDto userAccountsDto){
        String userNickname=userAccountsDto.getUserNickname();
        Optional<User> user=userRepository.findByUserNickname(userNickname);

        user.get().updateUserName(userAccountsDto.getUserName());
        user.get().updateUserNickname(user.get().getUserNickname());    //변경 x
        user.get().setEncryptedPassword(passwordEncoder.encode(userAccountsDto.getUserPassword()));
        user.get().updateUserStatus(userAccountsDto.getUserStatus());
        user.get().updateUserBirth(user.get().getUserBirth());      //변경 x
        user.get().updateRole(UserRole.ROLE_USER);
        user.get().updateFamily(user.get().getFamily());            //변경 x

        return userRepository.save(user.get());
    }

    //회원탈퇴
    @Transactional
    public String userDelete(final UserDeleteDto userDeleteDto) throws Exception{
        String userNickname=userDeleteDto.getUserNickname();
        Optional<User> user=userRepository.findByUserNickname(userNickname);

        if(passwordEncoder.matches(userDeleteDto.getUserPassword(),user.get().getUserPassword())){
            userRepository.delete(user.get());
            return "true";
        }
        else return "false";
    }

    public boolean isUserNicknameDuplicated(final String userNickname) {
        return userRepository.existsByUserNickname(userNickname);
    }
}
