package ru.ilyam.articlesapi.service;

import ru.ilyam.articlesapi.dto.DailyArticleCountResponse;

import java.util.List;

public interface StatisticsService {

    DailyArticleCountResponse getArticlesCount();
}
