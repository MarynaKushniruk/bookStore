package com.example.bookstore.service;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.BookSearchParametersDto;
import com.example.bookstore.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<BookDto> findAll();

    BookDto getBookById(Long id);

    BookDto update(Book book, Long id);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto searchParametersDto);
}
