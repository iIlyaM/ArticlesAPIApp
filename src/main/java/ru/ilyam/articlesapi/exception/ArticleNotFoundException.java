package ru.ilyam.articlesapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ArticleNotFoundException extends RuntimeException {
    private Integer code;
    private String message;
}
