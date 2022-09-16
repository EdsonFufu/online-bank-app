package com.simplilearn.project.onlinebankapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplilearn.project.onlinebankapp.controllers.AuthController;
import com.simplilearn.project.onlinebankapp.entities.LoginReq;
import com.simplilearn.project.onlinebankapp.service.CustomUserDetailsService;
import com.simplilearn.project.onlinebankapp.service.UserService;
import com.simplilearn.project.onlinebankapp.utils.AuthTokenFilter;
import com.simplilearn.project.onlinebankapp.utils.JwtTokenUtil;
import groovy.util.logging.Slf4j;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = IcinOnlineBankAppApplication.class)
@RunWith(MockitoJUnitRunner.class)
class AuthControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenUtil jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private ObjectMapper om = new ObjectMapper();

    private LoginReq loginReqInvalid = new LoginReq("mockito","mockito");
    private LoginReq loginReqValid = new LoginReq("vivian","vivian");


    @Test
    @DisplayName(value = "Login With Valid Credentials")
    public void loginWithValidCredentials() throws Exception {

        mockMvc.perform(
                post("/api/auth/signin")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writer().writeValueAsString(loginReqValid))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("jwt")))
                .andExpect(jsonPath("$.jwt", Matchers.notNullValue()));
    }

    @Test
    @DisplayName(value = "Login With Invalid Credentials")
    public void loginWithInValidCredentials() throws Exception {

        mockMvc.perform(
                        post("/api/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writer().writeValueAsString(loginReqInvalid))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}
