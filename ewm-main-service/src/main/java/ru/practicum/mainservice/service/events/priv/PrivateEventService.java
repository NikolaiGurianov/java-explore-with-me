package ru.practicum.mainservice.service.events.priv;

import ru.practicum.mainservice.dto.eventDto.*;
import ru.practicum.mainservice.dto.requestDto.ParticipationRequestDto;

import java.util.List;

public interface PrivateEventService {
    List<EventShortDto> getEventsByUserIdPr(Long userId, int from, int size);

    EventFullDto getEventByIdPr(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequestsForEventPr(Long userId, Long eventId);

    EventFullDto addEventPr(Long userId, NewEventDto newEventDto);

    EventFullDto updateEventPr(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    EventRequestResponse changeStatusRequests(Long userId, Long eventId, EventRequest eventRequest);
}
