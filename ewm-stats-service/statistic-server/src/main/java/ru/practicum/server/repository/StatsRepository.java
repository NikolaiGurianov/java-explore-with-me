package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import ru.practicum.server.model.EndpointHit;
import ru.practicum.server.model.ViewStat;

import java.time.LocalDateTime;
import java.util.List;

@Component
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.server.model.ViewStat(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE h.uri IN :uris AND h.timestamp BETWEEN :start and :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<ViewStat> getViewStatsWithUniqueIp(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("SELECT new ru.practicum.server.model.ViewStat(h.app, h.uri, COUNT(h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE h.uri IN :uris AND h.timestamp BETWEEN :start and :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStat> getViewStatsWithAllIp(LocalDateTime start, LocalDateTime end, String[] uris);
}