package com.example.onjeong.user;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.family.repository.FamilyRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.util.FamilyUtils;
import com.example.onjeong.util.UserUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FamilyRepository familyRepository;

    public final Long userId= 1L;
    public final String userNickname= "gildong";


    @Test
    void 유저등록() {
        // given
        final Family family= FamilyUtils.getRandomFamily();
        final Family savedFamily= familyRepository.save(family);
        final User user = UserUtils.getRandomUser(savedFamily);

        // when
        final User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser.getUserId()).isEqualTo(user.getUserId());
        assertThat(savedUser.getUserName()).isEqualTo(user.getUserName());
        assertThat(savedUser.getUserNickname()).isEqualTo(user.getUserNickname());
        assertThat(savedUser.getUserBirth()).isEqualTo(user.getUserBirth());
    }

    @Test
    void 유저삭제() {
        // given
        final User user= userRepository.findById(userId).get();

        // when
        userRepository.delete(user);

        // then
    }

    @Test
    void 닉네임으로유저한명조회(){
        //given


        //when
        final User getUser= userRepository.findByUserNickname(userNickname).get();

        //then
        assertThat(userRepository.findById(getUser.getUserId()).isPresent()).isEqualTo(true);
    }

    @Test
    void 닉네임중복체크_중복된경우(){
        //given
        final String randomUserNickname= RandomStringUtils.random(10, true, true);

        //when
        final boolean isExisted= userRepository.existsByUserNickname(randomUserNickname);

        //then
        assertThat(isExisted).isEqualTo(false);
    }

    @Test
    void 닉네임중복체크_중복안된경우(){
        //given


        //when
        final boolean isExisted= userRepository.existsByUserNickname(userNickname);

        //then
        assertThat(isExisted).isEqualTo(true);
    }
}