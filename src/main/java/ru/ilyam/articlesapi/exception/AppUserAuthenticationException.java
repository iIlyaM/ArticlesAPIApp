package ru.ilyam.articlesapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AppUserAuthenticationException extends RuntimeException {
    private Integer code;
    private String message;
}
