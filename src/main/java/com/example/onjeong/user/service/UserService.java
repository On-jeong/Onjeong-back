package com.example.onjeong.user.service;

import com.example.onjeong.S3.S3Uploader;
import com.example.onjeong.anniversary.repository.AnniversaryRepository;
import com.example.onjeong.board.repository.BoardRepository;
import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.family.domain.Family;

import com.example.onjeong.family.exception.FamilyNotExistException;
import com.example.onjeong.home.repository.CoinHistoryRepository;
import com.example.onjeong.mail.repository.MailRepository;
import com.example.onjeong.profile.domain.Profile;
import com.example.onjeong.profile.exception.ProfileNotExistException;
import com.example.onjeong.profile.repository.*;

import com.example.onjeong.home.domain.Flower;
import com.example.onjeong.home.domain.FlowerColor;
import com.example.onjeong.home.domain.FlowerKind;
import com.example.onjeong.home.repository.FlowerRepository;

import com.example.onjeong.question.repository.AnswerRepository;
import com.example.onjeong.question.repository.QuestionRepository;
import com.example.onjeong.user.Auth.TokenUtils;
import com.example.onjeong.user.domain.*;
import com.example.onjeong.family.repository.FamilyRepository;
import com.example.onjeong.user.exception.*;
import com.example.onjeong.user.redis.RefreshToken;
import com.example.onjeong.user.redis.RefreshTokenRepository;
import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.user.dto.*;
import com.example.onjeong.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final ProfileRepository profileRepository;
    private final AnniversaryRepository anniversaryRepository;
    private final BoardRepository boardRepository;
    private final FlowerRepository flowerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final S3Uploader s3Uploader;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final CoinHistoryRepository coinHistoryRepository;
    private final ExpressionRepository expressionRepository;
    private final FavoriteRepository favoriteRepository;
    private final HateRepository hateRepository;
    private final InterestRepository interestRepository;
    private final MailRepository mailRepository;
    private final AuthUtil authUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("https://onjeong.s3.ap-northeast-2.amazonaws.com/")
    private String AWS_S3_BUCKET_URL;

    //가족회원이 없는 회원 가입
    @Transactional
    public void signUp(UserJoinDto userJoinDto){
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
        final User savedUser= userRepository.save(user);
        profileRepository.save(Profile.builder().user(savedUser).checkProfileImage(false).checkProfileUpload(false).family(family).build());

        final Flower newFlower = Flower.builder()
                .flowerBloom(false)
                .flowerKind(FlowerKind.values()[new Random().nextInt(FlowerKind.values().length)])
                .flowerColor(FlowerColor.values()[new Random().nextInt(FlowerColor.values().length)])
                .flowerLevel(1)
                .family(family)
                .build();
        flowerRepository.save(newFlower);
    }

    //가족회원이 있는 회원 가입
    @Transactional
    public void signUpJoined(UserJoinedDto userJoinedDto){
        final String joinedNickname= userJoinedDto.getJoinedNickname();
        final User joinedUser= userRepository.findByUserNickname(joinedNickname)
                .orElseThrow(()-> new JoinedUserNotExistException("joined user not exist", ErrorCode.JOINED_USER_NOTEXIST));
        final User user= User.builder()
                .userName(userJoinedDto.getUserName())
                .userNickname(userJoinedDto.getUserNickname())
                .userPassword(passwordEncoder.encode(userJoinedDto.getUserPassword()))
                .userStatus(userJoinedDto.getUserStatus())
                .userBirth(userJoinedDto.getUserBirth())
                .role(UserRole.ROLE_USER)
                .family(joinedUser.getFamily())
                .build();
        final User savedUser= userRepository.save(user);
        profileRepository.save(Profile.builder().user(savedUser).checkProfileImage(false).checkProfileUpload(false)
                .family(joinedUser.getFamily()).build());
    }

    //로그인
    @Transactional
    public void login(UserLoginDto userLoginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.getUserNickname(), userLoginDto.getUserPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    //회원정보 수정
    @Transactional
    public void modifyUserInformation(UserAccountDto userAccountsDto){
        final User loginUser= authUtil.getUserByAuthentication();
        loginUser.updateUserName(userAccountsDto.getUserName());
        loginUser.setEncryptedPassword(passwordEncoder.encode(userAccountsDto.getUserPassword()));
        loginUser.updateUserStatus(userAccountsDto.getUserStatus());
        loginUser.updateUserBirth(userAccountsDto.getUserBirth());
    }

    //회원탈퇴
    @Transactional
    public void deleteUser(UserDeleteDto userDeleteDto){
        final User loginUser= authUtil.getUserByAuthentication();
        final Family family= familyRepository.findById(loginUser.getFamily().getFamilyId())
                .orElseThrow(()-> new FamilyNotExistException("family not exist", ErrorCode.FAMILY_NOTEXIST));
        final Profile profile= profileRepository.findByUser(loginUser)
                .orElseThrow(()-> new ProfileNotExistException("profile not exist", ErrorCode.PROFILE_NOTEXIST));

        if(passwordEncoder.matches(userDeleteDto.getUserPassword(),loginUser.getUserPassword())){
            deleteAllTableRelatedToProfile(profile);
            deleteAllTableRelatedToUser(loginUser, profile.getProfileImageUrl());

            SecurityContextHolder.getContext().setAuthentication(null);
            SecurityContextHolder.clearContext();

            if(family.getUsers().size()==0) {
                deleteAllTableRelatedToFamily(family);
            }
        }

    }

    private void deleteAllTableRelatedToProfile(Profile profile){
        expressionRepository.deleteAllByProfile(profile);
        favoriteRepository.deleteAllByProfile(profile);
        hateRepository.deleteAllByProfile(profile);
        interestRepository.deleteAllByProfile(profile);
    }

    private void deleteAllTableRelatedToUser(User loginUser, String profileImageUrl){
        deleteProfileImage(profileImageUrl);
        profileRepository.deleteByUser(loginUser);
        boardRepository.deleteAllByUser(loginUser);
        answerRepository.deleteAllByUser(loginUser);
        mailRepository.deleteAllByReceiveUser(loginUser);
        mailRepository.deleteAllBySendUser(loginUser);
        userRepository.delete(loginUser);
    }

    private void deleteProfileImage(String profileImageUrl){
        if(profileImageUrl!=null) s3Uploader.deleteFile(profileImageUrl.substring(AWS_S3_BUCKET_URL.length()));
    }

    private void deleteAllTableRelatedToFamily(Family family){
        anniversaryRepository.deleteAllByFamily(family);
        questionRepository.deleteAllByFamily(family);
        coinHistoryRepository.deleteAllByFamily(family);
        flowerRepository.deleteAllByFamily(family);
        familyRepository.delete(family);
    }


    //유저 기본정보 알기
    @Transactional
    public UserDto getUser(){
        final User loginUser= authUtil.getUserByAuthentication();
        return UserDto.builder()
                .userId(loginUser.getUserId())
                .userName(loginUser.getUserName())
                .userStatus(loginUser.getUserStatus())
                .userBirth(loginUser.getUserBirth().toString())
                .userNickname(loginUser.getUserNickname())
                .familyId(loginUser.getFamily().getFamilyId())
                .build();
    }

    //New Access Token 재발급
    @Transactional
    public String refreshToken(String refreshToken) {
        Optional<RefreshToken> getRefreshToken= refreshTokenRepository.findById(refreshToken);
        if(getRefreshToken.isPresent()){
            User user= userRepository.findById(getRefreshToken.get().getUserId())
                    .orElseThrow(()-> new UserNotExistException("user not exist", ErrorCode.USER_NOTEXIST));
            return TokenUtils.generateJwtToken(user);
        }
        else{
            throw new RefreshTokenExpiredException("refresh token expired",ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
    }


    public boolean isUserNicknameDuplicated(String userNickname) {
        return userRepository.existsByUserNickname(userNickname);
    }
}
