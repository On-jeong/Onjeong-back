package com.example.onjeong.user;

import com.example.onjeong.S3.S3Uploader;
import com.example.onjeong.board.repository.BoardRepository;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.family.repository.FamilyRepository;
import com.example.onjeong.home.domain.Flower;
import com.example.onjeong.home.repository.FlowerRepository;
import com.example.onjeong.profile.domain.Profile;
import com.example.onjeong.profile.repository.ProfileRepository;
import com.example.onjeong.question.repository.AnswerRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.dto.*;
import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.user.service.UserService;
import com.example.onjeong.util.AuthUtil;
import com.example.onjeong.util.FamilyUtils;
import com.example.onjeong.util.ProfileUtils;
import com.example.onjeong.util.UserUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


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
    private BoardRepository boardRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private S3Uploader s3Uploader;

    @Mock
    private FlowerRepository flowerRepository;


    @Nested
    class 회원가입{
        @Test
        void 가족회원이없는경우(){
            //given
            final UserJoinDto userJoinDto= userJoinDto();


            //when
            userService.signUp(userJoinDto);


            //then
            verify(familyRepository,times(1)).save(any(Family.class));
            verify(userRepository,times(1)).save(any(User.class));
            verify(profileRepository,times(1)).save(any(Profile.class));
            verify(flowerRepository,times(1)).save(any(Flower.class));
        }

        @Test
        void 가족회원이있는경우(){
            //given
            final Family family= FamilyUtils.getRandomFamily();
            final User joinedUser= UserUtils.getRandomUser(family);
            final UserJoinedDto userJoinedDto= userJoinedDto();

            doReturn(Optional.of(joinedUser)).when(userRepository).findByUserNickname(userJoinedDto.getJoinedNickname());


            //when
            userService.signUpJoined(userJoinedDto);


            //then
            verify(userRepository,times(1)).save(any(User.class));
            verify(profileRepository,times(1)).save(any(Profile.class));
        }
    }


    @Test
    void 회원정보_수정(){
        //given
        final Family family= FamilyUtils.getRandomFamily();
        final User user= UserUtils.getRandomUser(family);
        final UserAccountDto userAccountDto= userAccountDto();

        doReturn(user).when(authUtil).getUserByAuthentication();


        //when
        userService.modifyUserInformation(userAccountDto);


        //then
        assertThat(user.getUserName()).isEqualTo(userAccountDto.getUserName());
    }


    @Nested
    class 회원탈퇴{
        @Test
        void 가족구성원한명일경우(){
            //given
            final Family family= FamilyUtils.getRandomFamily();
            final User user= UserUtils.getRandomUser(family);
            final Profile profile= ProfileUtils.getRandomProfile(family, user);
            final UserDeleteDto userDeleteDto= userDeleteDto(user.getUserPassword());
            family.getUsers().add(user);

            ReflectionTestUtils.setField(userService, "AWS_S3_BUCKET_URL", "");

            doReturn(user).when(authUtil).getUserByAuthentication();
            doReturn(Optional.of(family)).when(familyRepository).findById(user.getFamily().getFamilyId());
            doReturn(Optional.of(profile)).when(profileRepository).findByUser(user);
            doReturn(true).when(passwordEncoder).matches(userDeleteDto.getUserPassword(),user.getUserPassword());


            //when
            userService.deleteUser(userDeleteDto);


            //then
            verify(familyRepository,times(1)).delete(family);
        }

        @Test
        void 가족구성원두명이상일경우(){
            //given
            final Family family= FamilyUtils.getRandomFamily();
            final User user1= UserUtils.getRandomUser(family);
            final User user2= UserUtils.getRandomUser(family);
            family.getUsers().add(user1);
            family.getUsers().add(user2);
            final Profile profile= ProfileUtils.getRandomProfile(family, user1);
            final UserDeleteDto userDeleteDto= userDeleteDto(user1.getUserPassword());

            ReflectionTestUtils.setField(userService, "AWS_S3_BUCKET_URL", "");

            doReturn(user1).when(authUtil).getUserByAuthentication();
            doReturn(Optional.of(family)).when(familyRepository).findById(user1.getFamily().getFamilyId());
            doReturn(Optional.of(profile)).when(profileRepository).findByUser(user1);
            doReturn(true).when(passwordEncoder).matches(userDeleteDto.getUserPassword(),user1.getUserPassword());


            //when
            userService.deleteUser(userDeleteDto);


            //then
            verify(familyRepository,times(0)).delete(family);
        }
    }


    @Test
    void 유저기본정보_조회(){
        //given
        final Family family= FamilyUtils.getRandomFamily();
        final User user= UserUtils.getRandomUser(family);

        doReturn(user).when(authUtil).getUserByAuthentication();


        //when
        UserDto result= userService.getUser();


        //then
        assertThat(result.getUserName()).isEqualTo(user.getUserName());
        assertThat(result.getFamilyId()).isEqualTo(user.getFamily().getFamilyId());
    }


    private UserJoinDto userJoinDto(){
        return UserJoinDto.builder()
                .userName("HongGilDong")
                .userNickname("GilDong")
                .userPassword("pw123")
                .userStatus("son")
                .userBirth(LocalDate.now())
                .userEmail("@email")
                .build();
    }

    private UserJoinedDto userJoinedDto(){
        return UserJoinedDto.builder()
                .userName("HongGilDong")
                .userNickname("GilDong")
                .userPassword("pw123")
                .userStatus("son")
                .userBirth(LocalDate.now())
                .joinedNickname("joinedNickname")
                .userEmail("@email")
                .build();
    }

    private UserAccountDto userAccountDto(){
        return UserAccountDto.builder()
                .userName("HongGilDong")
                .userPassword("pw123")
                .userStatus("son")
                .userBirth(LocalDate.now())
                .userEmail("@email")
                .build();
    }

    private UserDeleteDto userDeleteDto(String userPassword){
        return UserDeleteDto.builder()
                .userPassword(userPassword)
                .build();
    }
}