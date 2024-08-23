package ru.ilyam.articlesapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateArticleDto {
    @NotNull
    private String title;
    @NotNull
    @Length(min = 1, max = 100)
    private String text;
}
