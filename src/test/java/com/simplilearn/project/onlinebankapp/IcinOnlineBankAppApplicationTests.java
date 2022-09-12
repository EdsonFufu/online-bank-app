package com.simplilearn.project.onlinebankapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplilearn.project.onlinebankapp.entities.LoginReq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class IcinOnlineBankAppApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    private LoginReq loginReqInvalid = new LoginReq("selenium","selenium");
    private LoginReq loginReqValid = new LoginReq("edson","edson");
    @Test
    public void loginWithValidCredentials() throws Exception {
        mockMvc.perform(post("/api/signin",new ObjectMapper().writer().writeValueAsString(loginReqValid))).andDo(print()).andExpect(status().isOk()).andExpect((ResultMatcher) content().string(containsString("jwt")));
    }

}
