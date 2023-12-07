package ru.practicum.mainservice.dto.eventDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.mainservice.dto.LocationDto;

import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

import static ru.practicum.util.Constants.DATE_TIME_FORMAT;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {
    @Length(min = 20, max = 2000, message = "Длина аннотации события должна быть не менее 20 и не более 2000 символов")
    private String annotation;
    private Long category;
    @Length(min = 20, max = 7000, message = "Длина описания события должна быть не менее 20 и не более 7000 символов")
    private String description;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero
    private Long participantLimit;
    private Boolean requestModeration;
    @Length(min = 3, max = 120, message = "Длина заголовка события должна быть не менее 3 и не более 120 символов")
    private String title;
    private StateAction stateAction;

    public enum StateAction {
        PUBLISH_EVENT,
        REJECT_EVENT
    }
}