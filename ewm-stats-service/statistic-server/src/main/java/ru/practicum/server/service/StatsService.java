package ru.practicum.server.service;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    HitDto add(HitDto hitDto);

    List<ViewStat> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}