package ru.practicum.mainservice.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.comment.dto.CommentFullDto;
import ru.practicum.mainservice.comment.dto.CommentShortDto;
import ru.practicum.mainservice.comment.dto.NewCommentDto;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.mapper.EventMapper;
import ru.practicum.mainservice.mapper.UserMapper;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.User;

import java.time.LocalDateTime;

import static ru.practicum.mainservice.statuses.CommentStatus.PENDING;

@UtilityClass
public class CommentMapper {

    public static Comment toComment(User user, Event event, NewCommentDto newCommentDto) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .event(event)
                .commentator(user)
                .createdOn(LocalDateTime.now())
                .status(PENDING).build();
    }

    public static CommentFullDto toFullDto(Comment comment) {
        return CommentFullDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .event(EventMapper.toEventShortDto(comment.getEvent()))
                .commentator(UserMapper.toUserShortDto(comment.getCommentator()))
                .createdOn(comment.getCreatedOn())
                .publishedOn(comment.getPublishedOn())
                .status(comment.getStatus())
                .build();
    }

    public static CommentShortDto toShortDto(Comment comment) {
        return CommentShortDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .publishedOn(comment.getPublishedOn())
                .event(EventMapper.toEventShortDto(comment.getEvent()))
                .commentator(UserMapper.toUserShortDto(comment.getCommentator()))
                .build();
    }
}