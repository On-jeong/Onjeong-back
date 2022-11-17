package com.example.onjeong.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.family.repository.FamilyRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.domain.UserRole;
import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.util.FamilyUtils;
import com.example.onjeong.util.UserUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void 유저등록() {
        // given
        final Family family= FamilyUtils.getRandomFamily();
        final User user = UserUtils.getRandomUser(family);

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
        final Family family= FamilyUtils.getRandomFamily();
        final User user = UserUtils.getRandomUser(family);
        final User getUser= userRepository.save(user);

        // when
        userRepository.delete(getUser);

        // then
        assertThat(userRepository.findById(getUser.getUserId()).isPresent()).isEqualTo(false);
    }
}
