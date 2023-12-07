package ru.practicum.mainservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.model.User;

import java.util.List;

public interface UsersRepository extends JpaRepository<User, Long> {
    default User get(long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Пользователь с идентификатором #" +
                id + " не зарегистрирован!"));
    }

    List<User> findAllByIdIn(List<Long> userIds, Pageable pageable);
}