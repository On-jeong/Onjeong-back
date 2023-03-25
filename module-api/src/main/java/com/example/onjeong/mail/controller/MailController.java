package com.example.onjeong.mail.controller;


import com.example.onjeong.coin.domain.CoinHistoryType;
import com.example.onjeong.coin.service.CoinService;
import com.example.onjeong.mail.domain.Mail;
import com.example.onjeong.mail.dto.MailRequestDto;
import com.example.onjeong.mail.service.MailService;
import com.example.onjeong.notification.service.NotificationService;
import com.example.onjeong.result.ResultCode;
import com.example.onjeong.result.ResultResponse;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags="Mail")
@RequiredArgsConstructor
@RestController
public class MailController {

    private final MailService mailService;
    private final CoinService coinService;
    private final NotificationService notificationService;

    @ApiOperation(value = "메시지 전송")
    @PostMapping("/mails")
    public ResponseEntity<ResultResponse> sendMail(@RequestBody MailRequestDto mailSendDto) throws FirebaseMessagingException {
        Mail mail = mailService.sendMail(mailSendDto);
        notificationService.sendMail(mail);
        coinService.coinSave(CoinHistoryType.MAIL, 10);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.POST_MAIL_SUCCESS));
    }

    @ApiOperation(value = "받은 메일함 확인")
    @GetMapping("/mailList/receive")
    public ResponseEntity<ResultResponse> showAllReceiveMail(@PageableDefault(size=20, sort = "mail_id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_RECEIVE_MAIL_SUCCESS, mailService.showAllReceiveMail(pageable)));
    }

    @ApiOperation(value = "보낸 메일함 확인")
    @GetMapping("/mailList/send")
    public ResponseEntity<ResultResponse> showAllSendMail(@PageableDefault(size=20, sort = "mail_id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_SEND_MAIL_SUCCESS, mailService.showAllSendMail(pageable)));
    }

    @ApiOperation(value = "특정 메일 읽음 처리")
    @GetMapping("/mails/{mailId}")
    public ResponseEntity<ResultResponse> showMail(@PathVariable("mailId") Long mailId) {
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_ONE_MAIL_SUCCESS, mailService.showOneMail(mailId)));
    }

    @ApiOperation(value = "보낸 메시지 삭제")
    @PostMapping("/mails/send/delete")
    public ResponseEntity<ResultResponse> deleteSendMail(@RequestParam() Long[] mailIds) {
        mailService.deleteSendMail(mailIds);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_SEND_MAIL_SUCCESS));
    }

    @ApiOperation(value = "받은 메시지 삭제")
    @PostMapping("/mails/receive/delete")
    public ResponseEntity<ResultResponse> deleteReceiveMail(@RequestParam() Long[] mailIds) {
        mailService.deleteReceiveMail(mailIds);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_RECEIVE_MAIL_SUCCESS));
    }

}
