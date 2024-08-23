package ru.ilyam.articlesapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ilyam.articlesapi.dto.DailyArticleCountResponse;
import ru.ilyam.articlesapi.service.StatisticsService;

@RestController
@RequestMapping("/api/v1/stats-management")
@RequiredArgsConstructor
public class StatsController {
    private final StatisticsService statisticsService;

    @GetMapping("/articles/count")
    public ResponseEntity<DailyArticleCountResponse> getArticlesCountReport() {
        return new ResponseEntity<>(statisticsService.getArticlesCount(), HttpStatus.OK);
    }
}
