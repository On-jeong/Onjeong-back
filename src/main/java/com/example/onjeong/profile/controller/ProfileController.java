package com.example.onjeong.profile.controller;

import com.example.onjeong.profile.dto.*;
import com.example.onjeong.profile.service.ProfileService;
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
    public ResponseEntity<String> profileImageRegister(@RequestPart(value = "images", required = false) MultipartFile multipartFile) {
        return ResponseEntity.ok(profileService.profileImageRegister(multipartFile));
    }

    @ApiOperation(value="프로필 사진 수정하기")
    @PutMapping(value = "/profiles/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> profileImageModify(@RequestPart(value = "images", required = false) MultipartFile multipartFile) {
        profileService.profileImageDelete();
        profileService.profileImageRegister(multipartFile);
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
    public ResponseEntity<String> profileMessageRegister(@RequestBody ProfileMessageDto profileMessageDto) {
        String result= profileService.profileMessageRegister(profileMessageDto);
        return ResponseEntity.ok(result);
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
    public ResponseEntity<String> profileFavoriteRegister(@PathVariable("userId") Long userId, @RequestBody FavoriteDto favoriteDto) {
        String result= profileService.profileFavoriteRegister(userId, favoriteDto);
        return ResponseEntity.ok(result);
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
    public ResponseEntity<String> profileHateRegister(@PathVariable("userId") Long userId, @RequestBody HateDto hateDto) {
        String result= profileService.profileHateRegister(userId, hateDto);
        return ResponseEntity.ok(result);
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
    public ResponseEntity<String> profileExpressionRegister(@PathVariable("userId") Long userId, @RequestBody ExpressionDto expressionDto) {
        String result= profileService.profileExpressionRegister(userId, expressionDto);
        return ResponseEntity.ok(result);
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
    public ResponseEntity<String> profileInterestRegister(@PathVariable("userId") Long userId, @RequestBody InterestDto interestDto) {
        String result= profileService.profileInterestRegister(userId, interestDto);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="관심사 삭제하기")
    @DeleteMapping(value = "/profiles/interests/{userId}/{interestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> profileInterestRemove(@PathVariable("userId") Long userId, @PathVariable("interestId") Long interestId) {
        String result= profileService.profileInterestRemove(userId, interestId);
        return ResponseEntity.ok(result);
    }
}
