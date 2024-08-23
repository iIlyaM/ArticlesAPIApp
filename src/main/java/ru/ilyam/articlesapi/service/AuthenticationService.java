package ru.ilyam.articlesapi.service;

import org.springframework.security.core.AuthenticationException;
import ru.ilyam.articlesapi.dto.AuthRequestDto;
import ru.ilyam.articlesapi.dto.AuthResponseDto;


public interface AuthenticationService {
    AuthResponseDto authenticate(AuthRequestDto authRequestDto) throws AuthenticationException;
}