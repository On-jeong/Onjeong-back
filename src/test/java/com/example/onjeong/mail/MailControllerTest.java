package com.example.onjeong.mail;

import com.example.onjeong.Config.SecurityConfig;
import com.example.onjeong.Config.WebMvcConfig;
import com.example.onjeong.notification.service.NotificationService;
import com.example.onjeong.coin.domain.CoinHistoryType;
import com.example.onjeong.coin.service.CoinService;
import com.example.onjeong.mail.controller.MailController;
import com.example.onjeong.mail.domain.Mail;
import com.example.onjeong.mail.dto.MailRequestDto;
import com.example.onjeong.mail.service.MailService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MailController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                ),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebMvcConfig.class
                )})
class MailControllerTest {

    @MockBean
    private MailService mailService;

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
    void 메일전송() throws Exception {
        //given
        final MailRequestDto mailRequestDto = MailRequestDto.builder()
                .receiveUserId(1L)
                .mailContent("mail")
                .build();
        final String uri = "/mails";

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mailRequestDto))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("M001")));
        verify(coinService,times(1)).coinSave(any(CoinHistoryType.class),any(Integer.class));
        verify(notificationService,times(1)).sendMail(isA(Mail.class));
    }

    @Test
    @WithMockUser
    void 받은메일함확인() throws Exception {
        //given
        final String uri = "/mailList/receive";

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("M002")));


    }

    @Test
    @WithMockUser
    void 보낸메일함확인() throws Exception {
        //given
        final String uri = "/mailList/send";

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("M003")));
    }

    @Test
    @WithMockUser
    void 특정메일확인() throws Exception {
        final Long mailId = 1L;
        final String uri = "/mails/" + mailId;

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("M004")));
    }

    @Test
    @WithMockUser
    void 보낸메일삭제() throws Exception {
        //given
        final String uri = "/mails/send/delete";

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("mailIds", "1, 2, 3")
        );

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("M005")));
    }

    @Test
    @WithMockUser
    void 받은메일삭제() throws Exception {
        //given
        final String uri = "/mails/receive/delete";

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("mailIds", "1, 2, 3")
        );

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("M006")));
    }

    public static <T> T isA(Class<T> type){
        return null;
    }

}