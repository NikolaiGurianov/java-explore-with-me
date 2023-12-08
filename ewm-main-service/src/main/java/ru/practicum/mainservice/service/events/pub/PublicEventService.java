package ru.practicum.mainservice.service.events.pub;

import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.statuses.SortEvent;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {
    List<EventFullDto> getEventsPub(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, Boolean onlyAvailable, SortEvent sort, int from, int size,
                                    HttpServletRequest httpServletRequest);

    EventFullDto getEventByIdPub(Long id, HttpServletRequest httpServletReque);
}
