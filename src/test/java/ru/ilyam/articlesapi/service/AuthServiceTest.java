package ru.ilyam.articlesapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import ru.ilyam.articlesapi.dto.AuthRequestDto;
import ru.ilyam.articlesapi.dto.AuthResponseDto;
import ru.ilyam.articlesapi.entity.User;
import ru.ilyam.articlesapi.exception.AppUserAuthenticationException;
import ru.ilyam.articlesapi.exception.UserNotFoundException;
import ru.ilyam.articlesapi.repository.UserRepository;
import ru.ilyam.articlesapi.security.jwt.JwtService;
import ru.ilyam.articlesapi.service.impl.AuthenticationServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private User user;
    private AuthRequestDto authRequestDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("jim@mail.com");
        user.setPassword("password123");

        authRequestDto = new AuthRequestDto();
        authRequestDto.setEmail("jim@mail.com");
        authRequestDto.setPassword("password123");
    }

    @Test
    void authenticate_whenValidCredentials_thenReturnsAuthResponseDto() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        AuthResponseDto result = authenticationService.authenticate(authRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo("jwtToken");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void authenticate_whenInvalidCredentials_thenThrowsAppUserAuthenticationException() {
        AuthRequestDto testAuthRequestDto = new AuthRequestDto("gfhjghf@mail.com", "123123123");
        doThrow(new AuthenticationException("Invalid credentials") {})
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(AppUserAuthenticationException.class, () ->
                authenticationService.authenticate(testAuthRequestDto));
    }

    @Test
    void authenticate_whenUserNotFound_thenThrowsUserNotFoundException() {
        AuthRequestDto testAuthRequestDto = new AuthRequestDto("gfhjghf@mail.com", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(testAuthRequestDto.getEmail(), testAuthRequestDto.getPassword()));

        when(userRepository.findByEmail(testAuthRequestDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                authenticationService.authenticate(testAuthRequestDto));
    }
}