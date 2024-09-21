package ru.ilyam.articlesapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.ilyam.articlesapi.dto.RegisterRequestDto;
import ru.ilyam.articlesapi.dto.UserResponseDto;
import ru.ilyam.articlesapi.entity.User;

import java.util.List;

public interface UserService {
    Page<UserResponseDto> getAll(Pageable pageable);

    UserResponseDto findByEmail(String email);

    UserResponseDto findById(Long id);

    void delete(Long id);

    UserResponseDto update(Long userId, RegisterRequestDto requestDto);

    User getCurrentUser();
}
