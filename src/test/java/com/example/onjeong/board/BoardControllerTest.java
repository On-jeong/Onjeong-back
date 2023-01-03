package com.example.onjeong.board;


import com.example.onjeong.Config.SecurityConfig;
import com.example.onjeong.Config.WebMvcConfig;
import com.example.onjeong.board.controller.BoardController;
import com.example.onjeong.board.service.BoardService;
import com.example.onjeong.fcm.FCMService;
import com.example.onjeong.coin.service.CoinService;
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
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = BoardController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                ),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebMvcConfig.class
                )})
public class BoardControllerTest {

    @MockBean
    private BoardService boardService;

    @MockBean
    private CoinService coinService;

    @MockBean
    private FCMService fcmService;

    @Autowired
    private MockMvc mockMvc;


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
    void 오늘의_기록_모두_가져오기() throws Exception {
        //given
        final String boardDate= "2022-12-03";
        final String uri = "/boards/all/" + boardDate;


        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("B001")));
    }


    @Nested
    class 오늘의_기록_작성하기{
        @Test
        @WithMockUser
        void 글과이미지파일_둘다_작성할경우() throws Exception {
            //given
            final String boardDate= "2022-12-03";
            final String uri = "/boards/" + boardDate;
            final String boardContent= "hi";
            final MockMultipartFile imgFile= new MockMultipartFile("images", "",
                    "image/png", FileInputStream.nullInputStream());
            final MockMultipartFile textFile = new MockMultipartFile("boardContent", "",
                    "text/plain", boardContent.getBytes(StandardCharsets.UTF_8));


            //when
            ResultActions resultActions = mockMvc.perform(
                    multipart(uri)
                            .file(textFile)
                            .file(imgFile)
                            .accept(MediaType.APPLICATION_JSON)
            );


            //then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", equalTo("B003")));
        }

        @Test
        @WithMockUser
        void 글만_작성할경우() throws Exception {
            //given
            final String boardDate= "2022-12-03";
            final String uri = "/boards/" + boardDate;
            final String boardContent= "hi";
            final MockMultipartFile textFile = new MockMultipartFile("boardContent", "",
                    "text/plain", boardContent.getBytes(StandardCharsets.UTF_8));


            //when
            ResultActions resultActions = mockMvc.perform(
                    multipart(uri)
                            .file(textFile)
                            .accept(MediaType.APPLICATION_JSON)
            );


            //then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", equalTo("B003")));
        }
    }


    @Test
    @WithMockUser
    void 오늘의_기록_한개_가져오기() throws Exception {
        //given
        final Long boardId= 1L;
        final String uri = "/boards/"+ boardId;


        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("B002")));
    }


    @Test
    @WithMockUser
    void 오늘의_기록_삭제하기() throws Exception {
        //given
        final Long boardId= 1L;
        final String uri = "/boards/" + boardId;


        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("B005")));
    }

}