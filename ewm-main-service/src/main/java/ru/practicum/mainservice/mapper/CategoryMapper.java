package ru.practicum.mainservice.mapper;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.mainservice.dto.categoryDto.CategoryDto;
import ru.practicum.mainservice.dto.categoryDto.NewCategoryDto;
import ru.practicum.mainservice.model.Category;

@Data
@Component
public class CategoryMapper {
    public static Category toCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

    public static CategoryDto toCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}