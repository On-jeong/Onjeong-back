package com.example.onjeong.profile;


import com.example.onjeong.Config.SecurityConfig;
import com.example.onjeong.Config.WebMvcConfig;
import com.example.onjeong.board.controller.BoardController;
import com.example.onjeong.board.service.BoardService;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.fcm.FCMService;
import com.example.onjeong.home.domain.CoinHistoryType;
import com.example.onjeong.home.service.CoinService;
import com.example.onjeong.profile.controller.ProfileController;
import com.example.onjeong.profile.domain.Profile;
import com.example.onjeong.profile.dto.ProfileMessageDto;
import com.example.onjeong.profile.dto.SelfIntroductionAnswerRegisterDto;
import com.example.onjeong.profile.service.ProfileService;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.util.FamilyUtils;
import com.example.onjeong.util.ProfileUtils;
import com.example.onjeong.util.UserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.FileInputStream;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ProfileController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                ),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebMvcConfig.class
                )})
public class ProfileControllerTest {

    @MockBean
    private ProfileService profileService;

    @MockBean
    private CoinService coinService;

    @MockBean
    private FCMService fcmService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @TestConfiguration
    static class DefaultConfigWithoutCsrf extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            super.configure(http);
            http.csrf().disable();
        }
    }


    @Test
    @WithMockUser
    void 가족구성원모두조회() throws Exception {
        //given
        final String uri = "/families";


        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("P001")));
    }


    @Test
    @WithMockUser
    void 프로필상단에_보여줄_개인정보_조회() throws Exception {
        //given
        final Long userId= 1L;
        final String uri = "/profiles/" + userId;


        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("P002")));
    }

    @Nested
    class 프로필사진{
        @Test
        @WithMockUser
        void 등록_처음등록하지않은경우() throws Exception {
            //given
            final String uri = "/profiles/image";
            final MockMultipartFile imgFile= new MockMultipartFile("images", "",
                    "image/png", FileInputStream.nullInputStream());
            final Family family= FamilyUtils.getRandomFamily();
            final User user= UserUtils.getRandomUser(family);
            final Profile profile= ProfileUtils.getRandomProfile(family, user);

            doReturn(true).when(profileService).checkProfileUpload();
            doReturn(profile).when(profileService).registerProfileImage(imgFile);

            //when
            ResultActions resultActions = mockMvc.perform(
                    multipart(uri)
                            .file(imgFile)
            );


            //then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", equalTo("P003")));
            verify(coinService,times(0)).coinSave(CoinHistoryType.PROFILEIMAGE, 100);
        }

        @Test
        @WithMockUser
        void 등록_처음등록한경우() throws Exception {
            //given
            final String uri = "/profiles/image";
            final MockMultipartFile imgFile= new MockMultipartFile("images", "",
                    "image/png", FileInputStream.nullInputStream());
            final Family family= FamilyUtils.getRandomFamily();
            final User user= UserUtils.getRandomUser(family);
            final Profile profile= ProfileUtils.getRandomProfile(family, user);

            doReturn(false).when(profileService).checkProfileUpload();
            doReturn(profile).when(profileService).registerProfileImage(imgFile);


            //when
            ResultActions resultActions = mockMvc.perform(
                    multipart(uri)
                            .file(imgFile)
            );


            //then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", equalTo("P003")));
            verify(coinService,times(1)).coinSave(CoinHistoryType.PROFILEIMAGE, 100);
        }

        @Test
        @WithMockUser
        void 삭제() throws Exception {
            //given
            final String uri = "/profiles/image";


            //when
            ResultActions resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.delete(uri)
                            .contentType(MediaType.APPLICATION_JSON)
            );


            //then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", equalTo("P004")));
        }
    }


    @Nested
    class 상태메시지{
        @Test
        @WithMockUser
        void 조회() throws Exception {
            //given
            final Long userId= 1L;
            final String uri = "/profiles/message/" + userId;


            //when
            ResultActions resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.get(uri)
                            .contentType(MediaType.APPLICATION_JSON)
            );


            //then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", equalTo("P005")));
        }

        @Test
        @WithMockUser
        void 작성_처음등록하지않은경우() throws Exception {
            //given
            final String uri = "/profiles/message/";
            final ProfileMessageDto profileMessageDto= profileMessageDto("profile message to register");

            doReturn(true).when(profileService).checkProfileUpload();


            //when
            ResultActions resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(profileMessageDto))
            );


            //then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", equalTo("P006")));
        }

        @Test
        @WithMockUser
        void 작성_처음등록하는경우() throws Exception {
            //given
            final String uri = "/profiles/message/";
            final ProfileMessageDto profileMessageDto= profileMessageDto("profile message to register");

            doReturn(false).when(profileService).checkProfileUpload();


            //when
            ResultActions resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(profileMessageDto))
            );


            //then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", equalTo("P006")));
            verify(coinService,times(1)).coinSave(CoinHistoryType.PROFILEMESSAGE, 100);
        }

        @Test
        @WithMockUser
        void 수정() throws Exception {
            //given
            final String uri = "/profiles/message/";
            final ProfileMessageDto profileMessageDto= profileMessageDto("profile message to modify");


            //when
            ResultActions resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.patch(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(profileMessageDto))
            );


            //then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", equalTo("P007")));
        }
    }


    @Test
    @WithMockUser
    void 유저_개인정보와상태메시지_조회() throws Exception {
        //given
        final Long userId= 1L;
        final String uri = "/profiles/" + userId + "/user-profile";


        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("P020")));

    }


    @Test
    @WithMockUser
    void 자기소개답변목록_조회() throws Exception {
        //given
        final Long userId= 1L;
        final String uri = "/profiles/" + userId + "/self-introduction";


        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("P021")));

    }


    @Test
    @WithMockUser
    void 좋아하는것_작성() throws Exception {
        //given
        final Long userId= 1L;
        final String uri = "/profiles/favorites/" + userId;
        final SelfIntroductionAnswerRegisterDto selfIntroductionAnswerRegisterDto=
                selfIntroductionAnswerRegisterDto("answer to register");


        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(selfIntroductionAnswerRegisterDto))
        );


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("P009")));

    }


    @Test
    @WithMockUser
    void 좋아하는것_삭제() throws Exception {
        //given
        final Long userId= 1L;
        final Long selfIntroductionAnswerId= 1L;
        final String uri = "/profiles/favorites/" + userId + "/" + selfIntroductionAnswerId;


        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("P010")));

    }



    private ProfileMessageDto profileMessageDto(String message){
        return ProfileMessageDto.builder()
                .message(message)
                .build();
    }

    private SelfIntroductionAnswerRegisterDto selfIntroductionAnswerRegisterDto(String selfIntroductionAnswerContent){
        return SelfIntroductionAnswerRegisterDto.builder()
                .selfIntroductionAnswerContent(selfIntroductionAnswerContent)
                .build();
    }
}