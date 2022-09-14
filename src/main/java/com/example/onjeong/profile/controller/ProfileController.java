package com.example.onjeong.profile.controller;

import com.example.onjeong.fcm.FCMService;
import com.example.onjeong.home.domain.CoinHistoryType;
import com.example.onjeong.home.service.CoinService;
import com.example.onjeong.profile.domain.Profile;
import com.example.onjeong.profile.dto.*;
import com.example.onjeong.profile.service.ProfileService;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<List<FamilyGetDto>> allUserGet(){
        List<FamilyGetDto> result= profileService.allUserGet();
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="프로필 상단에 개인 정보 보여주기")
    @GetMapping(value = "/profiles/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInformationDto> userInformationGet(@PathVariable("userId") Long userId) {
        UserInformationDto result= profileService.userInformationGet(userId);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="프로필 사진 등록하기")
    @PostMapping(value = "/profiles/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> profileImageRegister(@RequestPart(value = "images", required = false) MultipartFile multipartFile) throws FirebaseMessagingException, IOException {
        if(profileService.checkProfileUpload()) fcmService.sendProfileModify(profileService.profileImageRegister(multipartFile));
        else{
            fcmService.sendProfileModify(profileService.profileImageRegister(multipartFile));
            coinService.coinSave(CoinHistoryType.PROFILE, 100);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ApiOperation(value="프로필 사진 수정하기")
    @PutMapping(value = "/profiles/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> profileImageModify(@RequestPart(value = "images", required = false) MultipartFile multipartFile) throws FirebaseMessagingException, IOException {
        profileService.profileImageDelete();
        Profile profile= profileService.profileImageRegister(multipartFile);
        fcmService.sendProfileModify(profile);
        return ResponseEntity.ok("success");
    }

    @ApiOperation(value="프로필 사진 삭제하기")
    @DeleteMapping(value = "/profiles/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> profileImageDelete() {
        return ResponseEntity.ok(profileService.profileImageDelete());
    }


    @ApiOperation(value="상태메시지 보여주기")
    @GetMapping(value = "/profiles/messages/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileMessageDto> profileMessageGet(@PathVariable("userId") Long userId) {
        ProfileMessageDto result= profileService.profileMessageGet(userId);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="상태메시지 작성하기")
    @PostMapping(value = "/profiles/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> profileMessageRegister(@RequestBody ProfileMessageDto profileMessageDto) throws FirebaseMessagingException{
        if(profileService.checkProfileUpload()) fcmService.sendProfileModify(profileService.profileMessageRegister(profileMessageDto));
        else{
            fcmService.sendProfileModify(profileService.profileMessageRegister(profileMessageDto));
            coinService.coinSave(CoinHistoryType.PROFILE, 100);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ApiOperation(value="상태메시지 수정하기")
    @PatchMapping(value = "/profiles/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> profileMessageModify(@RequestBody ProfileMessageDto profileMessageDto) {
        String result= profileService.profileMessageModify(profileMessageDto);
        return ResponseEntity.ok(result);
    }


    @ApiOperation(value="좋아하는 것 목록 보여주기")
    @GetMapping(value = "/profiles/favorites/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FavoriteGetDto>> profileFavoritesGet(@PathVariable("userId") Long userId) {
        List<FavoriteGetDto> result= profileService.profileFavoritesGet(userId);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="좋아하는 것 작성하기")
    @PostMapping(value = "/profiles/favorites/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> profileFavoriteRegister(@PathVariable("userId") Long userId, @RequestBody FavoriteDto favoriteDto) throws FirebaseMessagingException{
        if(profileService.checkProfileUpload()) fcmService.sendProfileModify(profileService.profileFavoriteRegister(userId, favoriteDto));
        else{
            fcmService.sendProfileModify(profileService.profileFavoriteRegister(userId, favoriteDto));
            coinService.coinSave(CoinHistoryType.PROFILE, 100);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ApiOperation(value="좋아하는 것 삭제하기")
    @DeleteMapping(value = "/profiles/favorites/{userId}/{favoriteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> profileFavoriteRemove(@PathVariable("userId") Long userId, @PathVariable("favoriteId") Long favoriteId) {
        String result= profileService.profileFavoriteRemove(userId, favoriteId);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="싫어하는 것 목록 보여주기")
    @GetMapping(value = "/profiles/hates/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HateGetDto>> profileHatesGet(@PathVariable("userId") Long userId) {
        List<HateGetDto> result= profileService.profileHatesGet(userId);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="싫어하는 것 작성하기")
    @PostMapping(value = "/profiles/hates/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> profileHateRegister(@PathVariable("userId") Long userId, @RequestBody HateDto hateDto) throws FirebaseMessagingException{
        if(profileService.checkProfileUpload()) fcmService.sendProfileModify(profileService.profileHateRegister(userId, hateDto));
        else{
            fcmService.sendProfileModify(profileService.profileHateRegister(userId, hateDto));
            coinService.coinSave(CoinHistoryType.PROFILE, 100);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ApiOperation(value="싫어하는 것 삭제하기")
    @DeleteMapping(value = "/profiles/hates/{userId}/{hateId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> profileHateRemove(@PathVariable("userId") Long userId, @PathVariable("hateId") Long hateId) {
        String result= profileService.profileHateRemove(userId, hateId);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="한단어로 표현하는 것 목록 보여주기")
    @GetMapping(value = "/profiles/expressions/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ExpressionGetDto>> profileExpressionsGet(@PathVariable("userId") Long userId) {
        List<ExpressionGetDto> result= profileService.profileExpressionsGet(userId);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="한단어로 표현하는 것 작성하기")
    @PostMapping(value = "/profiles/expressions/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> profileExpressionRegister(@PathVariable("userId") Long userId, @RequestBody ExpressionDto expressionDto) throws FirebaseMessagingException{
        if(profileService.checkProfileUpload()) fcmService.sendProfileModify(profileService.profileExpressionRegister(userId, expressionDto));
        else{
            fcmService.sendProfileModify(profileService.profileExpressionRegister(userId, expressionDto));
            coinService.coinSave(CoinHistoryType.PROFILE, 100);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ApiOperation(value="한단어로 표현하는 것 삭제하기")
    @DeleteMapping(value = "/profiles/expressions/{userId}/{expressionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> profileExpressionRemove(@PathVariable("userId") Long userId, @PathVariable("expressionId") Long expressionId) {
        String result= profileService.profileExpressionRemove(userId, expressionId);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="관심사 목록 보여주기")
    @GetMapping(value = "/profiles/interests/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InterestGetDto>> profileInterestsGet(@PathVariable("userId") Long userId) {
        List<InterestGetDto> result= profileService.profileInterestsGet(userId);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="관심사 작성하기")
    @PostMapping(value = "/profiles/interests/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> profileInterestRegister(@PathVariable("userId") Long userId, @RequestBody InterestDto interestDto) throws FirebaseMessagingException{
        if(profileService.checkProfileUpload()) fcmService.sendProfileModify(profileService.profileInterestRegister(userId, interestDto));
        else{
            fcmService.sendProfileModify(profileService.profileInterestRegister(userId, interestDto));
            coinService.coinSave(CoinHistoryType.PROFILE, 100);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ApiOperation(value="관심사 삭제하기")
    @DeleteMapping(value = "/profiles/interests/{userId}/{interestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> profileInterestRemove(@PathVariable("userId") Long userId, @PathVariable("interestId") Long interestId) {
        String result= profileService.profileInterestRemove(userId, interestId);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="유저 프로필(개인정보+상태메시지) 보여주기")
    @GetMapping(value = "/profiles/{userId}/user-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileGetDto> userProfileGet(@PathVariable("userId") Long userId) {
        UserProfileGetDto result= profileService.userProfileGet(userId);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="유저 개인정보(좋아하는것, 싫어하는것..등) 보여주기")
    @GetMapping(value = "/profiles/{userId}/user-informations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInformationsGetDto> userInformationsGet(@PathVariable("userId") Long userId) {
        UserInformationsGetDto result= profileService.userInformationsGet(userId);
        return ResponseEntity.ok(result);
    }
}
