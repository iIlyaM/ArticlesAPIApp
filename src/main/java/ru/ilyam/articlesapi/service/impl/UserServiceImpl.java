package ru.ilyam.articlesapi.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ilyam.articlesapi.dto.RegisterRequestDto;
import ru.ilyam.articlesapi.dto.UserResponseDto;
import ru.ilyam.articlesapi.entity.User;
import ru.ilyam.articlesapi.exception.UserNotFoundException;
import ru.ilyam.articlesapi.mapper.UserMapper;
import ru.ilyam.articlesapi.repository.PrivilegeRepository;
import ru.ilyam.articlesapi.repository.RoleRepository;
import ru.ilyam.articlesapi.repository.UserRepository;
import ru.ilyam.articlesapi.security.jwt.JwtService;
import ru.ilyam.articlesapi.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;


    @Override
    @Transactional
    public UserResponseDto create(RegisterRequestDto userCreateRequestDto) {
        User user = userMapper.toEntity(userCreateRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var userId = userRepository.save(user).getId();
        roleRepository.saveUserRoles(userId, userCreateRequestDto.getRoleIds());
        jwtService.generateToken(user);
        return userMapper.toUserResponseDto(user);
    }

    @Override
    public Page<UserResponseDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toUserResponseDto);
    }

    @Override
    public UserResponseDto findByEmail(String email) {
        return userMapper.toUserResponseDto(userRepository.findByEmail(email).orElseThrow(() ->
        new UserNotFoundException(HttpStatus.NOT_FOUND.value(), "Пользователь с такой почтой не найден")));
    }

    @Override
    public UserResponseDto findById(Long id) {
        return userMapper.toUserResponseDto(userRepository.findById(id).orElse(null));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public UserResponseDto update(Long userId, RegisterRequestDto requestDto) {
        var updatedUser = userMapper.toEntity(requestDto);
        updatedUser.setId(userId);
        userRepository.deleteUserRolesByUserId(userId);
        userRepository.save(updatedUser);
        roleRepository.saveUserRoles(userId, requestDto.getRoleIds());
        return userMapper.toUserResponseDto(updatedUser);
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username).orElseThrow(() ->
                new UserNotFoundException(HttpStatus.NOT_FOUND.value(), "Пользователь с такой почтой не найден"));
    }



}
