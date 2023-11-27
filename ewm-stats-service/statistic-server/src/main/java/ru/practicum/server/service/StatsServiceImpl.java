package ru.practicum.server.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDto;
import ru.practicum.server.exception.ValidException;
import ru.practicum.server.mapper.HitMapper;
import ru.practicum.server.model.EndpointHit;
import ru.practicum.server.model.ViewStat;
import ru.practicum.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Service
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final HitMapper hitMapper;

    @Override
    public HitDto add(HitDto hitDto) {
        EndpointHit hit = hitMapper.toHit(hitDto);
        return hitMapper.toHitDto(statsRepository.save(hit));
    }

    @Override
    public List<ViewStat> getStatistic(String start, String end, String[] uris, Boolean unique) {
        LocalDateTime startDateTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDateTime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (startDateTime.isAfter(endDateTime)) {
            log.error("Invalid time range. 'start' should be before 'end'.");
            throw new ValidException("Invalid time range. 'start' should be before 'end'.");
        }
        if (unique) {
            log.info("Getting statistics with unique IP addresses");
            return statsRepository.getViewStatsWithUniqueIp(startDateTime, endDateTime, uris);
        } else {
            log.info("Getting statistics with all IP addresses");
            return statsRepository.getViewStatsWithAllIp(startDateTime, endDateTime, uris);
        }
    }
}