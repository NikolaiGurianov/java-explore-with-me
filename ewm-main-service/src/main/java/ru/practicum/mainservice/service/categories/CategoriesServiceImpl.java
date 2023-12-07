package ru.practicum.mainservice.service.categories;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dto.categoryDto.CategoryDto;
import ru.practicum.mainservice.dto.categoryDto.NewCategoryDto;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.mapper.CategoryMapper;
import ru.practicum.mainservice.model.Category;
import ru.practicum.mainservice.repository.CategoriesRepository;
import ru.practicum.mainservice.repository.EventsRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.util.Constants.SORT_BY_ID_ASC;

@Slf4j
@Data
@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final EventsRepository eventsRepository;

    @Override
    public List<CategoryDto> getCategoriesPub(int from, int size) {
        log.info("Public: Запрос на получение категорий");
        Pageable pageable = PageRequest.of(from / size, size, SORT_BY_ID_ASC);
        List<CategoryDto> categoryDtoList = categoriesRepository.findAll(pageable).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
        log.info("Public: Запрос выполнен. Получено {} категорий", categoryDtoList.size());
        return categoryDtoList;
    }

    @Override
    public CategoryDto getCategoriesByIdPub(Long catId) {
        log.info("Public: Запрос на получение категории с ID={}", catId);
        CategoryDto categoryDto = get(catId);
        log.info("Public: Запрос выполнен. Получена категория: {}", categoryDto);
        return categoryDto;
    }

    @Override
    public CategoryDto addCategoryAdm(NewCategoryDto newCategoryDto) {
        log.info("Admin: Получен запрос на добавление новой категории");

        for (Category category : categoriesRepository.findAll()) {
            if (category.getName().equals(newCategoryDto.getName())) {
                throw new ConflictException("Имя категории должно быть уникальное");
            }
        }
        Category category = categoriesRepository.save(CategoryMapper.toCategory(newCategoryDto));
        CategoryDto categoryDto = CategoryMapper.toCategoryDto(category);
        log.info("Admin: Запрос выполнен. Добавлена категория: {}", categoryDto);
        return categoryDto;
    }

    @Override
    public CategoryDto updateCategoryAdm(Long catId, NewCategoryDto newCategoryDto) {
        log.info("Admin: Получен запрос на обновление категории");
        Category updateCategory = getEntity(catId);
        for (Category category : categoriesRepository.findAll()) {
            if (!category.getId().equals(catId) && category.getName().equals(newCategoryDto.getName())) {
                throw new ConflictException("Имя должно быть уникальным");
            }
        }
        updateCategory.setName(newCategoryDto.getName());
        CategoryDto categoryDto = CategoryMapper.toCategoryDto(categoriesRepository.save(updateCategory));
        log.info("Admin: Запрос выполнен. Категория обновлена: {}", categoryDto);
        return categoryDto;
    }

    @Override
    public void deleteCategoryAdm(Long catId) {
        log.info("Admin: Получен запрос на удаление категории c ID= {}", catId);
        Category category = getEntity(catId);
        if (!eventsRepository.findAllByCategoryId(catId).isEmpty()) {
            throw new ConflictException("Категория с ID= {} используется и не может быть удалена", catId);
        }
        categoriesRepository.deleteById(catId);
        log.info("Admin: Запрос выполнен. Категория ID= {} удалена", catId);
    }

    @Override
    public CategoryDto get(Long categoryId) {
        return CategoryMapper.toCategoryDto(getEntity(categoryId));
    }

    @Override
    public Category getEntity(Long categoryId) {
        return categoriesRepository.get(categoryId);
    }
}
