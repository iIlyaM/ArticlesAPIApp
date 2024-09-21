package ru.ilyam.articlesapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.ilyam.articlesapi.ArticlesApiApplication;
import ru.ilyam.articlesapi.dto.AuthRequestDto;
import ru.ilyam.articlesapi.dto.RegisterRequestDto;
import ru.ilyam.articlesapi.repository.UserRepository;
import ru.ilyam.articlesapi.service.UserService;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ArticlesApiApplication.class)
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void reset() {
        userRepository.deleteAll();
    }

    @Test
    void authenticate_whenValidCredentials_thenReturnStatusOk() throws Exception {
        AuthRequestDto authRequestDto = new AuthRequestDto();
        authRequestDto.setEmail("john@mail.com");
        authRequestDto.setPassword("pass12345678");

        userService.create(new RegisterRequestDto(
                "john@mail.com",
                "pass12345678",
                "john",
                List.of(1L)
        ));

        mvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void authenticate_whenInvalidCredentials_thenReturnUnauthorized() throws Exception {
        AuthRequestDto authRequestDto = new AuthRequestDto();
        authRequestDto.setEmail("john@mail.com");
        authRequestDto.setPassword("pass12345678");

        mvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequestDto)))
                .andExpect(status().isUnauthorized());
    }
}

