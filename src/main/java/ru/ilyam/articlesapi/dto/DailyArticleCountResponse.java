package ru.ilyam.articlesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyArticleCountResponse {
    private Map<LocalDateTime, Long> articleCountReportMap;
}
