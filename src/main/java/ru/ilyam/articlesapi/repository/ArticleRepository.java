package ru.ilyam.articlesapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ilyam.articlesapi.dto.DailyArticleCountDto;
import ru.ilyam.articlesapi.entity.Article;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findAll(Pageable pageable);

    @Query("SELECT a FROM Article a JOIN a.user u WHERE u.nickname = :nickname")
    Page<Article> findAllByAuthorNickname(@Param("nickname") String nickname, Pageable pageable);

    @Query("SELECT a FROM Article a JOIN a.user u WHERE u.id = :user_id")
    Page<Article> findAllByAuthorId(@Param("user_id") Long id, Pageable pageable);

    @Query("SELECT COUNT(a) FROM Article a WHERE a.createdAt >= :startDate AND a.createdAt <= :endDate")
    Long countPublishedArticlesBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new ru.ilyam.articlesapi.dto.DailyArticleCountDto(a.createdAt, COUNT(a)) FROM Article a " +
            "WHERE a.createdAt >= :startDate AND a.createdAt <= :endDate " +
            "GROUP BY a.createdAt " +
            "ORDER BY a.createdAt")
    List<DailyArticleCountDto> getArticlesCountByCreateDate(@Param("startDate") LocalDateTime startDate,
                                                            @Param("endDate") LocalDateTime endDate);
}
