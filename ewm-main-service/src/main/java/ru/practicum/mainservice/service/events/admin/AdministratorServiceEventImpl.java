package ru.practicum.mainservice.service.events.admin;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.UpdateEventAdminRequest;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.ValidException;
import ru.practicum.mainservice.mapper.EventMapper;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.repository.EventsRepository;
import ru.practicum.mainservice.service.categories.CategoriesService;
import ru.practicum.mainservice.service.events.pub.PublicEventServiceImpl;
import ru.practicum.mainservice.statuses.State;

import java.time.LocalDateTime;
import java.util.*;

import static ru.practicum.mainservice.statuses.State.*;
import static ru.practicum.util.Constants.*;

@Slf4j
@Data
@Service
@RequiredArgsConstructor
public class AdministratorServiceEventImpl implements AdministratorEventService {
    private final EventsRepository eventsRepository;
    private final CategoriesService categoryService;
    private final StatsClient statsClient;
    private final PublicEventServiceImpl publicEventService;

    @Override
    public List<EventFullDto> getEventsByParametersAdm(List<Long> users, List<State> states, List<Long> categories,
                                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        log.info("Public: Запрос на получение событий с параметрами: users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);

        List<Event> filterEvents = eventsRepository.findAllByParameters(users, states, categories,
                rangeStart, rangeEnd, PageRequest.of(from / size, size, SORT_BY_ID_ASC));
        List<EventFullDto> list = new ArrayList<>();
        filterEvents.forEach(event -> {
            event.setViews(publicEventService.getViewsEvent(statsClient, event));
            list.add(EventMapper.toEventFullDto(event));
        });
        log.info("Admin: Запрос выполнен. Получено {} событий", list.size());

        return list;
    }

    @Override
    public EventFullDto editEventAdm(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Admin: Запрос на обновление события с ID={}", eventId);
        Event event = getEntity(eventId);
        if (PENDING != event.getState()) {
            throw new ConflictException("Редактировать можно только ожидающие модерации события");
        }
        State state = getStateForEvent(event, updateEventAdminRequest.getStateAction());
        event.setState(state);

        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(categoryService.getEntity(updateEventAdminRequest.getCategory()));
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            if (updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new ValidException("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
            }
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        event.setPublishedOn(LocalDateTime.now());
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventsRepository.save(event));
        log.info("Admin: Запрос выполнен. Событие обновлено: {}", eventFullDto);
        return eventFullDto;
    }

    private State getStateForEvent(Event event, UpdateEventAdminRequest.StateAction action) {
        State result = CANCELED;
        if (action != null) {
            if (action.equals(UpdateEventAdminRequest.StateAction.PUBLISH_EVENT)) {
                result = PUBLISHED;
            } else if (action.equals(UpdateEventAdminRequest.StateAction.REJECT_EVENT) && event.getState().equals(PUBLISHED)) {
                throw new ConflictException("Невозможно отменить опубликованное событие!");
            }
        }
        return result;
    }

    @Override
    public EventFullDto get(Long eventId) {
        return EventMapper.toEventFullDto(getEntity(eventId));
    }

    @Override
    public Event getEntity(Long eventId) {
        return eventsRepository.get(eventId);
    }
}