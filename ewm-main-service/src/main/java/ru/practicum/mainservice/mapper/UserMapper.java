package ru.practicum.mainservice.mapper;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.mainservice.dto.userDto.NewUserDto;
import ru.practicum.mainservice.dto.userDto.UserDto;
import ru.practicum.mainservice.dto.userDto.UserShortDto;
import ru.practicum.mainservice.model.User;

@Data
@Component
public class UserMapper {
    public static NewUserDto toDto(User user) {
        NewUserDto userDto = new NewUserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static UserShortDto toUserShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());
        return userShortDto;
    }

    public static User toUser(NewUserDto newUserDto) {
        User user = new User();
        user.setId(newUserDto.getId());
        user.setName(newUserDto.getName());
        user.setEmail(newUserDto.getEmail());
        return user;
    }
}