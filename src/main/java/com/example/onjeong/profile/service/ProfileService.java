package com.example.onjeong.profile.service;

import com.amazonaws.services.s3.AmazonS3;
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

    @Value("https://onjeong-bucket.s3.ap-northeast-2.amazonaws.com/")
    private String AWS_S3_BUCKET_URL;


    //가족 프로필 중 구성원 보여주기
    @Transactional
    public List<FamilyGetDto> allUserGet(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user= userRepository.findByUserNickname(authentication.getName());
        List<User> users= user.get().getFamily().getUsers();
        List<FamilyGetDto> result= new ArrayList<>();
        for(User u:users){
            FamilyGetDto familyGetDto= FamilyGetDto.builder()
                    .userId(u.getUserId())
                    .userStatus(u.getUserStatus())
                    .build();
            result.add(familyGetDto);
        }
        return result;
    }

    //프로필 상단에 개인 정보 보여주기
    @Transactional
    public UserInformationDto userInformationGet(Long userId){
        User user= userRepository.findById(userId).get();
        Profile profile= profileRepository.findByUser(user).get();
        return UserInformationDto.builder().user(user).profileImageUrl(profile.getProfileImageUrl()).checkProfileImage(profile.isCheckProfileImage()).build();
    }

    //프로필 사진 등록하기
    @Transactional
    public String profileImageRegister(MultipartFile multipartFile){
        try {
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            User user= userRepository.findByUserNickname(authentication.getName()).get();
            Profile profile= profileRepository.findByUser(user).get();
            String imageUrl= s3Uploader.upload(multipartFile, "profile");
            profile.setProfileImageUrl(imageUrl);
            profile.setCheckProfileImage(true);
            return imageUrl;
        } catch (IOException e) {
            e.printStackTrace();
            return "profile image upload fail";
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

        profile.setProfileImageUrl("");
        profile.setCheckProfileImage(false);
        return "success";
    }

    //상태메시지 보여주기
    @Transactional
    public ProfileMessageDto profileMessageGet(Long userId){
        User user= userRepository.findById(userId).get();
        Profile profile= profileRepository.findByUser(user).get();
        return ProfileMessageDto.builder()
                .message(profile.getMessage())
                .build();
    }

    //상태메시지 작성하기
    @Transactional
    public String profileMessageRegister(ProfileMessageDto profileMessageDto){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName()).get();
        Profile profile= profileRepository.findByUser(user).get();
        profile.setMessage(profileMessageDto.getMessage());
        return profile.getMessage();
    }

    //상태메시지 수정하기
    @Transactional
    public String profileMessageModify(ProfileMessageDto profileMessageDto){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName()).get();
        Profile profile= profileRepository.findByUser(user).get();
        profile.setMessage(profileMessageDto.getMessage());
        return profile.getMessage();
    }

    //좋아하는 것 목록 보여주기
    @Transactional
    public List<FavoriteGetDto> profileFavoritesGet(Long userId){
        User user= userRepository.findById(userId).get();
        List<Favorite> favorites=profileRepository.findByUser(user).get().getFavorites();
        List<FavoriteGetDto> result= new ArrayList<>();
        for(Favorite f:favorites){
            FavoriteGetDto favoriteGetDto= FavoriteGetDto.builder()
                    .favoriteId(f.getFavoriteId())
                    .favoriteContent(f.getFavoriteContent())
                    .build();
            result.add(favoriteGetDto);
        }
        return result;
    }

    //좋아하는 것 작성하기
    @Transactional
    public String profileFavoriteRegister(Long userId, FavoriteDto favoriteDto){
        User user= userRepository.findById(userId).get();
        final Favorite favorite= Favorite.builder()
                .favoriteContent(favoriteDto.getFavoriteContent())
                .profile(profileRepository.findByUser(user).get())
                .build();
        favoriteRepository.save(favorite);
        return "true";
    }

    //좋아하는 것 삭제하기
    @Transactional
    public String profileFavoriteRemove(Long userId, Long favoriteId){
        User user= userRepository.findById(userId).get();
        Profile profile= profileRepository.findByUser(user).get();
        if(favoriteRepository.deleteByFavoriteIdAndProfile(favoriteId,profile).equals("1")) return "true";
        else return "false";
    }

    //싫어하는 것 목록 보여주기
    @Transactional
    public List<HateGetDto> profileHatesGet(Long userId){
        User user= userRepository.findById(userId).get();
        List<Hate> hates=profileRepository.findByUser(user).get().getHates();
        List<HateGetDto> result= new ArrayList<>();
        for(Hate f:hates){
            HateGetDto hateGetDto= HateGetDto.builder()
                    .hateId(f.getHateId())
                    .hateContent(f.getHateContent())
                    .build();
            result.add(hateGetDto);
        }
        return result;
    }

    //싫어하는 것 작성하기
    @Transactional
    public String profileHateRegister(Long userId, HateDto hateDto){
        User user= userRepository.findById(userId).get();
        final Hate hate= Hate.builder()
                .hateContent(hateDto.getHateContent())
                .profile(profileRepository.findByUser(user).get())
                .build();
        hateRepository.save(hate);
        return "true";
    }

    //싫어하는 것 삭제하기
    @Transactional
    public String profileHateRemove(Long userId, Long hateId){
        User user= userRepository.findById(userId).get();
        Profile profile= profileRepository.findByUser(user).get();
        if(hateRepository.deleteByHateIdAndProfile(hateId,profile).equals("1")) return "true";
        else return "false";
    }

    //한단어로 표현하는 것 목록 보여주기
    @Transactional
    public List<ExpressionGetDto> profileExpressionsGet(Long userId){
        User user= userRepository.findById(userId).get();
        List<Expression> expressions=profileRepository.findByUser(user).get().getExpressions();
        List<ExpressionGetDto> result= new ArrayList<>();
        for(Expression e:expressions){
            ExpressionGetDto expressionGetDto= ExpressionGetDto.builder()
                    .expressionId(e.getExpressionId())
                    .expressionContent(e.getExpressionContent())
                    .build();
            result.add(expressionGetDto);
        }
        return result;
    }

    //한단어로 표현하는 것 작성하기
    @Transactional
    public String profileExpressionRegister(Long userId, ExpressionDto expressionDto){
        User user= userRepository.findById(userId).get();
        final Expression expression= Expression.builder()
                .expressionContent(expressionDto.getExpressionContent())
                .profile(profileRepository.findByUser(user).get())
                .build();
        expressionRepository.save(expression);
        return "true";
    }

    //한단어로 표현하는 것 삭제하기
    @Transactional
    public String profileExpressionRemove(Long userId, Long expressionId){
        User user= userRepository.findById(userId).get();
        Profile profile= profileRepository.findByUser(user).get();
        if(expressionRepository.deleteByExpressionIdAndProfile(expressionId,profile).equals("1")) return "true";
        else return "false";
    }

    //관심사 목록 보여주기
    @Transactional
    public List<InterestGetDto> profileInterestsGet(Long userId){
        User user= userRepository.findById(userId).get();
        List<Interest> interests=profileRepository.findByUser(user).get().getInterests();
        List<InterestGetDto> result= new ArrayList<>();
        for(Interest i:interests){
            InterestGetDto interestGetDto= InterestGetDto.builder()
                    .interestId(i.getInterestId())
                    .interestContent(i.getInterestContent())
                    .build();
            result.add(interestGetDto);
        }
        return result;
    }

    //관심사 작성하기
    @Transactional
    public String profileInterestRegister(Long userId, InterestDto interestDto){
        User user= userRepository.findById(userId).get();
        final Interest interest= Interest.builder()
                .interestContent(interestDto.getInterestContent())
                .profile(profileRepository.findByUser(user).get())
                .build();
        interestRepository.save(interest);
        return "true";
    }

    //관심사 삭제하기
    @Transactional
    public String profileInterestRemove(Long userId, Long interestId){
        User user= userRepository.findById(userId).get();
        Profile profile= profileRepository.findByUser(user).get();
        if(interestRepository.deleteByInterestIdAndProfile(interestId,profile).equals("1")) return "true";
        else return "false";
    }

}
