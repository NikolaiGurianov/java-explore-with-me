package ru.practicum.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.comment.dto.CommentFullDto;
import ru.practicum.mainservice.comment.dto.NewCommentDto;
import ru.practicum.mainservice.comment.dto.UpdateCommentRequest;
import ru.practicum.mainservice.comment.service.CommentService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users/{userId}/events/{eventId}/comment")
@RequiredArgsConstructor
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping
    ResponseEntity<CommentFullDto> addComment(@PathVariable Long eventId,
                                              @PathVariable Long userId,
                                              @Valid @RequestBody NewCommentDto newCommentDto) {
        return new ResponseEntity<>(commentService.addCommentPr(eventId, userId, newCommentDto), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{commentId}")
    public CommentFullDto editComment(@PathVariable Long userId,
                                      @PathVariable Long eventId,
                                      @PathVariable Long commentId,
                                      @Valid @RequestBody UpdateCommentRequest updateCommentRequest) {
        return commentService.editCommentPr(userId, eventId, commentId, updateCommentRequest);
    }

    @DeleteMapping(value = "/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        commentService.deleteCommentPr(userId, commentId);
    }
}
