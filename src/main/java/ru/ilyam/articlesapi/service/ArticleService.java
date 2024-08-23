package ru.ilyam.articlesapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.ilyam.articlesapi.dto.CreateArticleDto;
import ru.ilyam.articlesapi.dto.DailyArticleCountResponse;
import ru.ilyam.articlesapi.dto.ReadArticleDto;

public interface ArticleService {

    void postArticle(CreateArticleDto articleDto);

    Page<ReadArticleDto> findAll(Pageable pageable);

    Page<ReadArticleDto> findByAuthorId(Long id, Pageable pageable);

    ReadArticleDto findById(Long id);

    Page<ReadArticleDto> findByAuthorNickname(String nickname, Pageable pageable);

    void removeArticleById(Long id);

    ReadArticleDto update(Long id, CreateArticleDto articleDto);

}
