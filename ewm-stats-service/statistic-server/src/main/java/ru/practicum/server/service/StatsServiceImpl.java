package ru.practicum.server.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDto;
import ru.practicum.server.exception.ValidException;
import ru.practicum.server.mapper.HitMapper;
import ru.practicum.server.model.EndpointHit;
import ru.practicum.dto.ViewStat;
import ru.practicum.server.repository.StatsRepository;

import java.time.LocalDateTime;
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
    public List<ViewStat> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end)) {
            log.error("Invalid time range. 'start' should be before 'end'.");
            throw new ValidException("Invalid time range. 'start' should be before 'end'.");
        }
        if (uris == null) {
            if (unique) {
                return statsRepository.getViewStatisticsWithUniqueIpAllUris(start, end);
            } else {
                return statsRepository.getViewStatisticsWithAllIpAllUris(start, end);
            }
        } else {
            if (unique) {
                log.info("Getting statistics with unique IP addresses");
                return statsRepository.getViewStatsWithUniqueIp(start, end, uris);
            } else {
                log.info("Getting statistics with all IP addresses");
                return statsRepository.getViewStatsWithAllIp(start, end, uris);
            }
        }
    }
}