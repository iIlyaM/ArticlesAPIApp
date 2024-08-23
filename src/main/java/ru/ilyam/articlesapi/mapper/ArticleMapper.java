package ru.ilyam.articlesapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ilyam.articlesapi.dto.CreateArticleDto;
import ru.ilyam.articlesapi.dto.ReadArticleDto;
import ru.ilyam.articlesapi.entity.Article;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    Article toEntity(CreateArticleDto articleDto);

    Article toEntity(ReadArticleDto articleDto);

    CreateArticleDto toCreateArticleDto(Article article);

    @Mapping(source = "user.nickname", target = "nickname")
    ReadArticleDto toReadArticleDto(Article article);
}
