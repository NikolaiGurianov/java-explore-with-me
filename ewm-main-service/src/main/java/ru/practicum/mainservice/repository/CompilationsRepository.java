package ru.practicum.mainservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.model.Compilation;

import java.util.List;

public interface CompilationsRepository extends JpaRepository<Compilation, Long> {
    default Compilation get(long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Подборка с ID= {} не зарегистрирован!", id));
    }

    List<Compilation> findAllByPinned(Pageable pageable, Boolean pinned);
}