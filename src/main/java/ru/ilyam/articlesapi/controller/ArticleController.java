package ru.ilyam.articlesapi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ilyam.articlesapi.dto.CreateArticleDto;
import ru.ilyam.articlesapi.dto.ReadArticleDto;
import ru.ilyam.articlesapi.service.ArticleService;

@RestController
@RequestMapping("/api/v1/article-management")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/articles/article")
    public ResponseEntity<HttpStatus> create(
            @Valid @RequestBody CreateArticleDto createArticleDto
    ) {
        articleService.postArticle(createArticleDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/article/{id}")
    public ResponseEntity<ReadArticleDto> getUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(articleService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/article/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        articleService.removeArticleById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/article/{id}")
    public ResponseEntity<ReadArticleDto> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody CreateArticleDto createArticleDto
    ) {
        return new ResponseEntity<>(articleService.update(id, createArticleDto), HttpStatus.OK);
    }

    @GetMapping("/articles")
    public ResponseEntity<Page<ReadArticleDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0") @Min(0) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") @Min(1) Integer pageSize
    ) {
        return new ResponseEntity<>(articleService.findAll(PageRequest.of(pageNumber, pageSize)), HttpStatus.OK);
    }

    @GetMapping("/author/{id}/articles")
    public ResponseEntity<Page<ReadArticleDto>> getAllByAuthorId(
            @PathVariable("id") Long id,
            @RequestParam(value = "pageNumber", defaultValue = "0") @Min(0) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") @Min(1) Integer pageSize
    ) {
        return new ResponseEntity<>(articleService
                .findByAuthorId(id, PageRequest.of(pageNumber, pageSize)), HttpStatus.OK);
    }
}
