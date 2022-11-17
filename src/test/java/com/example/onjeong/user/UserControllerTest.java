package com.example.onjeong.user;

import com.example.onjeong.result.ResultCode;
import com.example.onjeong.result.ResultResponse;
import com.example.onjeong.user.controller.UserController;
import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void 회원탈퇴_성공() throws Exception{
        //given
        final String url = "/home";

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk());
    }

}
/*
    @Test
    void signUpSuccess() throws Exception {
        // given
        SignUpRequest request = signUpRequest();
        UserResponse response = userResponse();

        doReturn(response).when(userService)
                .signUp(any(SignUpRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("email", response.getEmail()).exists())
                .andExpect(jsonPath("pw", response.getPw()).exists())
                .andExpect(jsonPath("role", response.getRole()).exists())
    }

    private SignUpRequest signUpRequest() {
        return SignUpRequest.builder()
                .email("test@test.test")
                .pw("test")
                .build();
    }

    private UserResponse userResponse() {
        return UserResponse.builder()
                .email("test@test.test")
                .pw("test")
                .role(UserRole.ROLE_USER)
                .build();
    }

 */

