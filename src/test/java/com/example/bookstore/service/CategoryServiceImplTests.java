package com.example.bookstore.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookstore.dto.categorydto.CategoryRequestDto;
import com.example.bookstore.dto.categorydto.CategoryResponseDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.CategoryMapper;
import com.example.bookstore.model.Category;
import com.example.bookstore.repository.category.CategoryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTests {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Fantasy");
        category.setDescription("Unreal story");
    }

    @Test
    public void findAll_ValidPageable_ListOfCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        Page<Category> categoryPage = new PageImpl<>(categories);
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categoryPage);
        List<CategoryResponseDto> expected = new ArrayList<>();
        expected.add(createCategoryDto());
        when(categoryMapper.toDto(category)).thenReturn(createCategoryDto());
        List<CategoryResponseDto> actual = categoryService.findAll(mock(Pageable.class));
        Assertions.assertEquals(expected, actual);
        verify(categoryRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void findById_WithInvalidId_ShouldThrowEntityNotFoundException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(anyLong()));
        verify(categoryRepository, times(1)).findById(anyLong());
    }

    @Test
    public void saveCategory_WithValidCategoryDto_ShouldReturnSavedCategory() {
        CategoryRequestDto requestDto = new CategoryRequestDto()
                .setName("Fantasy").setDescription("Unreal story");
        CategoryResponseDto expected = createCategoryDto();
        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryResponseDto actual = categoryService.save(requestDto);
        Assertions.assertEquals(expected, actual);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    public void updateCategory_WithValidIdAndCategoryDto_ReturnUpdatedCategory() {
        CategoryResponseDto expected = createCategoryDto();
        Category updatedCategory = new Category();
        updatedCategory.setId(expected.getId());
        updatedCategory.setName("Fantasy");
        updatedCategory.setDescription("Unreal story");
        when(categoryRepository.findById(expected.getId())).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(category)).thenReturn(expected.setName("Fantasy"));
        CategoryResponseDto actual = categoryService.update(expected.getId(), expected);
        Assertions.assertEquals(expected, actual);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    public void deleteCategoryById_WithValidId_ShouldDeleteCategory() {
        categoryService.deleteById(anyLong());
        verify(categoryRepository, times(1)).deleteById(anyLong());
    }

    private CategoryResponseDto createCategoryDto() {
        return new CategoryResponseDto()
                .setId(1L).setName("Fantasy").setDescription("Unreal story");
    }
}

