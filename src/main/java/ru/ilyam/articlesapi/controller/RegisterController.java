package ru.ilyam.articlesapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ilyam.articlesapi.dto.RegisterRequestDto;
import ru.ilyam.articlesapi.dto.UserResponseDto;
import ru.ilyam.articlesapi.service.UserService;

@RestController
@RequestMapping("/api/v1/register-management")
@RequiredArgsConstructor
public class RegisterController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody RegisterRequestDto requestDto) {
        return ResponseEntity.ok(userService.create(requestDto));
    }
}
