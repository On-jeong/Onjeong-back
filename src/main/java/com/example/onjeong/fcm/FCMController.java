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
    private final UserRepository userRepository;

    @ApiOperation(value="로그인 시 FCM 토큰 저장")
    @PostMapping(value = "/token/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> FCMRegister(@RequestBody DeviceTokenRequest deviceTokenRequest) throws FirebaseMessagingException {

        User user = userService.findUser(deviceTokenRequest.getUserNickname());
        user.updateDeviceToken(deviceTokenRequest.getToken());

        Family family = user.getFamily();
        String topic = family.getFamilyId().toString();

        List<String> registrationTokens = new ArrayList<>();
        registrationTokens.add(topic);

        TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(
                registrationTokens, topic);

        System.out.println(response.getSuccessCount() + " tokens were subscribed successfully");

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ApiOperation(value="로그아웃 시 FCM 토큰 해제")
    @PostMapping(value = "/token/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> FCMCancel(@RequestBody DeviceTokenRequest deviceTokenRequest) throws FirebaseMessagingException {

        User user = userService.findUser(deviceTokenRequest.getUserNickname());
        List<String> registrationTokens = new ArrayList<>();
        registrationTokens.add(deviceTokenRequest.getToken());

        Family family = user.getFamily();
        String topic = family.getFamilyId().toString();

        TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(
                registrationTokens, topic);
        System.out.println(response.getSuccessCount() + " tokens were unsubscribed successfully");

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
