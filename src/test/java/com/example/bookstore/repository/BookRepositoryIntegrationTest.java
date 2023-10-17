package com.example.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.book.BookRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
    @Sql(scripts = "classpath:database/add-for-book-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete-for-book-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

    void findAllByCategoryId_ReturnOneBook() {
        long id = 1L;
        Book book = new Book();
        book.setId(id);
        book.setTitle("Odyssey");
        book.setAuthor("Homer");
        book.setIsbn("98765432");
        book.setPrice(BigDecimal.valueOf(250.50));
        List<Book> expected = new ArrayList<>();
        expected.add(book);
        List<Book> actual = bookRepository.findAllByCategoryId(id, PageRequest.of(0, 10));
        assertEquals(expected, actual);
    }

    @Sql(scripts = "classpath:database/add-for-book-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete-for-book-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void findAllByCategoryId_ReturnEmpty() {
        long id = 5;
        List<Book> books = bookRepository.findAllByCategoryId(id, PageRequest.of(0, 10));
        assertEquals(0, books.size());
    }
}
