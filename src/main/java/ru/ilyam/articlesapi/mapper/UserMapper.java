package ru.ilyam.articlesapi.mapper;

import org.mapstruct.Mapper;
import ru.ilyam.articlesapi.dto.RegisterRequestDto;
import ru.ilyam.articlesapi.dto.UserResponseDto;
import ru.ilyam.articlesapi.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    RegisterRequestDto toRegisterRequestDto(User user);

    User toEntity(RegisterRequestDto requestDto);

    UserResponseDto toUserResponseDto(User user);

    UserResponseDto toUserResponseDto(RegisterRequestDto registerRequestDto);
}
