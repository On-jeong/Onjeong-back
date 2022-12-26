package com.example.onjeong.mail;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.mail.domain.Mail;
import com.example.onjeong.mail.repository.MailRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.util.FamilyUtils;
import com.example.onjeong.util.MailUtils;
import com.example.onjeong.util.UserUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MailRepositoryTest {

    @Autowired
    MailRepository mailRepository;


    @Test
    void 메일저장확인() {
        // given
        final Family family = FamilyUtils.getRandomFamily();
        final User user1 = UserUtils.getRandomUser(family);
        final User user2 = UserUtils.getRandomUser(family);

        // when
        Mail mail = MailUtils.getRandomMail(user1, user2);
        Mail savedMail = mailRepository.save(mail);

        // then
        assertEquals(mail.getMailId(), savedMail.getMailId());
    }

    @Test
    @DisplayName("받은메일함 조회 시 최신순으로 최대 30개의 메일만 조회 되는지 확인")
    void 받은메일함확인() {
        //given
        final Family family = FamilyUtils.getRandomFamily();
        final User user = UserUtils.getRandomUser(family);

        // when
        List<Mail> result = mailRepository.findByReceiver(user.getUserId());

        // then
        // 리스트 갯수가 30 이하인지
        assertThat(result.size(),  lessThanOrEqualTo(30));
        for(Mail m : result){
            // 받는 사람이 정확한지
            assertEquals(user.getUserId(), m.getReceiveUser().getUserId());
            // 삭제한 메일은 아닌지
            assertFalse(m.isSenderWantDelete());
        }
        // 최신 메일만 정리되어 있는지 -> 1L은 가장 처음 들어간 메일이므로 없어야 한다.
        for(Mail m : result){
            assertNotEquals(1L, m.getMailId());
        }

    }

    @Test
    @DisplayName("보낸메일함 조회 시 최신순으로 최대 30개의 메일만 조회 되는지 확인")
    void 보낸메일함확인() {
        //given
        final Family family = FamilyUtils.getRandomFamily();
        final User user = UserUtils.getRandomUser(family);

        // when
        List<Mail> result = mailRepository.findBySender(user.getUserId());

        // then
        // 리스트 갯수가 30 이하인지
        assertThat(result.size(),  lessThanOrEqualTo(30));
        for(Mail m : result){
            // 받는 사람이 정확한지
            assertEquals(user.getUserId(), m.getSendUser().getUserId());
            // 삭제한 메일은 아닌지
            assertFalse(m.isSenderWantDelete());
        }
        // 최신 메일만 정리되어 있는지 -> 1L은 가장 처음 들어간 메일이므로 없어야 한다.
        for(Mail m : result){
            assertNotEquals(1L, m.getMailId());
        }
    }
}