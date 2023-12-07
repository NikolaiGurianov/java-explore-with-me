package ru.practicum.mainservice.dto.compilation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    private List<Long> events;
    @JsonProperty(defaultValue = "false")
    private Boolean pinned;
    @NotBlank(message = "Заголовок подборки не может быть пустым")
    @Length(min = 1, max = 50, message = "Длина заголовока подборки должна быть не менее 1 и не более 50 символов")
    private String title;
}
