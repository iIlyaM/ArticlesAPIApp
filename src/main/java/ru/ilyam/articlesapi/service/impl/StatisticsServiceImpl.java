package ru.ilyam.articlesapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ilyam.articlesapi.dto.DailyArticleCountResponse;
import ru.ilyam.articlesapi.repository.ArticleRepository;
import ru.ilyam.articlesapi.service.StatisticsService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final ArticleRepository articleRepository;


    @Override
    public DailyArticleCountResponse getArticlesCount() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(6);
        Map<LocalDateTime, Long> articlesCountMap = new LinkedHashMap<>();
        for(var entry: articleRepository.getArticlesCountByCreateDate(startDate, LocalDateTime.now())) {
            articlesCountMap.put(entry.getPublishDate(), entry.getArticleCount());
        }
        return new DailyArticleCountResponse(articlesCountMap);
    }
}

