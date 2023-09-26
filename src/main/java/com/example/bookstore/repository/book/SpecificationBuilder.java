package com.example.bookstore.repository.book;

import com.example.bookstore.dto.bookdto.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParametersDto searchParametersDto);
}
