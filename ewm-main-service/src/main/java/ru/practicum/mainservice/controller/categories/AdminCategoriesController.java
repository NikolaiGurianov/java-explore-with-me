package ru.practicum.mainservice.controller.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.categoryDto.CategoryDto;
import ru.practicum.mainservice.dto.categoryDto.NewCategoryDto;
import ru.practicum.mainservice.service.categories.CategoriesService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {
    private final CategoriesService categoriesService;

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return new ResponseEntity<>(categoriesService.addCategoryAdm(newCategoryDto), HttpStatus.CREATED);
    }

    @PatchMapping("{categoryId}")
    public CategoryDto updateCategory(@PathVariable Long categoryId,
                                      @Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoriesService.updateCategoryAdm(categoryId, newCategoryDto);
    }

    @DeleteMapping("{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long categoryId) {
        categoriesService.deleteCategoryAdm(categoryId);
    }
}