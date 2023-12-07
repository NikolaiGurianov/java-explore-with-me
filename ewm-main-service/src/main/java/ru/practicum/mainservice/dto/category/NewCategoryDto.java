package ru.practicum.mainservice.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {
    @NotNull
    @NotBlank
    @Length(min = 1, max = 50, message = "Длина названия должна быть не менее 1 и не более 50 символов")
    private String name;
}
