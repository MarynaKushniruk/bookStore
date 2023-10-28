package com.example.bookstore.service;

import com.example.bookstore.dto.bookdto.BookDto;
import com.example.bookstore.dto.bookdto.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.bookdto.BookSearchParametersDto;
import com.example.bookstore.dto.bookdto.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.book.BookRepository;
import com.example.bookstore.repository.book.BookSpecificationBuilder;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto bookDto) {
        Book book = bookMapper.toModel(bookDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find a book "
                        + "by id " + id));
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
    public BookDto update(CreateBookRequestDto bookDto, Long id) {
        Book bookToUpdate = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Can't find book by id "
                        + id));
        bookToUpdate.setAuthor(bookDto.getAuthor());
        bookToUpdate.setTitle(bookDto.getTitle());
        bookToUpdate.setDescription(bookDto.getDescription());
        bookToUpdate.setPrice(bookDto.getPrice());
        bookToUpdate.setIsbn(bookDto.getIsbn());
        bookToUpdate.setCoverImage(bookDto.getCoverImage());
        return bookMapper.toDto(bookRepository.save(bookToUpdate));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> bookSpecification = bookSpecificationBuilder
                .build(bookSearchParametersDto);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable) {
        return bookRepository.findAllByCategoryId(categoryId, pageable)
                .stream()
                .map(bookMapper::toDtoWithoutCategories)
                .collect(Collectors.toList());
    }
}
