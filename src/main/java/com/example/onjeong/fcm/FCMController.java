package com.example.onjeong.fcm;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.fcm.dto.DeviceTokenRequest;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.dto.UserDto;
import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.user.service.UserService;
import com.google.api.Http;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Api(tags="FCM")
@RequiredArgsConstructor
@RestController
public class FCMController {

    private final UserService userService;
    private final FCMService fcmService;
    private final UserRepository userRepository;

    @ApiOperation(value="로그인 시 FCM 토큰 저장")
    @PostMapping(value = "/token/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> FCMRegister(@RequestParam String token) throws FirebaseMessagingException {
        fcmService.registerToken(token);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ApiOperation(value="로그아웃 시 FCM 토큰 해제")
    @PostMapping(value = "/token/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> FCMCancel() throws FirebaseMessagingException {
        fcmService.deleteToken();
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
