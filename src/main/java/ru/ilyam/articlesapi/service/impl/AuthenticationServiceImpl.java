package ru.ilyam.articlesapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import ru.ilyam.articlesapi.dto.AuthRequestDto;
import ru.ilyam.articlesapi.dto.AuthResponseDto;
import ru.ilyam.articlesapi.exception.AppUserAuthenticationException;
import ru.ilyam.articlesapi.exception.UserNotFoundException;
import ru.ilyam.articlesapi.repository.UserRepository;
import ru.ilyam.articlesapi.security.jwt.JwtService;
import ru.ilyam.articlesapi.service.AuthenticationService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDto authenticate(AuthRequestDto authRequestDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequestDto.getEmail(),
                            authRequestDto.getPassword()
                    )
            );
            var user = userRepository.findByEmail(authRequestDto.getEmail()).orElseThrow(() ->
                    new UserNotFoundException(HttpStatus.NOT_FOUND.value(), "Пользователь с такой почтой не найден"));
            var jwtToken = jwtService.generateToken(user);
            return new AuthResponseDto(jwtToken);
        } catch (AuthenticationException ex) {
            throw new AppUserAuthenticationException(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        }
    }
}
