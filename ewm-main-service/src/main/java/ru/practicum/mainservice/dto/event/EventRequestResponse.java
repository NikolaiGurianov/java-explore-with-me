package ru.practicum.mainservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestResponse {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
