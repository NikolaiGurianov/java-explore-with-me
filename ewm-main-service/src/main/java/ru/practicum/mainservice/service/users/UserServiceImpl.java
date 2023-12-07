package ru.practicum.mainservice.service.users;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dto.userDto.NewUserDto;
import ru.practicum.mainservice.dto.userDto.UserDto;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.ValidException;
import ru.practicum.mainservice.mapper.UserMapper;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.repository.UsersRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.util.Constants.SORT_BY_ID_ASC;

@Slf4j
@Data
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepository usersRepository;

    @Override
    public UserDto addUser(NewUserDto newUserDto) {
        log.info("Admin: Запрос на добавление нового пользователя");
        String[] parts = newUserDto.getEmail().split("@");
        String mailboxName = parts[0];
        String domainName = parts[1];
        String[] partsDomain = domainName.split("\\.");

        String domainPart = partsDomain[0];
        if (mailboxName.length() > 64 || domainPart.length() > 63) {
            throw new ValidException("Адрес электронной почты не удовлетворяет требованиям");
        }
        for (User user : usersRepository.findAll()) {
            if (user.getName().equals(newUserDto.getName())) {
                throw new ConflictException("Имя пользователя должно быть уникальное");
            }
        }
        User user = usersRepository.save(UserMapper.toUser(newUserDto));
        UserDto dto = UserMapper.toUserDto(user);
        log.info("Admin: Новый пользователь успешно добавлен. Получен пользователь: {}", dto);

        return dto;
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        log.info("Admin: Запрос на получение списка пользователей");

        Pageable pageable = PageRequest.of(from / size, size, SORT_BY_ID_ASC);
        List<User> users;

        if (ids != null && !ids.isEmpty()) {
            users = usersRepository.findAllByIdIn(ids, pageable);
        } else {
            users = usersRepository.findAll(pageable).getContent();
        }
        List<UserDto> userDtos = users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        log.info("Admin: Запрос выполнен. Получено {} пользователей", userDtos.size());

        return userDtos;
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Admin: Запрос на удаление пользователя с ID={}", userId);
        usersRepository.deleteById(userId);
        log.info("Admin: Запрос выполнен. Пользователь удален");
    }

    @Override
    public NewUserDto get(Long userId) {
        return UserMapper.toDto(getEntity(userId));
    }

    @Override
    public User getEntity(Long userId) {
        return usersRepository.get(userId);
    }
}