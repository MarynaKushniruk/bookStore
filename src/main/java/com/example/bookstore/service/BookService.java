package com.example.bookstore.service;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.BookSearchParametersDto;
import com.example.bookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto bookDto);

    List<BookDto> findAll();

    BookDto getBookById(Long id);

    BookDto update(CreateBookRequestDto bookDto, Long id);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto searchParametersDto);
}
