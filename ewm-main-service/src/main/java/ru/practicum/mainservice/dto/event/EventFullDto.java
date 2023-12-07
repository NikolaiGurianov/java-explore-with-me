package ru.practicum.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.dto.LocationDto;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.user.UserShortDto;
import ru.practicum.mainservice.statuses.State;

import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static ru.practicum.util.Constants.DATE_TIME_FORMAT;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    private Long id;
    private String annotation;
    @NotNull
    private CategoryDto category;
    private String description;
    private Long confirmedRequests;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime createdOn;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private LocationDto location;
    private Boolean paid;//
    @JsonProperty(defaultValue = "0L")
    private Long participantLimit;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime publishedOn;
    @JsonProperty(defaultValue = "true")
    private Boolean requestModeration;
    private State state;
    private String title;
    private long views;
}