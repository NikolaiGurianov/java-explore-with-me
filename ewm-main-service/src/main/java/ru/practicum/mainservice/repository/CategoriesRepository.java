package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.model.Category;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
    default Category get(long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Категория с ID= {} не зарегистрирован!", id));
    }
}