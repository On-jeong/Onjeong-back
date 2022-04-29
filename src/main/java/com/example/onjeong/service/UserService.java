package com.example.onjeong.service;

import com.example.onjeong.domain.Family;
import com.example.onjeong.domain.MyUserDetails;
import com.example.onjeong.domain.User;
import com.example.onjeong.domain.UserRole;
import com.example.onjeong.dto.UserJoinDto;
import com.example.onjeong.dto.UserJoinedDto;
import com.example.onjeong.dto.UserLoginDto;
import com.example.onjeong.repository.FamilyRepository;
import com.example.onjeong.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
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
        return userRepository.save(user);
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
    public User login(final UserLoginDto userLoginDto) throws Exception{
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.getUserNickname(), userLoginDto.getUserNickname()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        MyUserDetails principal = (MyUserDetails) authentication.getPrincipal();
        return principal.getUser();
    }

    public boolean isUserNicknameDuplicated(final String userNickname) {
        return userRepository.existsByUserNickname(userNickname);
    }

}
