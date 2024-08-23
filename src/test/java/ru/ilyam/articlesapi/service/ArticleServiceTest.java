package ru.ilyam.articlesapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.ilyam.articlesapi.dto.CreateArticleDto;
import ru.ilyam.articlesapi.dto.ReadArticleDto;
import ru.ilyam.articlesapi.entity.Article;
import ru.ilyam.articlesapi.entity.User;
import ru.ilyam.articlesapi.exception.ArticleNotFoundException;
import ru.ilyam.articlesapi.mapper.ArticleMapper;
import ru.ilyam.articlesapi.repository.ArticleRepository;
import ru.ilyam.articlesapi.service.impl.ArticleServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleMapper articleMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private ArticleServiceImpl articleService;

    private Article article;
    private CreateArticleDto createArticleDto;
    private ReadArticleDto readArticleDto;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("jim@mail.com");
        testUser.setPassword("password123");
        testUser.setNickname("jim");

        article = new Article();
        article.setId(1L);
        article.setTitle("test article");
        article.setText("test text");
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        article.setUser(testUser);

        createArticleDto = new CreateArticleDto();
        createArticleDto.setTitle("test article");
        createArticleDto.setText("test text");

        readArticleDto = new ReadArticleDto();
        readArticleDto.setNickname(article.getUser().getNickname());
        readArticleDto.setTitle(article.getTitle());
        readArticleDto.setText(article.getText());
        readArticleDto.setCreatedAt(article.getCreatedAt());
        readArticleDto.setUpdatedAt(article.getUpdatedAt());
    }

    @Test
    void postArticle_whenValidData_thenCreateArticle() {
        when(articleMapper.toEntity(createArticleDto)).thenReturn(article);
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(articleRepository.save(article)).thenReturn(article);

        articleService.postArticle(createArticleDto);

        verify(articleMapper).toEntity(createArticleDto);
        verify(articleRepository).save(article);
        assertThat(article.getUser()).isEqualTo(testUser);
    }

    @Test
    void findAll_whenCalled_thenReturnArticles() {
        Page<Article> articlePage = new PageImpl<>(List.of(article));
        when(articleRepository.findAll(any(Pageable.class))).thenReturn(articlePage);
        when(articleMapper.toReadArticleDto(article)).thenReturn(readArticleDto);

        Page<ReadArticleDto> result = articleService.findAll(Pageable.unpaged());

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("test article");
    }

    @Test
    void findById_whenArticleExists_thenReturnsArticle() {
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));
        when(articleMapper.toReadArticleDto(article)).thenReturn(readArticleDto);

        ReadArticleDto result = articleService.findById(article.getId());

        assertThat(result).isNotNull();
        assertThat(result.getNickname()).isEqualTo(article.getUser().getNickname());
        assertThat(result.getTitle()).isEqualTo("test article");
    }

    @Test
    void findById_whenArticleDoesNotExist_thenThrowsArticleNotFoundException() {
        when(articleRepository.findById(50L)).thenReturn(Optional.empty());
        assertThrows(ArticleNotFoundException.class, () ->
                articleService.findById(50L));
    }

    @Test
    void update_whenArticleExists_thenReturnsUpdatedArticle() {
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        CreateArticleDto updateDto = new CreateArticleDto();
        updateDto.setTitle("new title");

        articleService.update(article.getId(), updateDto);

        assertThat(article).isNotNull();
        assertThat(article.getTitle()).isEqualTo("new title");
        verify(articleRepository).save(article);
    }

    @Test
    void update_whenArticleDoesNotExist_thenThrowsArticleNotFoundException() {
        CreateArticleDto updateDto = new CreateArticleDto();
        updateDto.setTitle("new title");

        when(articleRepository.findById(article.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> articleService.update(article.getId(), updateDto))
                .isInstanceOf(ArticleNotFoundException.class)
                .hasMessageContaining("No article with this id was found");
    }

    @Test
    void removeArticleById_whenArticleExists_thenRemovesArticle() {
        doNothing().when(articleRepository).deleteById(article.getId());

        articleService.removeArticleById(article.getId());

        verify(articleRepository).deleteById(article.getId());
    }
}