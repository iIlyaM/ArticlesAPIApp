package ru.ilyam.articlesapi.service;

import ru.ilyam.articlesapi.dto.RegisterRequestDto;
import ru.ilyam.articlesapi.dto.UserResponseDto;

public interface RegisterService {
    UserResponseDto register(RegisterRequestDto user);
}
