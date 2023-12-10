package ru.practicum.mainservice.comment.service;

import ru.practicum.mainservice.comment.dto.*;
import ru.practicum.mainservice.statuses.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {
    List<CommentShortDto> getPublishedCommentsForEventPub(Long eventId, int from, int size);

    CommentFullDto addCommentPr(Long eventId, Long userId, NewCommentDto newCommentDto);

    CommentFullDto updateCommentAdm(Long commentId, UpdateCommentRequest updateCommentRequest);

    List<CommentFullDto> getCommentByParamAdm(List<Long> users, String text, List<CommentStatus> states,
                                              List<Long> events, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                              int from, int size);

    CommentFullDto editCommentPr(Long userId, Long eventId, Long commentId, UpdateCommentRequest updateCommentRequest);

    void deleteCommentPr(Long userId, Long commentId);

    CommentFullDto getCommentByIdAdm(Long commentId);
}
