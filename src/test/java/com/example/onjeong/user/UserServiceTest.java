package com.example.onjeong.user;

import static org.mockito.Mockito.*;


import com.example.onjeong.anniversary.repository.AnniversaryRepository;
import com.example.onjeong.board.repository.BoardRepository;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.family.repository.FamilyRepository;
import com.example.onjeong.home.repository.CoinHistoryRepository;
import com.example.onjeong.home.repository.FlowerRepository;
import com.example.onjeong.mail.repository.MailRepository;
import com.example.onjeong.profile.domain.*;
import com.example.onjeong.profile.repository.*;
import com.example.onjeong.question.repository.AnswerRepository;
import com.example.onjeong.question.repository.QuestionRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.dto.UserDeleteDto;
import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.user.service.UserService;
import com.example.onjeong.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FamilyRepository familyRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private AuthUtil authUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ExpressionRepository expressionRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private HateRepository hateRepository;

    @Mock
    private InterestRepository interestRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private MailRepository mailRepository;

    @Mock
    private CoinHistoryRepository coinHistoryRepository;

    @Mock
    private AnniversaryRepository anniversaryRepository;

    @Mock
    private FlowerRepository flowerRepository;


    @Test
    void 회원탈퇴_가족구성원한명일경우(){
        //given
        final Family family= FamilyUtils.getRandomFamily();
        final User user= UserUtils.getRandomUser(family);
        final Profile profile= ProfileUtils.getRandomProfile(family, user);

        UserDeleteDto userDeleteDto= userDeleteDto(user.getUserPassword());

        doReturn(user).when(authUtil).getUserByAuthentication();
        doReturn(Optional.of(family)).when(familyRepository).findById(user.getFamily().getFamilyId());
        doReturn(Optional.of(profile)).when(profileRepository).findByUser(user);
        doReturn(true).when(passwordEncoder).matches(userDeleteDto.getUserPassword(),user.getUserPassword());
        doNothing().when(expressionRepository).deleteAllByProfile(profile);
        doNothing().when(favoriteRepository).deleteAllByProfile(profile);
        doNothing().when(hateRepository).deleteAllByProfile(profile);
        doNothing().when(interestRepository).deleteAllByProfile(profile);
        doNothing().when(profileRepository).deleteByUser(user);
        doNothing().when(boardRepository).deleteAllByUser(user);
        doNothing().when(answerRepository).deleteAllByUser(user);
        doNothing().when(mailRepository).deleteAllByReceiveUser(user);
        doNothing().when(mailRepository).deleteAllBySendUser(user);
        doNothing().when(userRepository).delete(user);
        doNothing().when(anniversaryRepository).deleteAllByFamily(family);
        doNothing().when(questionRepository).deleteAllByFamily(family);
        doNothing().when(coinHistoryRepository).deleteAllByFamily(family);
        doNothing().when(flowerRepository).deleteAllByFamily(family);
        doNothing().when(familyRepository).delete(family);

        //when
        userService.deleteUser(userDeleteDto);

        //then
        verify(familyRepository,times(1)).delete(family);
    }


    @Test
    void 회원탈퇴_가족구성원두명이상일경우(){
        //given
        final Family family= FamilyUtils.getRandomFamily();
        final User user1= UserUtils.getRandomUser(family);
        final User user2= UserUtils.getRandomUser(family);
        family.getUsers().add(user1);
        family.getUsers().add(user2);
        final Profile profile= ProfileUtils.getRandomProfile(family, user1);

        UserDeleteDto userDeleteDto= userDeleteDto(user1.getUserPassword());

        doReturn(user1).when(authUtil).getUserByAuthentication();
        doReturn(Optional.of(family)).when(familyRepository).findById(user1.getFamily().getFamilyId());
        doReturn(Optional.of(profile)).when(profileRepository).findByUser(user1);
        doReturn(true).when(passwordEncoder).matches(userDeleteDto.getUserPassword(),user1.getUserPassword());
        doNothing().when(expressionRepository).deleteAllByProfile(profile);
        doNothing().when(favoriteRepository).deleteAllByProfile(profile);
        doNothing().when(hateRepository).deleteAllByProfile(profile);
        doNothing().when(interestRepository).deleteAllByProfile(profile);
        doNothing().when(profileRepository).deleteByUser(user1);
        doNothing().when(boardRepository).deleteAllByUser(user1);
        doNothing().when(answerRepository).deleteAllByUser(user1);
        doNothing().when(mailRepository).deleteAllByReceiveUser(user1);
        doNothing().when(mailRepository).deleteAllBySendUser(user1);
        doNothing().when(userRepository).delete(user1);

        //when
        userService.deleteUser(userDeleteDto);

        //then
        verify(boardRepository,times(1)).deleteAllByUser(user1);
        verify(familyRepository,times(0)).delete(family);
    }


    private UserDeleteDto userDeleteDto(String userPassword){
        return UserDeleteDto.builder()
                .userPassword(userPassword)
                .build();
    }
}
