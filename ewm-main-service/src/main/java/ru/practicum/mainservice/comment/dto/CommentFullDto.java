package ru.practicum.mainservice.comment.dto;

import lombok.*;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.user.UserShortDto;
import ru.practicum.mainservice.statuses.CommentStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentFullDto {
    private Long id;
    private String text;
    private EventShortDto event;
    private UserShortDto commentator;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private CommentStatus status;
}