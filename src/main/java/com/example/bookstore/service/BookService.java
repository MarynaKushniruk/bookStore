package com.example.bookstore.service;

import com.example.bookstore.dto.bookdto.BookDto;
import com.example.bookstore.dto.bookdto.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.bookdto.BookSearchParametersDto;
import com.example.bookstore.dto.bookdto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto bookDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto getBookById(Long id);

    BookDto update(CreateBookRequestDto bookDto, Long id);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto searchParametersDto);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable);
}
