package com.example.onjeong.anniversary;

import com.example.onjeong.Config.SecurityConfig;
import com.example.onjeong.Config.WebMvcConfig;
import com.example.onjeong.anniversary.controller.AnniversaryController;
import com.example.onjeong.anniversary.dto.AnniversaryRegisterDto;
import com.example.onjeong.anniversary.service.AnniversaryService;
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

import java.util.Random;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AnniversaryController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                ),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebMvcConfig.class
                )})
public class AnniversaryControllerTest {

    @MockBean
    private AnniversaryService anniversaryService;

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
    void 월별_모든_특수일정_가져오기() throws Exception {
        //given
        final String anniversaryDate= "2022-12-03";
        final String uri = "/months/anniversaries/" + anniversaryDate;


        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("A001")));
    }


    @Test
    @WithMockUser
    void 해당_일의_특수일정_가져오기() throws Exception {
        //given
        final String anniversaryDate= "2022-12-03";
        final String uri = "/days/anniversaries/" + anniversaryDate;


        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("A002")));
    }

    @Test
    @WithMockUser
    void 해당_일의_특수일정_등록하기() throws Exception {
        //given
        final String anniversaryDate= "2022-12-03";
        final String uri = "/days/anniversaries/" + anniversaryDate;
        final AnniversaryRegisterDto anniversaryRegisterDto= anniversaryRegisterDto("birthday","ANNIVERSARY");

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(anniversaryRegisterDto))
        );


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("A003")));
    }


    @Test
    @WithMockUser
    void 해당_일의_특수일정_삭제하기() throws Exception {
        //given
        final Long anniversaryId= 1L;
        final String uri = "/days/anniversaries/" + anniversaryId;
        final AnniversaryRegisterDto anniversaryRegisterDto= anniversaryRegisterDto("birthday","ANNIVERSARY");

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(anniversaryRegisterDto))
        );


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("A004")));
    }


    private AnniversaryRegisterDto anniversaryRegisterDto(String anniversaryContent, String anniversaryType){
        return AnniversaryRegisterDto.builder()
                .anniversaryContent(anniversaryContent)
                .anniversaryType(anniversaryType)
                .build();
    }
}
