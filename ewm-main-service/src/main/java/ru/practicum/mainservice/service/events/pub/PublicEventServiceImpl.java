package ru.practicum.mainservice.service.events.pub;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.HitDto;
import ru.practicum.mainservice.dto.eventDto.EventFullDto;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.exception.ValidException;
import ru.practicum.mainservice.mapper.EventMapper;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.statuses.SortEvent;
import ru.practicum.mainservice.repository.EventsRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

import static ru.practicum.mainservice.statuses.SortEvent.EVENT_DATE;
import static ru.practicum.mainservice.statuses.SortEvent.VIEWS;
import static ru.practicum.mainservice.statuses.State.PUBLISHED;
import static ru.practicum.util.Constants.*;

@Slf4j
@Data
@RequiredArgsConstructor
@Service
public class PublicEventServiceImpl implements PublicEventService {
    private final EventsRepository eventsRepository;
    private final StatsClient statsClient;

    @Override
    public List<EventFullDto> getEventsPub(String text, List<Long> categories, Boolean paid,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                           SortEvent sort, int from, int size, HttpServletRequest httpServletRequest) {
        log.info("Public: Запрос на получение событий с параметрами: text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        if (rangeEnd.isBefore(rangeStart)) {
            throw new ValidException("Значение времени 'Конец' раньше значения 'Начало'");
        }
        Pageable pageable;
        if (sort == VIEWS) {
            pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "views"));
        } else if (sort == EVENT_DATE) {
            pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "eventDate"));
        } else {
            pageable = PageRequest.of(from / size, size, SORT_BY_ID_ASC);
        }

        List<Event> foundEvents = eventsRepository
                .findAllByParametersPub(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, pageable);
        sendInfo(statsClient, httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr());
        List<EventFullDto> eventDtoList = new ArrayList<>();
        for (Event foundEvent : foundEvents) {
            foundEvent.setViews(getViewsEvent(statsClient, foundEvent));
            EventFullDto eventDto = EventMapper.toEventFullDto(foundEvent);
            eventDtoList.add(eventDto);
        }
        log.info("Public: Запрос выполнен. Получено {} событий", eventDtoList.size());

        return eventDtoList;
    }

    @Override
    public EventFullDto getEventByIdPub(Long eventId, HttpServletRequest httpServletRequest) {
        log.info("Public: Запрос на получение события по ID={}", eventId);
        Event event = eventsRepository.findByIdAndState(eventId, PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Событие с ID={} не найдено", eventId));
        sendInfo(statsClient, httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr());
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setViews(getViewsEvent(statsClient, event));
        log.info("Public: Запрос выполнен. Получено событие: {}", event);

        return eventFullDto;
    }

    private void sendInfo(StatsClient statsClient, String uri, String ip) {
        HitDto requestHitDto = new HitDto();
        requestHitDto.setApp("ewm-main-service");
        requestHitDto.setUri(uri);
        requestHitDto.setIp(ip);
        requestHitDto.setTimestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER));

        statsClient.postHit(requestHitDto);
    }

    public Long getViewsEvent(StatsClient statsClient, Event event) {
        if (event.getPublishedOn() == null) {
            return 0L;
        }

        String[] uris = {"/events/" + event.getId()};

        ResponseEntity<Object> response = statsClient.getStatistics(
                event.getPublishedOn().format(DATE_TIME_FORMATTER),
                LocalDateTime.now().format(DATE_TIME_FORMATTER),
                uris, true);
        if (response == null || !response.getStatusCode().is2xxSuccessful()) {
            throw new ValidException("Произошла ошибка при получении просмотров");
        }

        log.info("Response body: {}", response.getBody());
        List<Map<String, Integer>> stats = (ArrayList<Map<String, Integer>>) response.getBody();

        if (stats.size() != 0) {
            return Long.valueOf(stats.get(0).get("hits"));
        }
        return 0L;
    }
}