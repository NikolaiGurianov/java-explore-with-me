package ru.practicum.mainservice.service.users;

import ru.practicum.mainservice.dto.userDto.NewUserDto;
import ru.practicum.mainservice.dto.userDto.UserDto;
import ru.practicum.mainservice.model.User;

import java.util.List;

public interface UserService {
    void deleteUser(Long userId);

    UserDto addUser(NewUserDto newUserDto);

    List<UserDto> getUsers(List<Long> ids, int from, int size);

    User getEntity(Long userId);

    NewUserDto get(Long userId);
}