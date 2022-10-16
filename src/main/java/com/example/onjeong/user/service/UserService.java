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
import com.example.onjeong.user.Auth.JwtTokenProvider;
import com.example.onjeong.user.Auth.TokenUtils;
import com.example.onjeong.user.domain.MyUserDetails;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.domain.UserRole;
import com.example.onjeong.family.repository.FamilyRepository;
import com.example.onjeong.user.exception.*;
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
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Optional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final ProfileRepository profileRepository;
    private final AnniversaryRepository anniversaryRepository;
    private final BoardRepository boardRepository;
    private final FlowerRepository flowerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
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
    public User login(UserLoginDto userLoginDto, @ApiIgnore HttpSession session) throws Exception{
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.getUserNickname(), userLoginDto.getUserPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MyUserDetails principal = (MyUserDetails) authentication.getPrincipal();

        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,SecurityContextHolder.getContext());
        return principal.getUser();
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
    public void deleteUser(UserDeleteDto userDeleteDto, HttpServletRequest httpServletRequest){
        HttpSession httpSession = httpServletRequest.getSession();
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        Family family= familyRepository.findById(user.getFamily().getFamilyId())
                .orElseThrow(()-> new FamilyNotExistException("family not exist", ErrorCode.FAMILY_NOTEXIST));
        Long profileId= profileRepository.findByUser(user)
                .orElseThrow(()-> new ProfileNotExistException("profile not exist", ErrorCode.PROFILE_NOTEXIST)).getProfileId();
        if(passwordEncoder.matches(userDeleteDto.getUserPassword(),user.getUserPassword())){
            expressionRepository.deleteInBatch(expressionRepository.findAllById(Collections.singleton(profileId)));
            favoriteRepository.deleteInBatch(favoriteRepository.findAllById(Collections.singleton(profileId)));
            hateRepository.deleteInBatch(hateRepository.findAllById(Collections.singleton(profileId)));
            interestRepository.deleteInBatch(interestRepository.findAllById(Collections.singleton(profileId)));
            String profileImgUrl= profileRepository.findByUser(user)
                    .orElseThrow(()-> new ProfileNotExistException("profile not exist", ErrorCode.PROFILE_NOTEXIST)).getProfileImageUrl();
            if(profileImgUrl!=null) s3Uploader.deleteFile(profileImgUrl.substring(AWS_S3_BUCKET_URL.length()));
            profileRepository.deleteByUser(user);
            boardRepository.deleteByUser(user);
            answerRepository.deleteByUser(user.getUserId());
            mailRepository.deleteByReceiver(user.getUserId());
            mailRepository.deleteBySender(user.getUserId());
            userRepository.deleteUser(user.getUserId());
            if(family.getUsers().size()==0) {
                anniversaryRepository.deleteByFamily(family);
                questionRepository.deleteByFamily(family);
                coinHistoryRepository.deleteByFamily(family);
                flowerRepository.deleteByFamily(family);
                familyRepository.delete(family);
            }
            httpSession.invalidate();
            SecurityContextHolder.getContext().setAuthentication(null);
            SecurityContextHolder.clearContext();
        }
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

    //Access Token 재발급
    @Transactional
    public String refreshToken(String refreshToken) {
        String accessToken= null;
        Optional<User> user= userRepository.findByRefreshToken(refreshToken);
        if(user.isPresent()){
            if(jwtTokenProvider.validateRefreshTokenExceptExpiration(refreshToken)) accessToken = TokenUtils.generateJwtToken(user.get());
            else throw new RefreshTokenExpiredException("refresh token expired",ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
        else{
            throw new RefreshTokenNotSameException("refresh token not same",ErrorCode.REFRESH_TOKEN_NOT_SAME);
        }
        return accessToken;
    }


    public boolean isUserNicknameDuplicated(String userNickname) {
        return userRepository.existsByUserNickname(userNickname);
    }
}
