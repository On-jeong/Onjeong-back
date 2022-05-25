package com.example.onjeong.service;

import com.example.onjeong.domain.Mail;
import com.example.onjeong.domain.User;
import com.example.onjeong.dto.MailDto;
import com.example.onjeong.dto.MailRequestDto;
import com.example.onjeong.repository.MailRepository;
import com.example.onjeong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MailService {

    private final UserRepository userRepository;
    private final MailRepository mailRepository;

    // 메시지 전송
    @Transactional
    public Mail sendMail(MailRequestDto mailSendDto){
        User sendUser = userRepository.findById(mailSendDto.getSendUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        User ReceiveUser = userRepository.findById(mailSendDto.getReceiveUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Mail mail = Mail.builder()
                .mailContent(mailSendDto.getMailContent())
                .sendUser(sendUser)
                .receiveUser(ReceiveUser)
                .checkRead(false)
                .receiverWantDelete(false)
                .senderWantDelete(false)
                .sendTime(LocalDateTime.now())
                .build();

        return mailRepository.save(mail);
    }

    // 받은 메일함 확인
    public List<MailDto> showAllReceiveMail(Long userId){
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<Mail> mailList = mailRepository.findByReceiverId(user);
        List<MailDto> mailDtoList = new ArrayList<>();

        for(Mail m : mailList){
            mailDtoList.add(MailDto.builder()
                    .mailContent(m.getMailContent())
                    .sendUserName(m.getSendUser().getUserNickname())
                    .receiveUserName(m.getReceiveUser().getUserNickname())
                    .checkRead(m.isCheckRead())
                    .sendTime(m.getSendTime())
                    .build());
        }
        return mailDtoList;
    }

    // 보낸 메일함 확인
    public List<MailDto> showAllSendMail(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<Mail> mailList = mailRepository.findBySenderId(user);
        List<MailDto> mailDtoList = new ArrayList<>();

        for(Mail m : mailList){
            mailDtoList.add(MailDto.builder()
                    .mailContent(m.getMailContent())
                    .sendUserName(m.getSendUser().getUserNickname())
                    .receiveUserName(m.getReceiveUser().getUserNickname())
                    .checkRead(m.isCheckRead())
                    .sendTime(m.getSendTime())
                    .build());
        }
        return mailDtoList;
    }

    // 특정 메시지 내용 확인
    public MailDto showOneMail(Long mailId){
        Mail mail = mailRepository.findById(mailId)
                .orElseThrow(() -> new IllegalArgumentException("아이디에 해당하는 메일이 존재하지 않습니다."));

        mail.Read(); // 게시글 읽음처리

        return MailDto.builder()
                .mailContent(mail.getMailContent())
                .sendUserName(mail.getSendUser().getUserNickname())
                .receiveUserName(mail.getReceiveUser().getUserNickname())
                .checkRead(mail.isCheckRead())
                .sendTime(mail.getSendTime())
                .build();
    }

    // 특정 보낸 메시지 삭제
    public boolean deleteSendMail(Long mailId){
        Mail mail = mailRepository.findById(mailId)
                .orElseThrow(() -> new IllegalArgumentException("아이디에 해당하는 메일이 존재하지 않습니다."));
        mail.deleteSend();
        return true;
    }

    // 특정 메시지 삭제
    public boolean deleteReceiveMail(Long mailId){
        Mail mail = mailRepository.findById(mailId)
                .orElseThrow(() -> new IllegalArgumentException("아이디에 해당하는 메일이 존재하지 않습니다."));
        mail.deleteReceive();
        return true;
    }

    // 추후 배치 등록 - db에서 메일 완전히 삭제
    public boolean deleteMail(){
        List<Mail> mailList = mailRepository.findDeleteMail();
        for(Mail m : mailList){
            mailRepository.delete(m);
        }
        return true;
    }

}
