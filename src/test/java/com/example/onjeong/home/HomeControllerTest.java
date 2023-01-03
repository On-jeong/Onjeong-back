package com.example.onjeong.home;

import com.example.onjeong.Config.SecurityConfig;
import com.example.onjeong.Config.WebMvcConfig;
import com.example.onjeong.home.controller.HomeController;
import com.example.onjeong.coin.service.CoinService;
import com.example.onjeong.home.service.HomeService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HomeController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                ),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebMvcConfig.class
                )})
class HomeControllerTest {

    @MockBean
    private CoinService coinService;

    @MockBean
    private HomeService homeService;

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
    void 현재꽃종류() throws Exception {
        //given
        final String uri = "/flowers";

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("H001")));
    }

    @Test
    @WithMockUser
    void 만개한꽃리스트() throws Exception {
        //given
        final String uri = "/flowers-bloom";

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("H002")));
    }

    @Test
    @WithMockUser
    void 코인적립내역() throws Exception {
        //given
        final String uri = "/histories";

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("H003")));
    }


    @Test
    @WithMockUser
    void 코인개수() throws Exception {
        //given
        final String uri = "/coins";

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("H004")));
    }

//    @Test
//    @WithMockUser
//    void 랜덤코인지급() throws Exception {
//        //given
//        final String uri = "/coins-random";
//
//        double randAmount = 0.1;
//        List<CoinHistoryDto> coinHistoryDtoList = new ArrayList<>();
//
//        doReturn(randAmount).when(mock(Math.class)).random();
//        doReturn(coinHistoryDtoList).when(coinService).coinSave(CoinHistoryType.RAND, (int) (randAmount * (100 - 10 + 1)) + 10);
//
//        //when
//        ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders.post(uri)
//                        .contentType(MediaType.APPLICATION_JSON)
//        );
//
//        //then
//        resultActions.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code", equalTo("H005")));
//    }
//
}