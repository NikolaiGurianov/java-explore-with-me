package ru.practicum.mainservice.service.events.admin;

import ru.practicum.mainservice.dto.eventDto.EventFullDto;
import ru.practicum.mainservice.dto.eventDto.UpdateEventAdminRequest;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.statuses.State;

import java.time.LocalDateTime;
import java.util.List;

public interface AdministratorEventService {
    List<EventFullDto> getEventsByParametersAdm(List<Long> users, List<State> states, List<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto editEventAdm(Long compId, UpdateEventAdminRequest updateEventAdminRequest);

    EventFullDto get(Long eventId);

    Event getEntity(Long eventId);
}