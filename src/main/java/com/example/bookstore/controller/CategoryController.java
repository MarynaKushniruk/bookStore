package com.example.bookstore.controller;

import com.example.bookstore.dto.categorydto.CategoryDto;
import com.example.bookstore.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category management", description = "Endpoints for managing categories")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasRole({'ADMIN'})")
    @PostMapping
    @Operation(summary = "Create a category", description = "Create and save a new category")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @GetMapping
    @Operation(summary = "Get all categories", description =
            "Get a list of all available categories")
    public List<CategoryDto> getAll(Pageable pageable) {

        return categoryService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by id", description = "Get category by id")
    public CategoryDto getCategoryById(@PathVariable Long id) {

        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole({'ADMIN'})")
    @PutMapping("/{id}")
    @Operation(summary = "Update a category", description = "Update a category by id")
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @RequestBody CategoryDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @PreAuthorize("hasRole({'ADMIN'})")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a category", description = "Delete a category by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }
}
