package com.example.onjeong.mail.service;




import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.mail.domain.Mail;
import com.example.onjeong.mail.dto.MailDto;
import com.example.onjeong.mail.dto.MailRequestDto;
import com.example.onjeong.mail.exception.MailNotExistException;
import com.example.onjeong.user.exception.UserNotExistException;
import com.example.onjeong.mail.repository.MailRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.util.AuthUtil;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuthUtil authUtil;

    // 메시지 전송
    @Transactional
    public Mail sendMail(MailRequestDto mailSendDto) throws FirebaseMessagingException {
        User sendUser = authUtil.getUserByAuthentication();
        User receiveUser = authUtil.getReceiveUserByUserId(mailSendDto.getReceiveUserId());
        Mail mail = Mail.builder()
                .mailContent(mailSendDto.getMailContent())
                .sendUser(sendUser)
                .receiveUser(receiveUser)
                .checkRead(false)
                .receiverWantDelete(false)
                .senderWantDelete(false)
                .sendTime(LocalDateTime.now())
                .build();

        return mailRepository.save(mail);
    }

    // 받은 메일함 확인
    public List<MailDto> showAllReceiveMail(){
        User user = authUtil.getUserByAuthentication();
        List<Mail> mailList = mailRepository.findByReceiver(user.getUserId());
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
        User user = authUtil.getUserByAuthentication();
        List<Mail> mailList = mailRepository.findBySender(user.getUserId());
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
                .orElseThrow(() -> new MailNotExistException("mail not exist", ErrorCode.MAIL_NOTEXIST));
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
    public List<Mail> deleteSendMail(Long[] mailIds){
        List<Mail> deleteList = new ArrayList<>();
        for(Long mailId : mailIds){
            Mail mail = mailRepository.findById(mailId)
                    .orElseThrow(() -> new MailNotExistException("mail not exist", ErrorCode.MAIL_NOTEXIST));
            mail.deleteSend();
            deleteList.add(mail);

            if(mail.isReceiverWantDelete() && mail.isSenderWantDelete()){
                mailRepository.delete(mail);
            }
        }
        return deleteList;
    }

    // 특정 받은 메시지 삭제
    @Transactional
    public List<Mail> deleteReceiveMail(Long[] mailIds){
        List<Mail> deleteList = new ArrayList<>();
        for(Long mailId : mailIds) {
            Mail mail = mailRepository.findById(mailId)
                    .orElseThrow(() -> new MailNotExistException("mail not exist", ErrorCode.MAIL_NOTEXIST));
            mail.deleteReceive();
            deleteList.add(mail);

            if(mail.isReceiverWantDelete() && mail.isSenderWantDelete()){
                mailRepository.delete(mail);
            }
        }
        return deleteList;
    }
}