package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.server.model.ViewStat;
import ru.practicum.server.service.StatsService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public HitDto add(@RequestBody HitDto hitDto) {
        log.info("Processing request to add hit.");
        return statsService.add(hitDto);
    }

    @GetMapping("/stats")
    public List<ViewStat> getStatistics(@RequestParam String start,
                                        @RequestParam String end,
                                        @RequestParam(required = false) String[] uris,
                                        @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Processing request to get statistics.");
        return statsService.getStatistic(start, end, uris, unique);
    }
}