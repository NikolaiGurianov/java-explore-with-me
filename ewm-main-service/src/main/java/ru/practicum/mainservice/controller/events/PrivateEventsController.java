package ru.practicum.mainservice.controller.events;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.eventDto.*;
import ru.practicum.mainservice.dto.requestDto.ParticipationRequestDto;
import ru.practicum.mainservice.service.events.priv.PrivateEventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventsController {
    private final PrivateEventService eventsService;

    @GetMapping
    public List<EventShortDto> getEvents(@PathVariable Long userId,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return eventsService.getEventsByUserIdPr(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventsById(@PathVariable Long userId,
                                      @PathVariable Long eventId) {
        return eventsService.getEventByIdPr(userId, eventId);
    }


    @PostMapping
    public ResponseEntity<EventFullDto> addEvent(@Valid @RequestBody NewEventDto newEventDto,
                                                 @PathVariable Long userId) {
        return new ResponseEntity<>(eventsService.addEventPr(userId, newEventDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @Valid @RequestBody(required = false) UpdateEventUserRequest updateEventUserRequest,
                                    @PathVariable Long eventId) {
        return eventsService.updateEventPr(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForEvent(@PathVariable Long userId,
                                                             @PathVariable Long eventId) {
        return eventsService.getRequestsForEventPr(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestResponse changeStatusRequests(@PathVariable Long userId,
                                                     @PathVariable Long eventId,
                                                     @RequestBody EventRequest eventRequest) {
        return eventsService.changeStatusRequests(userId, eventId, eventRequest);
    }
}