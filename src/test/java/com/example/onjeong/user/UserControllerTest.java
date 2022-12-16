package com.example.onjeong.user;

import com.example.onjeong.Config.SecurityConfig;
import com.example.onjeong.user.controller.UserController;
import com.example.onjeong.user.dto.UserAccountDto;
import com.example.onjeong.user.dto.UserDeleteDto;
import com.example.onjeong.user.dto.UserJoinDto;
import com.example.onjeong.user.dto.UserJoinedDto;
import com.example.onjeong.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )})
public class UserControllerTest {

    @MockBean
    private UserService userService;

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


    @Nested
    class 회원가입{
        @Nested
        class 가족회원이없는회원가입{
            @Test
            @WithMockUser
            void 가족회원이없는회원가입_성공() throws Exception{
                //given
                final String url = "/accounts";
                final UserJoinDto userJoinDto= userJoinDto();
                doReturn(false).when(userService).isUserNicknameDuplicated(userJoinDto.getUserNickname());

                //when
                ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userJoinDto))
                );

                //then
                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.code", equalTo("U001")));
            }

            @Test
            @WithMockUser
            void 가족회원이없는회원가입_실패() throws Exception{
                //given
                final String url = "/accounts";
                final UserJoinDto userJoinDto= userJoinDto();
                doReturn(true).when(userService).isUserNicknameDuplicated(userJoinDto.getUserNickname());

                //when
                ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userJoinDto))
                );

                //then
                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.status", equalTo(Integer.valueOf("400"))))
                        .andExpect(jsonPath("$.code", equalTo("U003")))
                        .andExpect(jsonPath("$.message", equalTo("USER NICKNAME DUPLICATION")));
            }
        }


        @Nested
        class 가족회원이있는회원가입{
            @Test
            @WithMockUser
            void 가족회원이있는회원가입_성공() throws Exception{
                //given
                final String url = "/accounts/joined";
                final UserJoinedDto userJoinedDto= userJoinedDto();
                doReturn(false).when(userService).isUserNicknameDuplicated(userJoinedDto.getUserNickname());

                //when
                ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userJoinedDto))
                );

                //then
                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.code", equalTo("U001")));
            }

            @Test
            @WithMockUser
            void 가족회원이있는회원가입_실패() throws Exception{
                //given
                final String url = "/accounts/joined";
                final UserJoinedDto userJoinedDto= userJoinedDto();
                doReturn(true).when(userService).isUserNicknameDuplicated(userJoinedDto.getUserNickname());

                //when
                ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userJoinedDto))
                );

                //then
                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.status", equalTo(Integer.valueOf("400"))))
                        .andExpect(jsonPath("$.code", equalTo("U003")))
                        .andExpect(jsonPath("$.message", equalTo("USER NICKNAME DUPLICATION")));
            }
        }
    }

    @Test
    @WithMockUser
    void 회원정보수정_성공() throws Exception{
        //given
        final String url = "/accounts";
        final UserAccountDto userAccountDto= userAccountDto();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAccountDto))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("U004")));
    }

    @Test
    @WithMockUser
    void 회원탈퇴_성공() throws Exception{
        //given
        final String url = "/accounts";
        final UserDeleteDto userDeleteDto= userDeleteDto();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(userDeleteDto))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("U005")));
    }

    @Test
    @WithMockUser
    void 유저기본정보조회_성공() throws Exception{
        //given
        final String url = "/user-information";

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("U006")));
    }


    private UserDeleteDto userDeleteDto(){
        return UserDeleteDto.builder()
                .userPassword("pw123")
                .build();
    }

    private UserJoinDto userJoinDto(){
        return UserJoinDto.builder()
                .userName("HongGilDong")
                .userNickname("GilDong")
                .userPassword("pw123")
                .userStatus("son")
                .userBirth(LocalDate.now())
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
                .build();
    }

    private UserAccountDto userAccountDto(){
        return UserAccountDto.builder()
                .userName("HongGilDong")
                .userPassword("pw123")
                .userStatus("son")
                .userBirth(LocalDate.now())
                .build();
    }
}