package ru.practicum.server.service;

import ru.practicum.dto.HitDto;
import ru.practicum.server.model.ViewStat;

import java.util.List;

public interface StatsService {
    HitDto add(HitDto hitDto);

    List<ViewStat> getStatistic(String start, String end, String[] uris, Boolean unique);
}
