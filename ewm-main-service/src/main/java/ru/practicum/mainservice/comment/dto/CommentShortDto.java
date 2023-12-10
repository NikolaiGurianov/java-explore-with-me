package ru.practicum.mainservice.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentShortDto {
    private Long id;
    private String text;
    private EventShortDto event;
    private UserShortDto commentator;
    private LocalDateTime publishedOn;
}
