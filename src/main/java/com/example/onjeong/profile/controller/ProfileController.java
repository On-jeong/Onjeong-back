package com.example.onjeong.profile.controller;

import com.example.onjeong.fcm.FCMService;
import com.example.onjeong.home.domain.CoinHistoryType;
import com.example.onjeong.home.service.CoinService;
import com.example.onjeong.profile.dto.*;
import com.example.onjeong.profile.service.ProfileService;
import com.example.onjeong.result.ResultCode;
import com.example.onjeong.result.ResultResponse;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags="Profile")
@RequiredArgsConstructor
@RestController
@Log4j2
public class ProfileController {
    private final ProfileService profileService;
    private final CoinService coinService;
    private final FCMService fcmService;

    @ApiOperation(value="가족 프로필 중 구성원 보여주기")
    @GetMapping(value = "/families", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> allUserGet(){
        List<FamilyGetDto> data= profileService.allUserGet();
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_ALL_USER_SUCCESS,data));
    }

    @ApiOperation(value="프로필 상단에 개인 정보 보여주기")
    @GetMapping(value = "/profiles/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> userInformationGet(@PathVariable("userId") Long userId) {
        UserInformationDto data= profileService.userInformationGet(userId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_USER_INFORMATION_SUCCESS,data));
    }

    @ApiOperation(value="프로필 사진 등록하기")
    @PostMapping(value = "/profiles/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> profileImageRegister(@RequestPart(value = "images") MultipartFile multipartFile) throws FirebaseMessagingException{
        if(profileService.checkProfileUpload()) fcmService.sendProfileModify(profileService.profileImageRegister(multipartFile));
        else{
            fcmService.sendProfileModify(profileService.profileImageRegister(multipartFile));
            coinService.coinSave(CoinHistoryType.PROFILE, 100);
        }
        return ResponseEntity.ok(ResultResponse.of(ResultCode.REGISTER_PROFILE_IMAGE_SUCCESS));
    }

    @ApiOperation(value="프로필 사진 삭제하기")
    @DeleteMapping(value = "/profiles/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> profileImageDelete() {
        profileService.profileImageDelete();
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_PROFILE_IMAGE_SUCCESS));
    }


    @ApiOperation(value="상태메시지 보여주기")
    @GetMapping(value = "/profiles/messages/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> profileMessageGet(@PathVariable("userId") Long userId) {
        ProfileMessageDto data= profileService.profileMessageGet(userId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_PROFILE_MESSAGE_SUCCESS,data));
    }

    @ApiOperation(value="상태메시지 작성하기")
    @PostMapping(value = "/profiles/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> profileMessageRegister(@RequestBody ProfileMessageDto profileMessageDto) throws FirebaseMessagingException{
        if(profileService.checkProfileUpload()) fcmService.sendProfileModify(profileService.profileMessageRegister(profileMessageDto));
        else{
            fcmService.sendProfileModify(profileService.profileMessageRegister(profileMessageDto));
            coinService.coinSave(CoinHistoryType.PROFILE, 100);
        }
        return ResponseEntity.ok(ResultResponse.of(ResultCode.REGISTER_PROFILE_MESSAGE_SUCCESS));
    }

    @ApiOperation(value="상태메시지 수정하기")
    @PatchMapping(value = "/profiles/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> profileMessageModify(@RequestBody ProfileMessageDto profileMessageDto) {
        profileService.profileMessageModify(profileMessageDto);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.MODIFY_PROFILE_MESSAGE_SUCCESS));
    }


    @ApiOperation(value="좋아하는 것 작성하기")
    @PostMapping(value = "/profiles/favorites/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> profileFavoriteRegister(@PathVariable("userId") Long userId
            , @RequestBody SelfIntroductionAnswerRegisterDto selfIntroductionAnswerRegisterDto) throws FirebaseMessagingException{
        if(profileService.checkProfileUpload()) fcmService.sendProfileModify(profileService.registerSelfIntroductionAnswer(userId, selfIntroductionAnswerRegisterDto, "favorite"));
        else{
            fcmService.sendProfileModify(profileService.registerSelfIntroductionAnswer(userId, selfIntroductionAnswerRegisterDto, "favorite"));
            coinService.coinSave(CoinHistoryType.PROFILE, 100);
        }
        return ResponseEntity.ok(ResultResponse.of(ResultCode.REGISTER_FAVORITE_SUCCESS));
    }

    @ApiOperation(value="좋아하는 것 삭제하기")
    @DeleteMapping(value = "/profiles/favorites/{userId}/{selfIntroductionAnswerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> profileFavoriteRemove(@PathVariable("userId") Long userId, @PathVariable("selfIntroductionAnswerId") Long selfIntroductionAnswerId) {
        profileService.removeSelfIntroductionAnswer(userId, selfIntroductionAnswerId, "favorite");
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_FAVORITE_SUCCESS));
    }

    @ApiOperation(value="싫어하는 것 작성하기")
    @PostMapping(value = "/profiles/hates/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> profileHateRegister(@PathVariable("userId") Long userId, @RequestBody SelfIntroductionAnswerRegisterDto selfIntroductionAnswerRegisterDto) throws FirebaseMessagingException{
        if(profileService.checkProfileUpload()) fcmService.sendProfileModify(profileService.registerSelfIntroductionAnswer(userId, selfIntroductionAnswerRegisterDto, "hate"));
        else{
            fcmService.sendProfileModify(profileService.registerSelfIntroductionAnswer(userId, selfIntroductionAnswerRegisterDto, "hate"));
            coinService.coinSave(CoinHistoryType.PROFILE, 100);
        }
        return ResponseEntity.ok(ResultResponse.of(ResultCode.REGISTER_HATE_SUCCESS));
    }

    @ApiOperation(value="싫어하는 것 삭제하기")
    @DeleteMapping(value = "/profiles/hates/{userId}/{selfIntroductionAnswerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> profileHateRemove(@PathVariable("userId") Long userId, @PathVariable("selfIntroductionAnswerId") Long selfIntroductionAnswerId) {
        profileService.removeSelfIntroductionAnswer(userId, selfIntroductionAnswerId, "hate");
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_HATE_SUCCESS));
    }

    @ApiOperation(value="한단어로 표현하는 것 작성하기")
    @PostMapping(value = "/profiles/expressions/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> profileExpressionRegister(@PathVariable("userId") Long userId, @RequestBody SelfIntroductionAnswerRegisterDto selfIntroductionAnswerRegisterDto) throws FirebaseMessagingException{
        if(profileService.checkProfileUpload()) fcmService.sendProfileModify(profileService.registerSelfIntroductionAnswer(userId, selfIntroductionAnswerRegisterDto, "expression"));
        else{
            fcmService.sendProfileModify(profileService.registerSelfIntroductionAnswer(userId, selfIntroductionAnswerRegisterDto, "expression"));
            coinService.coinSave(CoinHistoryType.PROFILE, 100);
        }
        return ResponseEntity.ok(ResultResponse.of(ResultCode.REGISTER_EXPRESSION_SUCCESS));
    }

    @ApiOperation(value="한단어로 표현하는 것 삭제하기")
    @DeleteMapping(value = "/profiles/expressions/{userId}/{selfIntroductionAnswerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> profileExpressionRemove(@PathVariable("userId") Long userId, @PathVariable("selfIntroductionAnswerId") Long selfIntroductionAnswerId) {
        profileService.removeSelfIntroductionAnswer(userId, selfIntroductionAnswerId, "expression");
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_EXPRESSION_SUCCESS));
    }

    @ApiOperation(value="관심사 작성하기")
    @PostMapping(value = "/profiles/interests/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> profileInterestRegister(@PathVariable("userId") Long userId, @RequestBody SelfIntroductionAnswerRegisterDto selfIntroductionAnswerRegisterDto) throws FirebaseMessagingException{
        if(profileService.checkProfileUpload()) fcmService.sendProfileModify(profileService.registerSelfIntroductionAnswer(userId, selfIntroductionAnswerRegisterDto, "interest"));
        else{
            fcmService.sendProfileModify(profileService.registerSelfIntroductionAnswer(userId, selfIntroductionAnswerRegisterDto, "interest"));
            coinService.coinSave(CoinHistoryType.PROFILE, 100);
        }
        return ResponseEntity.ok(ResultResponse.of(ResultCode.REGISTER_INTEREST_SUCCESS));
    }

    @ApiOperation(value="관심사 삭제하기")
    @DeleteMapping(value = "/profiles/interests/{userId}/{selfIntroductionAnswerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> profileInterestRemove(@PathVariable("userId") Long userId, @PathVariable("selfIntroductionAnswerId") Long selfIntroductionAnswerId) {
        profileService.removeSelfIntroductionAnswer(userId, selfIntroductionAnswerId, "interest");
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_INTEREST_SUCCESS));
    }

    @ApiOperation(value="유저 프로필(개인정보+상태메시지) 보여주기")
    @GetMapping(value = "/profiles/{userId}/user-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> userProfileGet(@PathVariable("userId") Long userId) {
        UserProfileGetDto data= profileService.userProfileGet(userId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_PROFILE_SUCCESS,data));
    }

    @ApiOperation(value="유저 개인정보(좋아하는것, 싫어하는것..등) 리스트 보여주기")
    @GetMapping(value = "/profiles/{userId}/self-introduction", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> getSelfIntroductionAnswer(@PathVariable("userId") Long userId) {
        SelfIntroductionAnswerListGetDto data= profileService.getSelfIntroductionAnswer(userId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_INFORMATIONS_SUCCESS,data));
    }
}
