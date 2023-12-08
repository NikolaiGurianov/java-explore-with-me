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
import ru.practicum.dto.ViewStatDto;
import ru.practicum.server.exception.ValidException;
import ru.practicum.server.mapper.HitMapper;
import ru.practicum.server.model.EndpointHit;
import ru.practicum.server.repository.StatsRepository;

import java.time.LocalDateTime;
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
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        List<String> uris = List.of("uri1", "uri2");

        when(statsRepository.getViewStatsWithUniqueIp(start, end, uris))
                .thenReturn(List.of(new ViewStatDto()));

        List<ViewStatDto> result = statsService.getStatistic(start, end, uris, true);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(statsRepository, times(1))
                .getViewStatsWithUniqueIp(start, end, uris);
    }

    @Test
    void getStatistics_All() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        List<String> uris = List.of("uri1", "uri2");

        when(statsRepository.getViewStatsWithAllIp(start, end, uris))
                .thenReturn(List.of(new ViewStatDto()));

        List<ViewStatDto> result = statsService.getStatistic(start, end, uris, false);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(statsRepository, times(1)).getViewStatsWithAllIp(start, end, uris);
    }

    @Test
    void getStatistics_InvalidTimeRange() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now();
        List<String> uris = List.of("uri1", "uri2");

        assertThrows(ValidException.class, () -> statsService.getStatistic(start, end, uris, false));
    }
}