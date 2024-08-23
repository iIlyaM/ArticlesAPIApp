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
        Map<LocalDateTime, Long> articleCountReportMap = new LinkedHashMap<>();
        LocalDateTime endDate = LocalDateTime.now().with(LocalTime.MAX);

        for (int i = 0; i < 7; i++) {
            LocalDateTime startDate = endDate.with(LocalTime.MIN);
            Long count = articleRepository.countPublishedArticlesBetweenDates(startDate, endDate);
            articleCountReportMap.put(endDate, count);
            endDate = startDate.minusSeconds(1);
        }

        return new DailyArticleCountResponse(articleCountReportMap);
    }
}

