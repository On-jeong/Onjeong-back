package com.example.onjeong.mail.controller;


import com.example.onjeong.fcm.FCMService;
import com.example.onjeong.home.domain.CoinHistoryType;
import com.example.onjeong.home.service.CoinService;
import com.example.onjeong.mail.domain.Mail;
import com.example.onjeong.mail.dto.MailDto;
import com.example.onjeong.mail.dto.MailRequestDto;
import com.example.onjeong.mail.service.MailService;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags="Mail")
@RequiredArgsConstructor
@RestController
public class MailController {

    private final MailService mailService;
    private final CoinService coinService;
    private final FCMService fcmService;

    @ApiOperation(value = "메시지 전송")
    @PostMapping("/mails")
    public ResponseEntity<HttpStatus> sendMail(@RequestBody MailRequestDto mailSendDto) throws FirebaseMessagingException {
        Mail mail = mailService.sendMail(mailSendDto);
        fcmService.sendMail(mail);
        coinService.coinSave(CoinHistoryType.MAIL, 10);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ApiOperation(value = "받은 메일함 확인")
    @GetMapping("/mailList/receive")
    public ResponseEntity<List<MailDto>> showAllReceiveMail() {
        return ResponseEntity.ok(mailService.showAllReceiveMail());
    }

    @ApiOperation(value = "보낸 메일함 확인")
    @GetMapping("/mailList/send")
    public ResponseEntity<List<MailDto>> showAllSendMail() {
        return ResponseEntity.ok(mailService.showAllSendMail());
    }

    @ApiOperation(value = "특정 메일 확인")
    @GetMapping("/mails/{mailId}")
    public ResponseEntity<MailDto> showMail(@PathVariable("mailId") Long mailId) {
        return ResponseEntity.ok(mailService.showOneMail(mailId));
    }

    @ApiOperation(value = "보낸 메시지 삭제")
    @PostMapping("/mails/send/delete")
    public ResponseEntity<Boolean> deleteSendMail(@RequestParam() Long[] mailIds) {
        return ResponseEntity.ok(mailService.deleteSendMail(mailIds));
    }

    @ApiOperation(value = "받은 메시지 삭제")
    @PostMapping("/mails/receive/delete")
    public ResponseEntity<Boolean> deleteReceiveMail(@RequestParam() Long[] mailIds) {
        return ResponseEntity.ok(mailService.deleteReceiveMail(mailIds));
    }

}
