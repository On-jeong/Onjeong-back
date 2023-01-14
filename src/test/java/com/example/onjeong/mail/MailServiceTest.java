package com.example.onjeong.mail;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.mail.domain.Mail;
import com.example.onjeong.mail.dto.MailDto;
import com.example.onjeong.mail.dto.MailRequestDto;
import com.example.onjeong.mail.exception.MailNotExistException;
import com.example.onjeong.mail.repository.MailRepository;
import com.example.onjeong.mail.service.MailService;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.util.AuthUtil;
import com.example.onjeong.util.FamilyUtils;
import com.example.onjeong.util.MailUtils;
import com.example.onjeong.util.UserUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @InjectMocks
    private MailService mailService;

    @Mock
    private AuthUtil authUtil;

    @Mock
    private MailRepository mailRepository;

    @Mock
    private Pageable pageable;

    @Test
    void 메일전송() throws FirebaseMessagingException {

        //given
        final Family family = FamilyUtils.getRandomFamily();
        final User user1 = UserUtils.getRandomUser(family);
        final User user2 = UserUtils.getRandomUser(family);

        final String mailContent = "hi";

        lenient().doReturn(user1).when(authUtil).getUserByAuthentication();
        lenient().doReturn(user2).when(authUtil).getUserByUserId(user2.getUserId());

        //when
        mailService.sendMail(mailRequestDto(user2.getUserId(), mailContent));

        //then
        verify(mailRepository,times(1)).save(any(Mail.class));
    }

    @Test
    void 받은메일함확인() {

        //given
        final Family family = FamilyUtils.getRandomFamily();
        final User user = UserUtils.getRandomUser(family);
        final User user2 = UserUtils.getRandomUser(family);

        final List<Mail> mailList = new ArrayList<>();
        for(int i=0; i<3; i++){
            final Mail mail = MailUtils.getRandomMail(user2, user);
            mailList.add(mail);
        }
        final Page<Mail> mailPage = new PageImpl(mailList);
        doReturn(user).when(authUtil).getUserByAuthentication();
        doReturn(mailPage).when(mailRepository).findByReceiver(pageable, user.getUserId());

        //when
        List<MailDto> result = mailService.showAllReceiveMail(pageable);

        //then
        assertEquals(result.size(), 3);
        for(MailDto m : result){
            assertEquals(m.getReceiveUserName(), user.getUserStatus());
        }
    }

    @Test
    void 보낸메일함확인() {
        //given
        final Family family = FamilyUtils.getRandomFamily();
        final User user = UserUtils.getRandomUser(family);
        final User user2 = UserUtils.getRandomUser(family);

        final List<Mail> mailList = new ArrayList<>();
        for(int i=0; i<3; i++){
            final Mail mail = MailUtils.getRandomMail(user, user2);
            mailList.add(mail);
        }
        final Page<Mail> mailPage = new PageImpl(mailList);
        doReturn(user).when(authUtil).getUserByAuthentication();
        doReturn(mailPage).when(mailRepository).findBySender(pageable, user.getUserId());

        //when
        List<MailDto> result = mailService.showAllSendMail(pageable);

        //then
        assertEquals(result.size(), 3);
        for(MailDto m : result){
            assertEquals(m.getSendUserName(), user.getUserStatus());
        }
    }

    @Nested
    class 특정메일확인{

        @Test
        void 성공(){
            //given
            final Family family= FamilyUtils.getRandomFamily();
            final User user = UserUtils.getRandomUser(family);
            final User user2 = UserUtils.getRandomUser(family);

            final Long mailId= 1L;
            final Mail mail = MailUtils.getRandomMail(user, user2);

            doReturn(Optional.of(mail)).when(mailRepository).findById(mailId);

            //when
            MailDto foundMail = mailService.showOneMail(mailId);

            //then
            assertEquals(foundMail.getReceiveUserName(), mail.getReceiveUser().getUserStatus());
            assertEquals(foundMail.getSendUserName(), mail.getSendUser().getUserStatus());
            assertEquals(foundMail.getMailContent(), mail.getMailContent());
            assertEquals(foundMail.getCheckRead(), true);
        }

        @Test
        void 실패(){
            //given
            final Long mailId= 1L;
            doReturn(Optional.empty()).when(mailRepository).findById(mailId);

            //when
            Throwable exception = assertThrows(MailNotExistException.class, () -> {
                mailService.showOneMail(mailId);
            });

            //then
            assertEquals(exception.getMessage(), "mail not exist");
        }
    }

    @Test
    void 보낸메일삭제() {
        //given
        final Family family = FamilyUtils.getRandomFamily();
        final User user = UserUtils.getRandomUser(family);
        final User user2 = UserUtils.getRandomUser(family);
        final Long[] mailIds = {1L, 2L, 3L};
        final Mail mail = MailUtils.getRandomMail(user, user2);

        //when
        for(Long id : mailIds){
            doReturn(Optional.of(mail)).when(mailRepository).findById(id);
        }
        List<Mail> deletedMailList = mailService.deleteSendMail(mailIds);

        //then
        for(Mail m : deletedMailList){
            assertTrue(m.isSenderWantDelete());
        }
    }

    @Test
    void 받은메일삭제() {
        //given
        final Family family = FamilyUtils.getRandomFamily();
        final User user = UserUtils.getRandomUser(family);
        final User user2 = UserUtils.getRandomUser(family);
        final Long[] mailIds = {1L, 2L, 3L};
        final Mail mail = MailUtils.getRandomMail(user, user2);

        //when
        for(Long id : mailIds){
            doReturn(Optional.of(mail)).when(mailRepository).findById(id);
        }
        List<Mail> deletedMailList = mailService.deleteReceiveMail(mailIds);

        //then
        for(Mail m : deletedMailList){
            assertTrue(m.isReceiverWantDelete());
        }
    }

    private MailRequestDto mailRequestDto(Long receiveUserId, String mailContent){
        return MailRequestDto.builder()
                .receiveUserId(receiveUserId)
                .mailContent(mailContent)
                .build();
    }
}