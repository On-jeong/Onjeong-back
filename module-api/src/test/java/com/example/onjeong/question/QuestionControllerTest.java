package com.example.onjeong.question;

import com.example.onjeong.Config.SecurityConfig;
import com.example.onjeong.Config.WebMvcConfig;
import com.example.onjeong.coin.domain.CoinHistoryType;
import com.example.onjeong.coin.service.CoinService;
import com.example.onjeong.notification.service.NotificationService;
import com.example.onjeong.question.controller.QuestionController;
import com.example.onjeong.question.domain.Answer;
import com.example.onjeong.question.dto.AnswerModifyRequestDto;
import com.example.onjeong.question.service.QuestionService;
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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = QuestionController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                ),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebMvcConfig.class
                )})
class QuestionControllerTest {

    @MockBean
    private QuestionService questionService;

    @MockBean
    private CoinService coinService;

    @MockBean
    private NotificationService notificationService;

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
    void 이주의질문확인() throws Exception {
        //given
        final String uri = "/questions";

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("Q001")));
    }

    @Test
    @WithMockUser
    void 이주의답변확인() throws Exception {
        //given
        final Long questionId = 1L;
        final String uri = "/answers/" + questionId;

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("Q002")));
    }

    @Test
    @WithMockUser
    void 답변한가족리스트() throws Exception {
        //given
        final String uri = "/answers-family";

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("Q003")));
    }


    @Nested
    class 답변등록{
        @Test
        @WithMockUser
        void 일반유저_답변등록() throws Exception {
            //given
            final String uri = "/answers/register";
            final String answerContent = "hi";
            doReturn(false).when(questionService).answerFamilyCheck();

            //when
            ResultActions resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(answerContent)
            );

            //then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", equalTo("Q004")));

            verify(coinService, times(1)).coinSave(CoinHistoryType.ANSWER, 10);
            verify(notificationService,times(1)).sendAnswer(isA(Answer.class));
            verify(notificationService,times(1)).sendFamilyCheck(isA(Answer.class));
        }


        @Test
        @WithMockUser
        void 마지막유저_답변등록() throws Exception {
            //given
            final String uri = "/answers/register";
            final String answerContent = "hi";
            doReturn(true).when(questionService).answerFamilyCheck();

            //when
            ResultActions resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(answerContent)
            );

            //then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", equalTo("Q004")));

            verify(coinService, times(1)).coinSave(CoinHistoryType.ANSWER, 210);
        }

    }

    @Test
    @WithMockUser
    void 답변수정() throws Exception {
        //given
        final String uri = "/answers";
        final AnswerModifyRequestDto answerModifyRequestDto = AnswerModifyRequestDto.builder()
                .answerContent("modified")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answerModifyRequestDto))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("Q005")));
    }

    @Test
    @WithMockUser
    void 답변삭제() throws Exception {
        //given
        final Long answerId = 1L;
        final String uri = "/answers/" + answerId;

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("Q006")));
    }

    public static <T> T isA(Class<T> type){
    return null;
}

}