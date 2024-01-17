package com.example.bookstore.service;

import com.example.bookstore.dto.categorydto.CategoryRequestDto;
import com.example.bookstore.dto.categorydto.CategoryResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CategoryRequestDto categoryDto);

    CategoryResponseDto update(Long id, CategoryResponseDto categoryDto);

    void deleteById(Long id);
}
