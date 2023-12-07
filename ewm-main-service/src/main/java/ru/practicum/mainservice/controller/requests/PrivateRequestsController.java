package ru.practicum.mainservice.controller.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.service.requests.RequestService;

import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateRequestsController {
    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        return requestService.getUserRequests(userId);
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> addUserRequests(@PathVariable Long userId,
                                                                   @RequestParam Long eventId) {
        return new ResponseEntity<>(requestService.addUserRequests(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{eventId}/cancel")
    public ParticipationRequestDto cancelUserRequest(@PathVariable Long userId,
                                                     @PathVariable Long eventId) {
        return requestService.cancelUserRequest(userId, eventId);
    }
}