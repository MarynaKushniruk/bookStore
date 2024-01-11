package com.example.bookstore.controller;

import com.example.bookstore.dto.bookdto.BookDto;
import com.example.bookstore.dto.bookdto.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.bookdto.BookSearchParametersDto;
import com.example.bookstore.dto.bookdto.CreateBookRequestDto;
import com.example.bookstore.service.BookService;
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

@Tag(name = "Book management", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books", description = "Get a list of all available books")
    public List<BookDto> findAll(Pageable pageable) {

        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by id", description = "Get book by id")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PreAuthorize("hasAuthority({'ADMIN'})")
    @PostMapping
    @Operation(summary = "Create a book", description = "Create and save a new book")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto save(@RequestBody @Valid CreateBookRequestDto book) {
        return bookService.save(book);
    }

    @GetMapping("/search")
    @Operation(summary = "Search books by parameters",
            description = "Get a list of book that being searched by parameters")
    public List<BookDto> searchBooks(BookSearchParametersDto searchParameters) {
        return bookService.search(searchParameters);
    }

    @PreAuthorize("hasAuthority({'ADMIN'})")
    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Update a book by id")
    public BookDto updateBookById(@RequestBody CreateBookRequestDto book, @PathVariable Long id) {
        return bookService.update(book, id);
    }

    @PreAuthorize("hasAuthority({'ADMIN'})")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a book", description = "Delete a book by id")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @GetMapping("/{id}/books")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id,
                                                                Pageable pageable) {
        return bookService.findAllByCategoryId(id, pageable);
    }
}
