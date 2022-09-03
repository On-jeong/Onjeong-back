package com.example.onjeong.profile.service;

import com.example.onjeong.S3.S3Uploader;
import com.example.onjeong.profile.domain.*;
import com.example.onjeong.profile.dto.*;
import com.example.onjeong.profile.repository.*;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<User> user= userRepository.findByUserNickname(authentication.getName());
        List<User> users= user.get().getFamily().getUsers();
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
        User user= userRepository.findById(userId).get();
        Profile profile= profileRepository.findByUser(user).get();
        return UserInformationDto.builder().user(user).profileImageUrl(profile.getProfileImageUrl())
                .checkProfileImage(profile.isCheckProfileImage()).build();
    }

    //프로필 사진 등록하기
    @Transactional
    public void profileImageRegister(final MultipartFile multipartFile){
        try {
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            User user= userRepository.findByUserNickname(authentication.getName()).get();
            Profile profile= profileRepository.findByUser(user).get();
            String imageUrl= s3Uploader.upload(multipartFile, "profile");
            profile.updateProfileImageUrl(imageUrl);
            profile.updateCheckProfileImage(true);
            profile.updateCheckProfileUpload(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //프로필 사진 삭제하기
    @Transactional
    public String profileImageDelete(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName()).get();
        Profile profile= profileRepository.findByUser(user).get();
        String deletedImage= profile.getProfileImageUrl();
        String fileName = deletedImage.substring(AWS_S3_BUCKET_URL.length());
        s3Uploader.deleteFile(fileName);

        profile.updateProfileImageUrl("");
        profile.updateCheckProfileImage(false);
        return "success";
    }

    //상태메시지 보여주기
    @Transactional
    public ProfileMessageDto profileMessageGet(final Long userId){
        User user= userRepository.findById(userId).get();
        Profile profile= profileRepository.findByUser(user).get();
        return ProfileMessageDto.builder()
                .message(profile.getMessage())
                .build();
    }

    //상태메시지 작성하기
    @Transactional
    public void profileMessageRegister(final ProfileMessageDto profileMessageDto){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName()).get();
        Profile profile= profileRepository.findByUser(user).get();
        profile.updateMessage(profileMessageDto.getMessage());
        profile.updateCheckProfileUpload(true);
    }

    //상태메시지 수정하기
    @Transactional
    public String profileMessageModify(final ProfileMessageDto profileMessageDto){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName()).get();
        Profile profile= profileRepository.findByUser(user).get();
        profile.updateMessage(profileMessageDto.getMessage());
        return profile.getMessage();
    }

    //좋아하는 것 목록 보여주기
    @Transactional
    public List<FavoriteGetDto> profileFavoritesGet(final Long userId){
        User user= userRepository.findById(userId).get();
        List<Favorite> favorites=profileRepository.findByUser(user).get().getFavorites();
        final List<FavoriteGetDto> result= new ArrayList<>();
        for(Favorite f:favorites){
            final FavoriteGetDto favoriteGetDto= FavoriteGetDto.builder()
                    .favoriteId(f.getFavoriteId())
                    .favoriteContent(f.getFavoriteContent())
                    .build();
            result.add(favoriteGetDto);
        }
        return result;
    }

    //좋아하는 것 작성하기
    @Transactional
    public void profileFavoriteRegister(final Long userId, final FavoriteDto favoriteDto){
        User user= userRepository.findById(userId).get();
        Profile profile= profileRepository.findByUser(user).get();
        final Favorite favorite= Favorite.builder()
                .favoriteContent(favoriteDto.getFavoriteContent())
                .profile(profile)
                .build();
        favoriteRepository.save(favorite);
        profile.updateCheckProfileUpload(true);
    }

    //좋아하는 것 삭제하기
    @Transactional
    public String profileFavoriteRemove(final Long userId, final Long favoriteId){
        User user= userRepository.findById(userId).get();
        Profile profile= profileRepository.findByUser(user).get();
        if(favoriteRepository.deleteByFavoriteIdAndProfile(favoriteId,profile).equals("1")) return "true";
        else return "false";
    }

    //싫어하는 것 목록 보여주기
    @Transactional
    public List<HateGetDto> profileHatesGet(final Long userId){
        User user= userRepository.findById(userId).get();
        List<Hate> hates=profileRepository.findByUser(user).get().getHates();
        final List<HateGetDto> result= new ArrayList<>();
        for(Hate f:hates){
            final HateGetDto hateGetDto= HateGetDto.builder()
                    .hateId(f.getHateId())
                    .hateContent(f.getHateContent())
                    .build();
            result.add(hateGetDto);
        }
        return result;
    }

    //싫어하는 것 작성하기
    @Transactional
    public void profileHateRegister(final Long userId, final HateDto hateDto){
        User user= userRepository.findById(userId).get();
        Profile profile= profileRepository.findByUser(user).get();
        final Hate hate= Hate.builder()
                .hateContent(hateDto.getHateContent())
                .profile(profile)
                .build();
        hateRepository.save(hate);
        profile.updateCheckProfileUpload(true);
    }

    //싫어하는 것 삭제하기
    @Transactional
    public String profileHateRemove(final Long userId, final Long hateId){
        User user= userRepository.findById(userId).get();
        Profile profile= profileRepository.findByUser(user).get();
        if(hateRepository.deleteByHateIdAndProfile(hateId,profile).equals("1")) return "true";
        else return "false";
    }

    //한단어로 표현하는 것 목록 보여주기
    @Transactional
    public List<ExpressionGetDto> profileExpressionsGet(final Long userId){
        User user= userRepository.findById(userId).get();
        List<Expression> expressions=profileRepository.findByUser(user).get().getExpressions();
        final List<ExpressionGetDto> result= new ArrayList<>();
        for(Expression e:expressions){
            final ExpressionGetDto expressionGetDto= ExpressionGetDto.builder()
                    .expressionId(e.getExpressionId())
                    .expressionContent(e.getExpressionContent())
                    .build();
            result.add(expressionGetDto);
        }
        return result;
    }

    //한단어로 표현하는 것 작성하기
    @Transactional
    public void profileExpressionRegister(final Long userId, final ExpressionDto expressionDto){
        User user= userRepository.findById(userId).get();
        Profile profile= profileRepository.findByUser(user).get();
        final Expression expression= Expression.builder()
                .expressionContent(expressionDto.getExpressionContent())
                .profile(profile)
                .build();
        expressionRepository.save(expression);
        profile.updateCheckProfileUpload(true);
    }

    //한단어로 표현하는 것 삭제하기
    @Transactional
    public String profileExpressionRemove(final Long userId, final Long expressionId){
        User user= userRepository.findById(userId).get();
        Profile profile= profileRepository.findByUser(user).get();
        if(expressionRepository.deleteByExpressionIdAndProfile(expressionId,profile).equals("1")) return "true";
        else return "false";
    }

    //관심사 목록 보여주기
    @Transactional
    public List<InterestGetDto> profileInterestsGet(final Long userId){
        User user= userRepository.findById(userId).get();
        List<Interest> interests=profileRepository.findByUser(user).get().getInterests();
        final List<InterestGetDto> result= new ArrayList<>();
        for(Interest i:interests){
            final InterestGetDto interestGetDto= InterestGetDto.builder()
                    .interestId(i.getInterestId())
                    .interestContent(i.getInterestContent())
                    .build();
            result.add(interestGetDto);
        }
        return result;
    }

    //관심사 작성하기
    @Transactional
    public void profileInterestRegister(final Long userId, final InterestDto interestDto){
        User user= userRepository.findById(userId).get();
        Profile profile= profileRepository.findByUser(user).get();
        final Interest interest= Interest.builder()
                .interestContent(interestDto.getInterestContent())
                .profile(profile)
                .build();
        interestRepository.save(interest);
        profile.updateCheckProfileUpload(true);
    }

    //관심사 삭제하기
    @Transactional
    public String profileInterestRemove(final Long userId, final Long interestId){
        User user= userRepository.findById(userId).get();
        Profile profile= profileRepository.findByUser(user).get();
        if(interestRepository.deleteByInterestIdAndProfile(interestId,profile).equals("1")) return "true";
        else return "false";
    }

    //유저 프로필(개인정보+상태메시지) 보여주기
    @Transactional
    public UserProfileGetDto userProfileGet(final Long userId){
        User user= userRepository.findById(userId).get();
        Profile profile= profileRepository.findByUser(user).get();
        return UserProfileGetDto.builder().user(user).profileImageUrl(profile.getProfileImageUrl())
                .checkProfileImage(profile.isCheckProfileImage()).message(profile.getMessage()).build();
    }

    //유저 개인정보(좋아하는것, 싫어하는것..등) 보여주기
    @Transactional
    public UserInformationsGetDto userInformationsGet(final Long userId){
        User user= userRepository.findById(userId).get();
        Profile profile= profileRepository.findByUser(user).get();
        List<Favorite> favorites= profile.getFavorites();
        final List<FavoriteGetDto> favoriteGetDtos= new ArrayList<>();
        for(Favorite f:favorites){
            final FavoriteGetDto favoriteGetDto= FavoriteGetDto.builder()
                    .favoriteId(f.getFavoriteId())
                    .favoriteContent(f.getFavoriteContent())
                    .build();
            favoriteGetDtos.add(favoriteGetDto);
        }
        List<Hate> hates= profile.getHates();
        final List<HateGetDto> hateGetDtos= new ArrayList<>();
        for(Hate h:hates){
            final HateGetDto hateGetDto= HateGetDto.builder()
                    .hateId(h.getHateId())
                    .hateContent(h.getHateContent())
                    .build();
            hateGetDtos.add(hateGetDto);
        }
        List<Expression> expressions= profile.getExpressions();
        final List<ExpressionGetDto> expressionGetDtos= new ArrayList<>();
        for(Expression e:expressions){
            final ExpressionGetDto expressionGetDto= ExpressionGetDto.builder()
                    .expressionId(e.getExpressionId())
                    .expressionContent(e.getExpressionContent())
                    .build();
            expressionGetDtos.add(expressionGetDto);
        }
        List<Interest> interests= profile.getInterests();
        final List<InterestGetDto> interestGetDtos= new ArrayList<>();
        for(Interest i:interests){
            final InterestGetDto interestGetDto= InterestGetDto.builder()
                    .interestId(i.getInterestId())
                    .interestContent(i.getInterestContent())
                    .build();
            interestGetDtos.add(interestGetDto);
        }
        return UserInformationsGetDto.builder().favorites(favoriteGetDtos).hates(hateGetDtos)
                .expressions(expressionGetDtos).interests(interestGetDtos).build();
    }

    @Transactional
    public boolean checkProfileUpload(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName()).get();
        Profile profile= profileRepository.findByUser(user).get();
        return profile.isCheckProfileUpload();
    }
}
