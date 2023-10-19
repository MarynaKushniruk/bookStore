package com.example.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.book.BookRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryIntegrationTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books by category id when 1 book have that category")
    @Sql(scripts = "classpath:database/add-for-book-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete-for-book-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

    void findAllByCategoryId_ReturnOneBook() {
        List<Book> books = bookRepository.findAllByCategoryId(1L, PageRequest.of(0, 10));
        assertEquals(1, books.size());
        assertEquals("Odyssey", books.get(0).getTitle());
    }

    @Test
    @DisplayName("Find all books by category id when no one book have this category")
    @Sql(scripts = "classpath:database/add-for-book-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete-for-book-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId_ReturnEmpty() {
        long id = 5;
        List<Book> books = bookRepository.findAllByCategoryId(id, PageRequest.of(0, 10));
        assertEquals(0, books.size());
    }
}
