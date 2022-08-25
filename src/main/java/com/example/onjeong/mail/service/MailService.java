package com.example.onjeong.mail.service;




import com.example.onjeong.mail.domain.Mail;
import com.example.onjeong.mail.dto.MailDto;
import com.example.onjeong.mail.dto.MailRequestDto;
import com.example.onjeong.mail.repository.MailRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MailService {

    private final UserRepository userRepository;
    private final MailRepository mailRepository;

    // 메시지 전송
    @Transactional
    public Mail sendMail(MailRequestDto mailSendDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> sendUser = userRepository.findByUserNickname(authentication.getName());

        User ReceiveUser = userRepository.findById(mailSendDto.getReceiveUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Mail mail = Mail.builder()
                .mailContent(mailSendDto.getMailContent())
                .sendUser(sendUser.get())
                .receiveUser(ReceiveUser)
                .checkRead(false)
                .receiverWantDelete(false)
                .senderWantDelete(false)
                .sendTime(LocalDateTime.now())
                .build();

        return mailRepository.save(mail);
    }

    // 받은 메일함 확인
    public List<MailDto> showAllReceiveMail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUserNickname(authentication.getName());

        List<Mail> mailList = mailRepository.findByReceiver(user.get().getUserId());
        List<MailDto> mailDtoList = new ArrayList<>();

        for(Mail m : mailList){
            mailDtoList.add(MailDto.builder()
                    .mailId(m.getMailId())
                    .mailContent(m.getMailContent())
                    .sendUserName(m.getSendUser().getUserStatus())
                    .receiveUserName(m.getReceiveUser().getUserStatus())
                    .checkRead(m.isCheckRead())
                    .sendTime(m.getSendTime())
                    .build());
        }
        return mailDtoList;
    }

    // 보낸 메일함 확인
    public List<MailDto> showAllSendMail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUserNickname(authentication.getName());

        List<Mail> mailList = mailRepository.findBySender(user.get().getUserId());
        List<MailDto> mailDtoList = new ArrayList<>();

        for(Mail m : mailList){
            mailDtoList.add(MailDto.builder()
                    .mailId(m.getMailId())
                    .mailContent(m.getMailContent())
                    .sendUserName(m.getSendUser().getUserStatus())
                    .receiveUserName(m.getReceiveUser().getUserStatus())
                    .checkRead(m.isCheckRead())
                    .sendTime(m.getSendTime())
                    .build());
        }
        return mailDtoList;
    }

    // 특정 메시지 내용 확인
    @Transactional
    public MailDto showOneMail(Long mailId){
        Mail mail = mailRepository.findById(mailId)
                .orElseThrow(() -> new IllegalArgumentException("아이디에 해당하는 메일이 존재하지 않습니다."));

        mail.Read(); // 게시글 읽음처리

        return MailDto.builder()
                .mailId(mail.getMailId())
                .mailContent(mail.getMailContent())
                .sendUserName(mail.getSendUser().getUserStatus())
                .receiveUserName(mail.getReceiveUser().getUserStatus())
                .checkRead(mail.isCheckRead())
                .sendTime(mail.getSendTime())
                .build();
    }

    // 특정 보낸 메시지 삭제
    @Transactional
    public boolean deleteSendMail(Long[] mailIds){
        for(Long mailId : mailIds){
            Mail mail = mailRepository.findById(mailId)
                    .orElseThrow(() -> new IllegalArgumentException("아이디에 해당하는 메일이 존재하지 않습니다."));
            mail.deleteSend();

            if(mail.isReceiverWantDelete() && mail.isSenderWantDelete()){
                mailRepository.delete(mail);
            }
        }
        return true;
    }

    // 특정 받은 메시지 삭제
    @Transactional
    public boolean deleteReceiveMail(Long[] mailIds){
        for(Long mailId : mailIds) {
            Mail mail = mailRepository.findById(mailId)
                    .orElseThrow(() -> new IllegalArgumentException("아이디에 해당하는 메일이 존재하지 않습니다."));
            mail.deleteReceive();

            if(mail.isReceiverWantDelete() && mail.isSenderWantDelete()){
                mailRepository.delete(mail);
            }
        }
        return true;
    }
}