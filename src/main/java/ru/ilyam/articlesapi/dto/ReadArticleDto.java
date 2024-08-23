package ru.ilyam.articlesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadArticleDto {
    private String title;
    private String text;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}