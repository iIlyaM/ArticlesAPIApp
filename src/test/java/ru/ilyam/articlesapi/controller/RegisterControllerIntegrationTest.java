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
import ru.ilyam.articlesapi.dto.RegisterRequestDto;
import ru.ilyam.articlesapi.repository.UserRepository;
import ru.ilyam.articlesapi.service.UserService;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ArticlesApiApplication.class)
@AutoConfigureMockMvc
public class RegisterControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void reset() {
        userRepository.deleteAll();
    }

    @Test
    void register_whenValidData_thenStatusCreated() throws Exception {
        RegisterRequestDto requestDto = new RegisterRequestDto();
        requestDto.setEmail("john@mail.com");
        requestDto.setPassword("pass12345678");
        requestDto.setNickname("john");
        requestDto.setRoleIds(List.of(1L));

        mvc.perform(post("/api/v1/register-management/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is("john@mail.com")))
                .andExpect(jsonPath("$.nickname", is("john")));
    }

    @Test
    void register_whenInvalidData_thenStatusBadRequest() throws Exception {
        RegisterRequestDto requestDto = new RegisterRequestDto();
        requestDto.setEmail("john@mail.com");
        requestDto.setPassword("pass12345678");
        requestDto.setRoleIds(List.of(1L));

        mvc.perform(post("/api/v1/register-management/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}
