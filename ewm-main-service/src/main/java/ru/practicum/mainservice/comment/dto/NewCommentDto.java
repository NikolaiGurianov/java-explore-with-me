package ru.practicum.mainservice.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentDto {
    @NotNull
    @NotBlank(message = "Текст комментария не может быть пустым")
    @Length(min = 1, max = 7000, message = "Длина текста комментария должна быть не менее 1 и не более 7000 символов")
    private String text;
}
