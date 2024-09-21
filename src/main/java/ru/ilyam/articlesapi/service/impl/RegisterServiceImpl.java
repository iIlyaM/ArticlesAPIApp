package ru.ilyam.articlesapi.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ilyam.articlesapi.dto.RegisterRequestDto;
import ru.ilyam.articlesapi.dto.UserResponseDto;
import ru.ilyam.articlesapi.entity.User;
import ru.ilyam.articlesapi.mapper.UserMapper;
import ru.ilyam.articlesapi.repository.RoleRepository;
import ru.ilyam.articlesapi.repository.UserRepository;
import ru.ilyam.articlesapi.security.jwt.JwtService;
import ru.ilyam.articlesapi.service.RegisterService;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    @Override
    @Transactional
    public UserResponseDto register(RegisterRequestDto userCreateRequestDto) {
        User user = userMapper.toEntity(userCreateRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var userId = userRepository.save(user).getId();
        roleRepository.saveUserRoles(userId, userCreateRequestDto.getRoleIds());
        jwtService.generateToken(user);
        return userMapper.toUserResponseDto(user);
    }
}
