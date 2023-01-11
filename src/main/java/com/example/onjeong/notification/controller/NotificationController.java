package com.example.onjeong.notification.controller;

import com.example.onjeong.notification.service.NotificationService;
import com.example.onjeong.result.ResultCode;
import com.example.onjeong.result.ResultResponse;
import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.user.service.UserService;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags="FCM")
@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final UserService userService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @ApiOperation(value="FCM 토큰 저장")
    @PostMapping(value = "/token/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> FCMRegister(@RequestParam String token) throws FirebaseMessagingException {
        notificationService.registerToken(token);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.POST_TOKEN_SUCCESS));
    }

    @ApiOperation(value="FCM 토큰 해제")
    @PostMapping(value = "/token/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> FCMCancel() throws FirebaseMessagingException {
        notificationService.deleteToken();
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_TOKEN_SUCCESS));
    }

    @ApiOperation(value="알림 목록 조회")
    @GetMapping(value = "/notification", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> notifications() {
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_ALARM_SUCCESS, notificationService.getNotifications()));
    }
}
