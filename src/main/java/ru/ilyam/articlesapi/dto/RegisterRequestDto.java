package ru.ilyam.articlesapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {
    @Email
    private String email;
    @NotNull
    @Length(min = 8, max = 100)
    private String password;
    @NotNull
    private String nickname;
    private List<Long> roleIds;
}
