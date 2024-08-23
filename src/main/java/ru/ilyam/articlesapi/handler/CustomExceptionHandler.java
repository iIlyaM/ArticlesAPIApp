package ru.ilyam.articlesapi.handler;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.ilyam.articlesapi.exception.AppUserAuthenticationException;
import ru.ilyam.articlesapi.exception.ArticleNotFoundException;
import ru.ilyam.articlesapi.exception.InvalidPrivilegesException;
import ru.ilyam.articlesapi.exception.UserNotFoundException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseBody
    @ExceptionHandler(AppUserAuthenticationException.class)
    public ProblemDetail handleSecurityException(AppUserAuthenticationException ex) {
        ProblemDetail errorDetail = ProblemDetail
                .forStatusAndDetail(HttpStatusCode.valueOf(ex.getCode()), ex.getMessage());
        return errorDetail;
    }

    @ResponseBody
    @ExceptionHandler(ArticleNotFoundException.class)
    public ProblemDetail handleSecurityException(ArticleNotFoundException ex) {
        ProblemDetail errorDetail = ProblemDetail
                .forStatusAndDetail(HttpStatusCode.valueOf(ex.getCode()), ex.getMessage());
        return errorDetail;
    }

    @ResponseBody
    @ExceptionHandler(InvalidPrivilegesException.class)
    public ProblemDetail handleSecurityException(InvalidPrivilegesException ex) {
        ProblemDetail errorDetail = ProblemDetail
                .forStatusAndDetail(HttpStatusCode.valueOf(ex.getCode()), ex.getMessage());
        return errorDetail;
    }

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleSecurityException(UserNotFoundException ex) {
        ProblemDetail errorDetail = ProblemDetail
                .forStatusAndDetail(HttpStatusCode.valueOf(ex.getCode()), ex.getMessage());
        return errorDetail;
    }
}
