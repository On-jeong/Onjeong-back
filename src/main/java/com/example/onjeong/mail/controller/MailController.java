package com.example.onjeong.mail.controller;


import com.example.onjeong.mail.dto.MailDto;
import com.example.onjeong.mail.dto.MailRequestDto;
import com.example.onjeong.mail.service.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags="Mail")
@RequiredArgsConstructor
@RestController
public class MailController {

    private final MailService mailService;

    @ApiOperation(value = "메시지 전송")
    @PostMapping("/mails")
    public ResponseEntity<Boolean> sendMail(@RequestBody MailRequestDto mailSendDto) {
        mailService.sendMail(mailSendDto);
        return ResponseEntity.ok(true);
    }

    @ApiOperation(value = "받은 메일함 확인")
    @GetMapping("/mailList/send/")
    public ResponseEntity<List<MailDto>> showAllReceiveMail(@PathVariable Long userId) {
        return ResponseEntity.ok(mailService.showAllReceiveMail(userId));
    }

    @ApiOperation(value = "보낸 메일함 확인")
    @GetMapping("/mailList/receive")
    public ResponseEntity<List<MailDto>> showAllSendMail(@PathVariable Long userId) {
        return ResponseEntity.ok(mailService.showAllSendMail(userId));
    }

    @ApiOperation(value = "특정 메일 확인")
    @GetMapping("/mails/")
    public ResponseEntity<MailDto> showMail(@PathVariable Long mailId) {
        return ResponseEntity.ok(mailService.showOneMail(mailId));
    }

    @ApiOperation(value = "보낸 메시지 삭제")
    @PostMapping("/mails/send")
    public ResponseEntity<Boolean> deleteSendMail(@PathVariable Long mailId) {
        return ResponseEntity.ok(mailService.deleteSendMail(mailId));
    }

    @ApiOperation(value = "받은 메시지 삭제")
    @PostMapping("/mails/receive")
    public ResponseEntity<Boolean> deleteReceiveMail(@PathVariable Long mailId) {
        return ResponseEntity.ok(mailService.deleteReceiveMail(mailId));
    }

}
