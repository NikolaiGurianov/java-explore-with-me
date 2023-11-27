package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.dto.HitDto;
import ru.practicum.server.exception.ValidException;
import ru.practicum.server.mapper.HitMapper;
import ru.practicum.server.model.EndpointHit;
import ru.practicum.server.model.ViewStat;
import ru.practicum.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {
    @Mock
    private StatsRepository statsRepository;

    @Mock
    private HitMapper hitMapper;

    @InjectMocks
    private StatsServiceImpl statsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        statsService = new StatsServiceImpl(statsRepository, hitMapper);
    }

    @Test
    void addHit() {
        HitDto hitDto = new HitDto("app", "uri", "ip", "2023-01-23 21:22:21");
        EndpointHit endpointHit = new EndpointHit();
        when(hitMapper.toHit(hitDto)).thenReturn(endpointHit);
        when(statsRepository.save(endpointHit)).thenReturn(endpointHit);
        when(hitMapper.toHitDto(endpointHit)).thenReturn(hitDto);

        HitDto result = statsService.add(hitDto);

        assertNotNull(result);
        verify(hitMapper, times(1)).toHit(hitDto);
        verify(statsRepository, times(1)).save(endpointHit);
        verify(hitMapper, times(1)).toHitDto(endpointHit);
    }

    @Test
    void getStatistics_Unique() {
        String start = "2023-01-01 00:00:00";
        String end = "2023-01-02 00:00:00";
        String[] uris = {"uri1", "uri2"};
        LocalDateTime startDateTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDateTime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        when(statsRepository.getViewStatsWithUniqueIp(startDateTime, endDateTime, uris))
                .thenReturn(Arrays.asList(new ViewStat()));

        List<ViewStat> result = statsService.getStatistic(start, end, uris, true);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(statsRepository, times(1))
                .getViewStatsWithUniqueIp(startDateTime, endDateTime, uris);
    }

    @Test
    void getStatistics_All() {
        String start = "2023-01-01 00:00:00";
        String end = "2023-01-02 00:00:00";
        String[] uris = {"uri1", "uri2"};
        LocalDateTime startDateTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDateTime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        when(statsRepository.getViewStatsWithAllIp(startDateTime, endDateTime, uris))
                .thenReturn(Arrays.asList(new ViewStat()));

        List<ViewStat> result = statsService.getStatistic(start, end, uris, false);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(statsRepository, times(1)).getViewStatsWithAllIp(startDateTime, endDateTime, uris);
    }

    @Test
    void getStatistics_InvalidTimeRange() {
        String start = "2023-01-02 00:00:00";
        String end = "2023-01-01 00:00:00";
        String[] uris = {"uri1", "uri2"};

        assertThrows(ValidException.class, () -> statsService.getStatistic(start, end, uris, false));
    }
}