package ru.practicum.mainservice.service.requests;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.exception.ValidException;
import ru.practicum.mainservice.mapper.RequestMapper;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.ParticipationRequest;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.repository.EventsRepository;
import ru.practicum.mainservice.repository.RequestsRepository;
import ru.practicum.mainservice.service.events.admin.AdministratorEventService;
import ru.practicum.mainservice.service.users.UserService;
import ru.practicum.mainservice.statuses.RequestStatus;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mainservice.statuses.RequestStatus.*;
import static ru.practicum.mainservice.statuses.State.PUBLISHED;

@Slf4j
@Data
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserService userService;
    private final AdministratorEventService admService;
    private final RequestsRepository requestsRepository;
    private final EventsRepository eventsRepository;

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        log.info("Private: Запрос на получение заявок пользователя с ID={}", userId);

        User user = userService.getEntity(userId);
        List<ParticipationRequest> requests = requestsRepository.findByRequester_Id(userId);
        List<ParticipationRequestDto> requestDtoList = requests.stream().map(RequestMapper::toDto).collect(Collectors.toList());
        log.info("Private: Запрос выполнен. Получено {} заявок", requestDtoList.size());

        return requestDtoList;
    }

    @Override
    public ParticipationRequestDto addUserRequests(Long userId, Long eventId) {
        log.info("Private: Запрос на добавление заявки пользователя с ID={} на участие в событии с ID={}", userId, eventId);

        User user = userService.getEntity(userId);
        Event event = admService.getEntity(eventId);

        if (requestsRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Нельзя добавить повторный запрос");
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new ConflictException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (event.getParticipantLimit() != 0L && event.getParticipantLimit() <= requestsRepository.countByEventIdAndStatus(eventId, CONFIRMED)) {
            throw new ConflictException("У события достигнут лимит запросов на участие");
        }
        if (event.getState() != PUBLISHED) {
            throw new ConflictException("Нельзя добавить запрос на участие в неопубликованном событии");
        }
        ParticipationRequest request = RequestMapper.toRequest(user, event);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0L) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventsRepository.save(event);
        }
        log.info("Private: Запрос выполнен. Добавлена заявка: {}", request);

        return RequestMapper.toDto(requestsRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelUserRequest(Long userId, Long requestId) {
        log.info("Private: Запрос на отмену заявки пользователя с ID={} и заявки с ID={}", userId, requestId);

        User user = userService.getEntity(userId);
        ParticipationRequest request = requestsRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с ID={} не найден", requestId));

        if (!request.getRequester().getId().equals(userId)) {
            throw new ValidException("Пользователь c ID={} не подал заявку на участие", userId);
        }
        request.setStatus(CANCELED);
        ParticipationRequestDto requestDto = RequestMapper.toDto(request);
        log.info("Private: Запрос выполнен. Заявка с ID={} отменена", requestId);

        return requestDto;
    }
}