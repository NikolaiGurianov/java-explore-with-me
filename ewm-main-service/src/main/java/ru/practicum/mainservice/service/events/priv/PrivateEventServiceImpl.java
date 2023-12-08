package ru.practicum.mainservice.service.events.priv;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dto.event.*;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.exception.ValidException;
import ru.practicum.mainservice.mapper.*;
import ru.practicum.mainservice.model.*;
import ru.practicum.mainservice.repository.*;
import ru.practicum.mainservice.service.categories.CategoriesService;
import ru.practicum.mainservice.service.events.admin.AdministratorEventService;
import ru.practicum.mainservice.service.users.UserService;
import ru.practicum.mainservice.statuses.RequestStatus;
import ru.practicum.mainservice.statuses.State;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mainservice.dto.event.UpdateEventUserRequest.StateAction.SEND_TO_REVIEW;
import static ru.practicum.mainservice.statuses.RequestStatus.CONFIRMED;

import static ru.practicum.util.Constants.SORT_BY_ID_ASC;

@Slf4j
@Data
@RequiredArgsConstructor
@Service
public class PrivateEventServiceImpl implements PrivateEventService {
    private final AdministratorEventService admService;
    private final EventsRepository eventsRepository;
    private final RequestsRepository requestsRepository;
    private final CategoriesRepository categoriesRepository;
    private final LocationRepository locationRepository;
    private final UserService userService;
    private final CategoriesService categoriesService;

