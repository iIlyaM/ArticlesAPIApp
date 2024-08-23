package ru.ilyam.articlesapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.ilyam.articlesapi.dto.RegisterRequestDto;
import ru.ilyam.articlesapi.dto.UserResponseDto;
import ru.ilyam.articlesapi.entity.User;
import ru.ilyam.articlesapi.exception.UserNotFoundException;
import ru.ilyam.articlesapi.mapper.UserMapper;
import ru.ilyam.articlesapi.repository.PrivilegeRepository;
import ru.ilyam.articlesapi.repository.RoleRepository;
import ru.ilyam.articlesapi.repository.UserRepository;
import ru.ilyam.articlesapi.security.jwt.JwtService;
import ru.ilyam.articlesapi.service.impl.UserServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PrivilegeRepository privilegeRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private RegisterRequestDto registerRequestDto;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("jim@mail.com");
        user.setPassword("password123");
        user.setNickname("Jim");

        registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setEmail("jim@mail.com");
        registerRequestDto.setPassword("password123");
        registerRequestDto.setRoleIds(List.of(1L));

        userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setNickname(user.getNickname());
    }

    @Test
    void create_whenValidData_thenReturnsUserResponseDto() {
        when(userMapper.toEntity(any(RegisterRequestDto.class))).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserResponseDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto result = userService.create(registerRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("jim@mail.com");
        verify(roleRepository).saveUserRoles(anyLong(), anyList());
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    void getAll_whenCalled_thenReturnsPageOfUsers() {
        Page<User> userPage = new PageImpl<>(Collections.singletonList(user));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);
        when(userMapper.toUserResponseDto(any(User.class))).thenReturn(userResponseDto);

        Page<UserResponseDto> result = userService.getAll(Pageable.unpaged());

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getEmail()).isEqualTo("jim@mail.com");
    }

    @Test
    void findByEmail_whenUserExists_thenReturnsUserResponseDto() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto result = userService.findByEmail(user.getEmail());

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("jim@mail.com");
    }

    @Test
    void findByEmail_whenUserDoesNotExist_thenThrowsUserNotFoundException() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.findByEmail(user.getEmail()));
    }

    @Test
    void findById_whenUserExists_thenReturnsUserResponseDto() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto result = userService.findById(user.getId());

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("jim@mail.com");
    }

    @Test
    void findById_whenUserDoesNotExist_thenReturnsNull() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        UserResponseDto result = userService.findById(user.getId());

        assertThat(result).isNull();
    }

    @Test
    void delete_whenUserExists_thenDeletesUser() {
        doNothing().when(userRepository).deleteById(user.getId());

        userService.delete(user.getId());

        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void update_whenValidData_thenReturnsUpdatedUserResponseDto() {
        when(userMapper.toEntity(any(RegisterRequestDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserResponseDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto result = userService.update(user.getId(), registerRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("jim@mail.com");
        verify(roleRepository).saveUserRoles(anyLong(), anyList());
    }
}