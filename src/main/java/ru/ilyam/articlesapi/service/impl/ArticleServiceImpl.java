package ru.ilyam.articlesapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.ilyam.articlesapi.dto.CreateArticleDto;
import ru.ilyam.articlesapi.dto.ReadArticleDto;
import ru.ilyam.articlesapi.entity.Article;
import ru.ilyam.articlesapi.exception.ArticleNotFoundException;
import ru.ilyam.articlesapi.mapper.ArticleMapper;
import ru.ilyam.articlesapi.repository.ArticleRepository;
import ru.ilyam.articlesapi.service.ArticleService;
import ru.ilyam.articlesapi.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final UserService userService;

    @Override
    public void postArticle(CreateArticleDto articleDto) {
        var article = articleMapper.toEntity(articleDto);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        article.setUser(userService.getCurrentUser());
        articleRepository.save(article);
    }

    @Override
    public Page<ReadArticleDto> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable).map(articleMapper::toReadArticleDto);
    }

    @Override
    public Page<ReadArticleDto> findByAuthorId(Long id, Pageable pageable) {
        return articleRepository.findAllByAuthorId(id, pageable)
                .map(articleMapper::toReadArticleDto);
    }

    @Override
    public ReadArticleDto findById(Long id) {
        return articleMapper.toReadArticleDto(articleRepository
                .findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(HttpStatus.NOT_FOUND.value(),
                        "No article with this id was found")));
    }

    @Override
    public Page<ReadArticleDto> findByAuthorNickname(String nickname, Pageable pageable) {
        return articleRepository
                .findAllByAuthorNickname(nickname, pageable)
                .map(articleMapper::toReadArticleDto);
    }

    @Override
    public void removeArticleById(Long id) {
        articleRepository.deleteById(id);
    }

    @Override
    public ReadArticleDto update(Long id, CreateArticleDto articleDto) {
        Optional<Article> optionalArticle = articleRepository.findById(id);

        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();

            if (articleDto.getTitle() != null) {
                article.setTitle(articleDto.getTitle());
            }
            if (articleDto.getText() != null) {
                article.setText(articleDto.getText());
            }
            article.setUpdatedAt(LocalDateTime.now());
            articleRepository.save(article);
            return articleMapper.toReadArticleDto(article);
        } else {
            throw new ArticleNotFoundException(HttpStatus.NOT_FOUND.value(), "No article with this id was found: " + id);
        }
    }

}
