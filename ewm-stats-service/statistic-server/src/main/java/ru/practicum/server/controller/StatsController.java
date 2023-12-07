package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStat;
import ru.practicum.server.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.Constants.DATE_TIME_FORMAT;

@Slf4j
@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<HitDto> add(@RequestBody HitDto requestHitDto) {
        log.info("Processing request to add hit.");
        return new ResponseEntity<>(statsService.add(requestHitDto), HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public List<ViewStat> getStatistics(@RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime start,
                                        @RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime end,
                                        @RequestParam(required = false) List<String> uris,
                                        @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Processing request to get statistics.");
        return statsService.getStatistic(start, end, uris, unique);
    }
}