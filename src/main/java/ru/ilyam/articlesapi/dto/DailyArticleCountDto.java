package ru.ilyam.articlesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyArticleCountDto {
    private LocalDateTime publishDate;
    private Long articleCount;
}
