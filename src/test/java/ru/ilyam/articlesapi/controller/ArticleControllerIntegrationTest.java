package ru.ilyam.articlesapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import ru.ilyam.articlesapi.ArticlesApiApplication;
import ru.ilyam.articlesapi.dto.CreateArticleDto;
import ru.ilyam.articlesapi.entity.Article;
import ru.ilyam.articlesapi.entity.User;
import ru.ilyam.articlesapi.repository.ArticleRepository;
import ru.ilyam.articlesapi.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ArticlesApiApplication.class)
@AutoConfigureMockMvc
class ArticleControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("jim@mail.com");
        testUser.setPassword("password123");
        testUser.setNickname("jim");
        userRepository.save(testUser);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testUser.getEmail(), testUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void reset() {
        articleRepository.deleteAll();
        userRepository.deleteAll();
        SecurityContextHolder.clearContext();
    }

    @Test
    void createArticle_whenValidData_thenStatusCreated() throws Exception {
        CreateArticleDto requestDto = new CreateArticleDto();
        requestDto.setTitle("test");
        requestDto.setText("test article.");

        mvc.perform(post("/api/v1/article-management/articles/article")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createArticle_whenValidData_thenStatusBagRequest() throws Exception {
        CreateArticleDto requestDto = new CreateArticleDto();
        requestDto.setTitle("test");
        requestDto.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut" +
                " labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi" +
                " ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse " +
                "cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa " +
                "qui officia deserunt mollit anim id est laborum. Sed ut perspiciatis unde omnis iste natus error sit " +
                "voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore " +
                "veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia " +
                "voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione " +
                "voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur," +
                " adipisci velit...\n" +
                "Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni" +
                " dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor" +
                " sit amet, consectetur, adipisci velit..");

        mvc.perform(post("/api/v1/article-management/articles/article")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getArticle_whenArticleExists_thenStatusOk() throws Exception {
        Article article = new Article();
        article.setTitle("article");
        article.setText("article.");
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        article.setUser(testUser);

        Article savedArticle = articleRepository.save(article);

        mvc.perform(get("/api/v1/article-management/article/{id}", savedArticle.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("article")))
                .andExpect(jsonPath("$.text", is("article.")));
    }

    @Test
    void getArticle_whenArticleIsNotExists_thenStatusNotFound() throws Exception {
        mvc.perform(get("/api/v1/article-management/article/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllArticles_whenGetArticles_thenStatusOk() throws Exception {
        Article article1 = new Article();
        article1.setTitle("first article");
        article1.setText("article 1");
        article1.setCreatedAt(LocalDateTime.now());
        article1.setUpdatedAt(LocalDateTime.now());
        article1.setUser(testUser);

        Article article2 = new Article();
        article2.setTitle("second article");
        article2.setText("article 2");
        article2.setCreatedAt(LocalDateTime.now());
        article2.setUpdatedAt(LocalDateTime.now());
        ;
        article2.setUser(testUser);

        articleRepository.saveAll(List.of(article1, article2));

        mvc.perform(get("/api/v1/article-management/articles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].title", is("first article")))
                .andExpect(jsonPath("$.content[1].text", is("article 2")));
    }

    @Test
    void deleteArticle_whenArticleExists_thenStatusNoContent() throws Exception {
        Article article = new Article();
        article.setTitle("article");
        article.setText("article.");
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        article.setUser(testUser);
        Article savedArticle = articleRepository.save(article);

        mvc.perform(delete("/api/v1/article-management/article/{id}", savedArticle.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateArticle_whenValidData_thenStatusOk() throws Exception {
        Article article = new Article();
        article.setTitle("article");
        article.setText("article.");
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        article.setUser(testUser);
        Article savedArticle = articleRepository.save(article);

        CreateArticleDto requestDto = new CreateArticleDto();
        requestDto.setTitle("New Title");
        requestDto.setText("New TEXT");

        mvc.perform(patch("/api/v1/article-management/article/{id}", savedArticle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("New Title")))
                .andExpect(jsonPath("$.text", is("New TEXT")));
    }

    @Test
    void getAllByAuthorId_whenAuthorHasArticles_thenStatusOk() throws Exception {
        Article article = new Article();
        article.setTitle("article");
        article.setText("article.");
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        article.setUser(testUser);
        articleRepository.save(article);

        mvc.perform(get("/api/v1/article-management/author/{id}/articles", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}