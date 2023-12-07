package ru.practicum.mainservice.mapper;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.mainservice.dto.requestDto.ParticipationRequestDto;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.ParticipationRequest;
import ru.practicum.mainservice.model.User;

import java.time.LocalDateTime;

import static ru.practicum.mainservice.statuses.RequestStatus.PENDING;

@Data
@Component
public class RequestMapper {
    public static ParticipationRequest toRequest(User user, Event event) {
        return new ParticipationRequest(null, event, user, PENDING, LocalDateTime.now());
    }

    public static ParticipationRequestDto toDto(ParticipationRequest request) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setId(request.getId());
        participationRequestDto.setRequester(request.getRequester().getId());
        participationRequestDto.setEvent(request.getEvent().getId());
        participationRequestDto.setCreated(request.getCreated());
        participationRequestDto.setStatus(request.getStatus());
        return participationRequestDto;
    }
}