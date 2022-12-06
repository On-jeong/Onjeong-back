package com.example.onjeong.mail;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.mail.domain.Mail;
import com.example.onjeong.mail.repository.MailRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.util.FamilyUtils;
import com.example.onjeong.util.MailUtils;
import com.example.onjeong.util.UserUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MailRepositoryTest {

    @Autowired
    private MailRepository mailRepository;

    @Test
    void 발신자메일_여러개_삭제(){
        //given
        final User sendUser= user();
        final User receiveUser= user();
        final List<Mail> mails= new ArrayList<>();

        for(int i=0;i<3;i++){
            final Mail mail= MailUtils.getRandomMail(sendUser, receiveUser);
            mails.add(mail);
        }
        mailRepository.saveAll(mails);


        //when
        mailRepository.deleteAllBySendUser(sendUser);


        //then
    }


    @Test
    void 수신자메일_여러개_삭제(){
        //given
        final User sendUser= user();
        final User receiveUser= user();
        final List<Mail> mails= new ArrayList<>();

        for(int i=0;i<3;i++){
            final Mail mail= MailUtils.getRandomMail(sendUser, receiveUser);
            mails.add(mail);
        }
        mailRepository.saveAll(mails);


        //when
        mailRepository.deleteAllByReceiveUser(receiveUser);


        //then
    }


    private User user(){
        final Family family= FamilyUtils.getRandomFamily();
        return UserUtils.getRandomUser(family);
    }
}
