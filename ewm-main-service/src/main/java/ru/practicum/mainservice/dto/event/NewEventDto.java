package ru.practicum.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.mainservice.dto.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

import static ru.practicum.util.Constants.DATE_TIME_FORMAT;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotNull
    @NotBlank(message = "Аннотация события не может быть пустым")
    @Length(min = 20, max = 2000, message = "Длина аннотации события должна быть не менее 20 и не более 2000 символов")
    private String annotation;
    private long category;
    @NotNull
    @NotBlank(message = "Описание события не может быть пустым")
    @Length(min = 20, max = 7000, message = "Длина описания события должна быть не менее 20 и не более 7000 символов")
    private String description;
    @NotNull
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime eventDate;
    @NotNull
    private LocationDto location;
    @NotNull
    private Boolean paid = false;
    @NotNull
    @PositiveOrZero
    private Long participantLimit = 0L;
    @NotNull
    private Boolean requestModeration = true;
    @Length(min = 3, max = 120, message = "Длина заголовка события должна быть не менее 3 и не более 120 символов")
    private String title;
}