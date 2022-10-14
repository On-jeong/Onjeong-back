package com.example.onjeong.profile.service;

import com.example.onjeong.S3.S3Uploader;
import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.profile.domain.*;
import com.example.onjeong.profile.dto.*;
import com.example.onjeong.profile.exception.ProfileNotExistException;
import com.example.onjeong.profile.repository.*;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.exception.UserNotExistException;
import com.example.onjeong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final HateRepository hateRepository;
    private final ExpressionRepository expressionRepository;
    private final InterestRepository interestRepository;
    private final S3Uploader s3Uploader;

    @Value("https://onjeong.s3.ap-northeast-2.amazonaws.com/")
    private String AWS_S3_BUCKET_URL;


    //가족 프로필 중 구성원 보여주기
    @Transactional
    public List<FamilyGetDto> allUserGet(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        List<User> users= user.getFamily().getUsers();
        final List<FamilyGetDto> result= new ArrayList<>();
        for(User u:users){
            final FamilyGetDto familyGetDto= FamilyGetDto.builder()
                    .userId(u.getUserId())
                    .userStatus(u.getUserStatus())
                    .build();
            result.add(familyGetDto);
        }
        return result;
    }

    //프로필 상단에 개인 정보 보여주기
    @Transactional
    public UserInformationDto userInformationGet(final Long userId){
        User user= userRepository.findById(userId)
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        Profile profile= profileRepository.findByUser(user)
                .orElseThrow(()-> new ProfileNotExistException("profile not exist", ErrorCode.PROFILE_NOTEXIST));
        return UserInformationDto.builder().user(user).profileImageUrl(profile.getProfileImageUrl())
                .checkProfileImage(profile.isCheckProfileImage()).build();
    }

    //프로필 사진 등록하기
    @Transactional
    public Profile profileImageRegister(final MultipartFile multipartFile){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        Profile profile= profileRepository.findByUser(user)
                .orElseThrow(()-> new ProfileNotExistException("profile not exist", ErrorCode.PROFILE_NOTEXIST));
        if(profile.isCheckProfileImage()) s3Uploader.deleteFile(profile.getProfileImageUrl().substring(AWS_S3_BUCKET_URL.length()));
        String imageUrl= s3Uploader.upload(multipartFile, "profile");
        profile.updateProfileImageUrl(imageUrl);
        profile.updateCheckProfileImage(true);
        profile.updateCheckProfileUpload(true);
        return profile;
    }

    //프로필 사진 삭제하기
    @Transactional
    public void profileImageDelete(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        Profile profile= profileRepository.findByUser(user)
                .orElseThrow(()-> new ProfileNotExistException("profile not exist", ErrorCode.PROFILE_NOTEXIST));
        String deletedImage= profile.getProfileImageUrl();
        String fileName = deletedImage.substring(AWS_S3_BUCKET_URL.length());
        s3Uploader.deleteFile(fileName);

        profile.updateProfileImageUrl("");
        profile.updateCheckProfileImage(false);
    }

    //상태메시지 보여주기
    @Transactional
    public ProfileMessageDto profileMessageGet(final Long userId){
        User user= userRepository.findById(userId)
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        Profile profile= profileRepository.findByUser(user)
                .orElseThrow(()-> new ProfileNotExistException("profile not exist", ErrorCode.PROFILE_NOTEXIST));
        return ProfileMessageDto.builder()
                .message(profile.getMessage())
                .build();
    }

    //상태메시지 작성하기
    @Transactional
    public Profile profileMessageRegister(final ProfileMessageDto profileMessageDto){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        Profile profile= profileRepository.findByUser(user)
                .orElseThrow(()-> new ProfileNotExistException("profile not exist", ErrorCode.PROFILE_NOTEXIST));
        profile.updateMessage(profileMessageDto.getMessage());
        profile.updateCheckProfileUpload(true);
        return profile;
    }

    //상태메시지 수정하기
    @Transactional
    public void profileMessageModify(final ProfileMessageDto profileMessageDto){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        Profile profile= profileRepository.findByUser(user)
                .orElseThrow(()-> new ProfileNotExistException("profile not exist", ErrorCode.PROFILE_NOTEXIST));
        profile.updateMessage(profileMessageDto.getMessage());
    }


    //유저 프로필(개인정보+상태메시지) 보여주기
    @Transactional
    public UserProfileGetDto userProfileGet(final Long userId){
        User user= userRepository.findById(userId)
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        Profile profile= profileRepository.findByUser(user)
                .orElseThrow(()-> new ProfileNotExistException("profile not exist", ErrorCode.PROFILE_NOTEXIST));
        return UserProfileGetDto.builder().user(user).profileImageUrl(profile.getProfileImageUrl())
                .checkProfileImage(profile.isCheckProfileImage()).message(profile.getMessage()).build();
    }

    //자기소개 답변 목록 보여주기
    @Transactional
    public SelfIntroductionAnswerListGetDto getSelfIntroductionAnswer(final Long userId){
        User user= getUserByUserId(userId);
        Profile profile= getProfileByUser(user);
        List<SelfIntroductionAnswerGetDto> favorites= getFavorites(profile);
        List<SelfIntroductionAnswerGetDto> hates= getHates(profile);
        List<SelfIntroductionAnswerGetDto> expressions= getExpressions(profile);
        List<SelfIntroductionAnswerGetDto> interests= getInterests(profile);
        return SelfIntroductionAnswerListGetDto.builder().favorites(favorites).hates(hates)
                .expressions(expressions).interests(interests).build();
    }

    //자기소개 답변 작성하기
    @Transactional
    public Profile registerSelfIntroductionAnswer(final Long userId, final SelfIntroductionAnswerRegisterDto selfIntroductionAnswerRegisterDto, final String category){
        User user= getUserByUserId(userId);
        Profile profile= getProfileByUser(user);
        switch(category){
            case "favorite":
                final Favorite favorite= Favorite.builder()
                        .favoriteContent(selfIntroductionAnswerRegisterDto.getSelfIntroductionAnswerContent())
                        .profile(profile)
                        .build();
                favoriteRepository.save(favorite);
                break;
            case "hate":
                final Hate hate= Hate.builder()
                        .hateContent(selfIntroductionAnswerRegisterDto.getSelfIntroductionAnswerContent())
                        .profile(profile)
                        .build();
                hateRepository.save(hate);
                break;
            case "expression":
                final Expression expression= Expression.builder()
                        .expressionContent(selfIntroductionAnswerRegisterDto.getSelfIntroductionAnswerContent())
                        .profile(profile)
                        .build();
                expressionRepository.save(expression);
                break;
            case "interest":
                final Interest interest= Interest.builder()
                        .interestContent(selfIntroductionAnswerRegisterDto.getSelfIntroductionAnswerContent())
                        .profile(profile)
                        .build();
                interestRepository.save(interest);
                break;
        }
        profile.updateCheckProfileUpload(true);
        return profile;
    }

    //자기소개 답변 삭제하기
    @Transactional
    public void removeSelfIntroductionAnswer(final Long userId, final Long selfIntroductionAnswerId, final String category){
        User user= getUserByUserId(userId);
        Profile profile= getProfileByUser(user);
        switch(category){
            case "favorite":
                favoriteRepository.deleteByFavoriteIdAndProfile(selfIntroductionAnswerId, profile);
                break;
            case "hate":
                hateRepository.deleteByHateIdAndProfile(selfIntroductionAnswerId, profile);
                break;
            case "expression":
                expressionRepository.deleteByExpressionIdAndProfile(selfIntroductionAnswerId, profile);
                break;
            case "interest":
                interestRepository.deleteByInterestIdAndProfile(selfIntroductionAnswerId, profile);
                break;
        }
    }


    @Transactional
    public boolean checkProfileUpload(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        Profile profile= profileRepository.findByUser(user)
                .orElseThrow(()-> new ProfileNotExistException("profile not exist", ErrorCode.PROFILE_NOTEXIST));
        return profile.isCheckProfileUpload();
    }

    @Transactional
    public List<SelfIntroductionAnswerGetDto> getFavorites(final Profile profile){
        final List<SelfIntroductionAnswerGetDto> result= new ArrayList<>();
        List<Favorite> favorites= profile.getFavorites();
        for(Favorite f:favorites){
            final SelfIntroductionAnswerGetDto selfIntroductionAnswerGetDto= SelfIntroductionAnswerGetDto.builder()
                    .selfIntroductionAnswerId(f.getFavoriteId())
                    .selfIntroductionAnswerContent(f.getFavoriteContent())
                    .build();
            result.add(selfIntroductionAnswerGetDto);
        }
        return result;
    }

    @Transactional
    public List<SelfIntroductionAnswerGetDto> getHates(final Profile profile){
        final List<SelfIntroductionAnswerGetDto> result= new ArrayList<>();
        List<Hate> hates= profile.getHates();
        for(Hate f:hates){
            final SelfIntroductionAnswerGetDto selfIntroductionAnswerGetDto= SelfIntroductionAnswerGetDto.builder()
                    .selfIntroductionAnswerId(f.getHateId())
                    .selfIntroductionAnswerContent(f.getHateContent())
                    .build();
            result.add(selfIntroductionAnswerGetDto);
        }
        return result;
    }

    @Transactional
    public List<SelfIntroductionAnswerGetDto> getExpressions(final Profile profile){
        final List<SelfIntroductionAnswerGetDto> result= new ArrayList<>();
        List<Expression> expressions= profile.getExpressions();
        for(Expression e:expressions){
            final SelfIntroductionAnswerGetDto selfIntroductionAnswerGetDto= SelfIntroductionAnswerGetDto.builder()
                    .selfIntroductionAnswerId(e.getExpressionId())
                    .selfIntroductionAnswerContent(e.getExpressionContent())
                    .build();
            result.add(selfIntroductionAnswerGetDto);
        }
        return result;
    }

    @Transactional
    public List<SelfIntroductionAnswerGetDto> getInterests(final Profile profile){
        final List<SelfIntroductionAnswerGetDto> result= new ArrayList<>();
        List<Interest> interests= profile.getInterests();
        for(Interest i:interests){
            final SelfIntroductionAnswerGetDto selfIntroductionAnswerGetDto= SelfIntroductionAnswerGetDto.builder()
                    .selfIntroductionAnswerId(i.getInterestId())
                    .selfIntroductionAnswerContent(i.getInterestContent())
                    .build();
            result.add(selfIntroductionAnswerGetDto);
        }
        return result;
    }

    @Transactional
    public User getUserByUserId(final Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
    }

    @Transactional
    public Profile getProfileByUser(final User user){
        return profileRepository.findByUser(user)
                .orElseThrow(()-> new ProfileNotExistException("profile not exist", ErrorCode.PROFILE_NOTEXIST));
    }
}
