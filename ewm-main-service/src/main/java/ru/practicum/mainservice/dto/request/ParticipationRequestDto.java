package ru.practicum.mainservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.statuses.RequestStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {
    private Long id;
    private Long event;
    private Long requester;
    private RequestStatus status;
    private LocalDateTime created;
}