    @Override
    public EventRequestResponse changeStatusRequests(Long userId, Long eventId, EventRequest eventRequest) {
        log.info("Private: Запрос на изменение статуса заявок на участие в событии {}", eventRequest);
        User user = userService.getEntity(userId);
        Event event = admService.getEntity(eventId);
        EventRequestResponse result = new EventRequestResponse(Collections.emptyList(), Collections.emptyList());
        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new ConflictException("Пользователь не является инициатором данного события");
        }
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            List<ParticipationRequest> confirmedRequests = requestsRepository.findByIdInAndStatus(eventRequest.getRequestIds(), CONFIRMED);
            result.setConfirmedRequests(confirmedRequests.stream()
                    .map(RequestMapper::toDto)
                    .collect(Collectors.toList()));
            return result;
        }
        if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new ConflictException("Превышен лимит участников события с ID= {}", event.getId());
        }
        List<ParticipationRequest> confirmedRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedRequests = new ArrayList<>();

        long vacantPlace = event.getParticipantLimit() - event.getConfirmedRequests();

        List<ParticipationRequest> requestsList = requestsRepository.findAllById(eventRequest.getRequestIds());

        for (ParticipationRequest request : requestsList) {
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new ConflictException("Запрос должен иметь статус PENDING");
            } else {
                if (eventRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
                    request.setStatus(eventRequest.getStatus());
                    requestsRepository.save(request);
                    event.setConfirmedRequests(requestsRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED));
                    eventsRepository.save(event);
                    confirmedRequests.add(request);
                    vacantPlace--;
                } else if (eventRequest.getStatus().equals(RequestStatus.REJECTED)) {
                    request.setStatus(eventRequest.getStatus());
                    requestsRepository.save(request);
                    rejectedRequests.add(request);
                } else {
                    request.setStatus(eventRequest.getStatus());
                    requestsRepository.save(request);
                }

            }
        }
        result.setConfirmedRequests(confirmedRequests.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList()));
        result.setRejectedRequests(rejectedRequests.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList()));

        eventsRepository.save(event);
        requestsRepository.saveAll(requestsList);

        return result;
    }

    @Override
    public List<ParticipationRequestDto> getRequestsForEventPr(Long userId, Long eventId) {
        log.info("Private: Запрос на получение заявок для события с ID={}", eventId);

        admService.getEntity(eventId);
        userService.getEntity(userId);
        List<ParticipationRequestDto> requestDtoList = requestsRepository.findAllByEventId(eventId)
                .stream().map(RequestMapper::toDto).collect(Collectors.toList());

        log.info("Private: Запрос выполнен. Получено {} заявок", requestDtoList.size());

        return requestDtoList;
    }

    @Override
    public List<EventShortDto> getEventsByUserIdPr(Long userId, int from, int size) {
        log.info("Private: Запрос на получение событий для пользователя с ID={}", userId);

        userService.getEntity(userId);
        Pageable pageable = PageRequest.of(from / size, size, SORT_BY_ID_ASC);
        List<Event> eventList = eventsRepository.findAllByInitiatorId(userId, pageable);

        if (eventList.isEmpty()) {
            return new ArrayList<>();
        }
        List<EventShortDto> eventShortDtoList = eventList.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
        log.info("Private: Запрос выполнен. Получено {} событий", eventShortDtoList.size());

        return eventShortDtoList;
    }

    @Override
    public EventFullDto addEventPr(Long userId, NewEventDto newEventDto) {
        User user = userService.getEntity(userId);
        Category category = categoriesService.getEntity(newEventDto.getCategory());

        if (newEventDto.getEventDate() != null && newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidException("Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента.");
        }

        Location location = locationRepository.save(LocationMapper.toLocation(newEventDto.getLocation()));

        Event event = EventMapper.toEvent(newEventDto, category, location, user);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventsRepository.save(event));
        log.info("Private: Запрос выполнен. Добавлено событие: {}", eventFullDto);

        return eventFullDto;
    }

    @Override
    public EventFullDto getEventByIdPr(Long userId, Long eventId) {
        log.info("Private: Запрос на получение события");

        User user = userService.getEntity(userId);
        Event event = admService.getEntity(eventId);

        if (!event.getInitiator().equals(user)) {
            throw new ConflictException("Пользователь с ID={}, не является инициатором данного события", userId);
        }
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventsRepository.save(event));
        log.info("Private: Запрос выполнен. Получено событие с ID= {}", eventId);

        return eventFullDto;
    }

    @Override
    public EventFullDto updateEventPr(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        log.info("Private: Запрос на обновление события");

        userService.getEntity(userId);
        Event event = admService.getEntity(eventId);

        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Изменить можно только отмененные события или события в состоянии ожидания модерации");
        }
        Long catId = updateEventUserRequest.getCategoryId();

        if (catId != null) {
            event.setCategory(categoriesRepository.findById(catId).orElseThrow(() ->
                    new NotFoundException("Событие с ID={} не найдено", catId)));
        }
        if (updateEventUserRequest.getEventDate() != null && updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidException("Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента.");
        }

        helperUpdateEvent(updateEventUserRequest, event);
        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction().equals(SEND_TO_REVIEW)) {
                event.setState(State.PENDING);
            } else {
                event.setState(State.CANCELED);
            }
        }
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventsRepository.save(event));
        log.info("Private: Запрос выполнен. Событие обновлено: {}", eventFullDto);

        return eventFullDto;
    }

    private void helperUpdateEvent(UpdateEventUserRequest requestToUpdate, Event event) {
        if (requestToUpdate.getAnnotation() != null && !requestToUpdate.getAnnotation().isBlank()) {
            event.setAnnotation(requestToUpdate.getAnnotation());
        }
        if (requestToUpdate.getCategoryId() != null) {
            Category category = categoriesRepository
                    .findById(requestToUpdate.getCategoryId()).orElseThrow(
                            () -> new NotFoundException("Категория с ID={} не найдена", requestToUpdate.getCategoryId()));
            event.setCategory(category);
        }
        if (requestToUpdate.getDescription() != null && !requestToUpdate.getDescription().isBlank()) {
            event.setDescription(requestToUpdate.getDescription());
        }
        if (requestToUpdate.getEventDate() != null) {
            event.setEventDate(requestToUpdate.getEventDate());
        }
        if (requestToUpdate.getLocationDto() != null) {
            event.setLocation(LocationMapper.toLocation(requestToUpdate.getLocationDto()));
        }
        if (requestToUpdate.getPaid() != null) {
            event.setPaid(requestToUpdate.getPaid());
        }
        if (requestToUpdate.getParticipantLimit() != null) {
            event.setParticipantLimit(requestToUpdate.getParticipantLimit());
        }
        if (requestToUpdate.getRequestModeration() != null) {
            event.setRequestModeration(requestToUpdate.getRequestModeration());
        }
        if (requestToUpdate.getTitle() != null && !requestToUpdate.getTitle().isBlank()) {
            event.setTitle(requestToUpdate.getTitle());
        }
    }
}
