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
import ru.ilyam.articlesapi.entity.User;
import ru.ilyam.articlesapi.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ArticlesApiApplication.class)
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void reset() {
        repository.deleteAll();
    }

    @Test
    void getAll_whenGetEmployees_thenStatusOK() throws Exception {
        createTestUser("jim@mail.com", "12345678", "JIMM@");
        createTestUser("bob@mail.com", "12345678", "bob@");

        mvc.perform(get("/api/v1/user-management/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].nickname", is("JIMM@")))
                .andExpect(jsonPath("$.content[1].email", is("bob@mail.com")));
    }

    @Test
    void getAll_whenGetEmpty_thenStatusOK() throws Exception {
        mvc.perform(get("/api/v1/user-management/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    void getUser_whenGetUser_thenStatusOk() throws Exception {
        Long userId = createTestUser("john@mail.com", "password123", "jooohn");
        mvc.perform(get("/api/v1/user-management/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(userId.intValue())))
                .andExpect(jsonPath("$.email", is("john@mail.com")))
                .andExpect(jsonPath("$.nickname", is("jooohn")));
    }


    @Test
    void deleteUser_whenUserExists_thenStatusNoContent() throws Exception {
        Long userId = createTestUser("jim@mail.com", "12345678", "JIMM@");

        mvc.perform(delete("/api/v1/user-management/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertThat(repository.findById(userId)).isEmpty();
    }

    @Test
    void updateUser_whenValidData_thenStatusOk() throws Exception {
        Long userId = createTestUser("jim@mail.com", "12345678", "JIMM@");


        RegisterRequestDto requestDto = new RegisterRequestDto();
        requestDto.setEmail("jimBOOK@mail.com");
        requestDto.setPassword("12345678");
        requestDto.setNickname("JIM");

        mvc.perform(put("/api/v1/user-management/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(userId.intValue())))
                .andExpect(jsonPath("$.email", is("jimBOOK@mail.com")))
                .andExpect(jsonPath("$.nickname", is("JIM")));
    }

    @Test
    void updateUser_whenInvalidData_thenStatusBadRequest() throws Exception {
        Long userId = createTestUser("jim@mail.com", "12345678", "JIMM@");


        RegisterRequestDto requestDto = new RegisterRequestDto();
        requestDto.setEmail("jimBOOK.com");
        requestDto.setPassword("12345678");
        requestDto.setNickname("JIM");

        mvc.perform(put("/api/v1/user-management/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    private Long createTestUser(String email, String password, String nickname) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setNickname(nickname);
        var newUser = repository.save(user);
        return newUser.getId();
    }

}