package ru.practicum.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.comment.dto.CommentFullDto;
import ru.practicum.mainservice.comment.dto.UpdateCommentRequest;
import ru.practicum.mainservice.comment.service.CommentService;
import ru.practicum.mainservice.statuses.CommentStatus;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.Constants.DATE_TIME_FORMAT;

@RestController
@RequestMapping(path = "/admin/comment")
@RequiredArgsConstructor
public class AdminCommentController {
    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    CommentFullDto updateComment(@PathVariable Long commentId,
                                 @Valid @RequestBody UpdateCommentRequest updateCommentRequest) {
        return commentService.updateCommentAdm(commentId, updateCommentRequest);
    }

    @GetMapping
    public List<CommentFullDto> getCommentsByParameters(@RequestParam(required = false) List<Long> users,
                                                        @RequestParam(required = false) String text,
                                                        @RequestParam(required = false) List<CommentStatus> states,
                                                        @RequestParam(required = false) List<Long> events,
                                                        @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
                                                        @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
                                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                        @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return commentService.getCommentByParamAdm(users, text, states, events, rangeStart, rangeEnd, from, size);
    }

    @GetMapping("{commentId}")
    public CommentFullDto getCommentById(@PathVariable Long commentId) {
        return commentService.getCommentByIdAdm(commentId);
    }


}
