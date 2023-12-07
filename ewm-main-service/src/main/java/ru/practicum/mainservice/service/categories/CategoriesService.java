package ru.practicum.mainservice.service.categories;

import ru.practicum.mainservice.dto.categoryDto.CategoryDto;
import ru.practicum.mainservice.dto.categoryDto.NewCategoryDto;
import ru.practicum.mainservice.model.Category;

import java.util.List;

public interface CategoriesService {
    List<CategoryDto> getCategoriesPub(int from, int size);

    CategoryDto getCategoriesByIdPub(Long catId);

    CategoryDto addCategoryAdm(NewCategoryDto newCategoryDto);

    CategoryDto updateCategoryAdm(Long catId, NewCategoryDto newCategoryDto);

    void deleteCategoryAdm(Long catId);

    Category getEntity(Long categoryId);

    CategoryDto get(Long categoryId);
}