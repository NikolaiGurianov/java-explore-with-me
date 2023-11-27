package ru.practicum.server.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.server.model.ViewStat;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StatsRepositoryTest {

    @Autowired
    private StatsRepository statsRepository;

    @Test
    void testGetViewStatsWithUniqueIp() {
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now();
        String[] uris = {"uri1", "uri2"};

        List<ViewStat> result = statsRepository.getViewStatsWithUniqueIp(startDateTime, endDateTime, uris);

        assertNotNull(result);
    }

    @Test
    void testGetViewStatsWithAllIp() {
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now();
        String[] uris = {"uri1", "uri2"};

        List<ViewStat> result = statsRepository.getViewStatsWithAllIp(startDateTime, endDateTime, uris);

        assertNotNull(result);
    }

    @Test
    public void testGetViewStatisticsWithUniqueIpAllUris() {
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();

        List<ViewStat> result = statsRepository.getViewStatisticsWithUniqueIpAllUris(start, end);

        assertNotNull(result);
    }

    @Test
    public void testGetViewStatisticsWithAllIpAllUris() {
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();

        List<ViewStat> result = statsRepository.getViewStatisticsWithAllIpAllUris(start, end);

        assertNotNull(result);
    }
}