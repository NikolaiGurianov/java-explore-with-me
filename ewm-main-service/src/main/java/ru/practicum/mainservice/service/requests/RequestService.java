package ru.practicum.mainservice.service.requests;

import ru.practicum.mainservice.dto.requestDto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto addUserRequests(Long userId, Long eventId);

    ParticipationRequestDto cancelUserRequest(Long userId, Long eventId);
}