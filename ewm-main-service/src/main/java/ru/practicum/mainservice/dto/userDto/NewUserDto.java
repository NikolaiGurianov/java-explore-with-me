package ru.practicum.mainservice.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDto {
    private long id;
    @Size(min = 6, max = 254, message = "Длина поля EMAIL должна быть между {min} и {max}")
    @NotBlank(message = "Поле EMAIL не должно быть пустым")
    @Email(message = "Некорректный формат почтового адреса")
    private String email;

    @NotNull
    @Size(min = 2, max = 250, message = "Длина поля NAME должна быть между {min} и {max}")
    @NotBlank(message = "Поле NAME не может быть пустым")
    private String name;
}
