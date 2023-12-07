package ru.practicum.mainservice.controller.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.categoryDto.CategoryDto;
import ru.practicum.mainservice.service.categories.CategoriesService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class PublicCategoriesController {
    private final CategoriesService categoriesService;

    @GetMapping
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                           @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return categoriesService.getCategoriesPub(from, size);
    }

    @GetMapping("{categoryId}")
    public CategoryDto getCategoriesById(@PathVariable Long categoryId) {
        return categoriesService.getCategoriesByIdPub(categoryId);
    }
}