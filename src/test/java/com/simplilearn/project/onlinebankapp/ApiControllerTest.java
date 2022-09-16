package com.simplilearn.project.onlinebankapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplilearn.project.onlinebankapp.entities.Transfer;
import com.simplilearn.project.onlinebankapp.exceptions.AccessDeniedException;
import com.simplilearn.project.onlinebankapp.service.AccountService;
import com.simplilearn.project.onlinebankapp.service.CheckBookRequestService;
import com.simplilearn.project.onlinebankapp.service.TransactionService;
import com.simplilearn.project.onlinebankapp.service.UserService;
import com.simplilearn.project.onlinebankapp.utils.JwtTokenUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("uat")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = IcinOnlineBankAppApplication.class)
@RunWith(MockitoJUnitRunner.class)
@WithMockUser(roles = "ROLE_CUSTOMER")
class ApiControllerTest extends AbstractTestNGSpringContextTests {


    @Autowired
    private JwtTokenUtil jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired CheckBookRequestService checkBookRequestService;
    @Autowired TransactionService transactionService;
    @Autowired AccountService accountService;

    private String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2aXZpYW4iLCJpYXQiOjE2NjMyMjc5MjUsImV4cCI6MTY2MzMxNDMyNX0.UvSHosDBc6KPIejuS_l-eVGYA6kIF81_Hsu8iT1FxkD9WNXCMziBcrFCqG0AyxFO_IOl5K84Y5lPRpOFI9FHBA";


    private Transfer transferValidAccounts = Transfer.builder().sourceAccount("110012555521503353").destinationAccount("110024210100032421").amount(1000000).description("Transfer 1,000,000 from Acc 110012555521503353 to Acc 110024210100032421").build();


    private Transfer transferInValidAccounts = Transfer.builder().build();

    @Autowired private MockMvc mockMvc;

    @Test
    @DisplayName(value = "Fetch User Profile Test")
    public void userProfileTest() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/user/profile")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()));
    }

    @Test
    @DisplayName(value = "Fetch User Profile Without Token Test")
    public void userProfileWithoutTokenTest() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/user/profile")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName(value = "Fetch Cheque Book Requests Test")
    public void chequeBookRequests() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/check-book-request")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()));
    }

    @Test
    @DisplayName(value = "Fetch Transactions Lists Test")
    public void transactions() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/transaction")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()));
    }

    @Test
    @DisplayName(value = "Transfer Fund Test")
    public void transfer() throws Exception {
        mockMvc.perform(
                post("/api/account/transfer")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writer().writeValueAsString(transferValidAccounts))
                        .header(HttpHeaders.AUTHORIZATION,"Bearer " + token)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.message",Matchers.notNullValue()));
    }

    @Test
    @DisplayName(value = "Request Cheque Book Test")
    public void requestCheckBook() throws Exception {
        mockMvc.perform(
                post("/api/check-book-request")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION,"Bearer " + token)
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$",Matchers.notNullValue()))
                .andExpect(jsonPath("$.message",Matchers.equalToIgnoringCase("Check Book Request Sent Successfully")));
    }


}
