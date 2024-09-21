package ru.ilyam.articlesapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ilyam.articlesapi.dto.DailyArticleCountDto;
import ru.ilyam.articlesapi.dto.DailyArticleCountResponse;
import ru.ilyam.articlesapi.repository.ArticleRepository;
import ru.ilyam.articlesapi.service.StatisticsService;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final ArticleRepository articleRepository;
    private static final Integer DAYS_INTERVAL = 7;


    @Override
    public DailyArticleCountResponse getArticlesCount() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(DAYS_INTERVAL);
        Map<LocalDateTime, Long> articlesCountMap = new LinkedHashMap<>();
        List<DailyArticleCountDto> articleCountList = articleRepository.getArticlesCountByCreateDate(startDate, LocalDateTime.now());
        for(var entry: articleCountList) {
            articlesCountMap.put(entry.getPublishDate(), entry.getArticleCount());
        }
        return new DailyArticleCountResponse(articlesCountMap);
    }
}

