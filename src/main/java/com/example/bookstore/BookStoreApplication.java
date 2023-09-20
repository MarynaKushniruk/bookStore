package com.example.bookstore;

import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book ivanhoe = new Book();
            ivanhoe.setAuthor("Walter Scott");
            ivanhoe.setPrice(BigDecimal.valueOf(250.00));
            ivanhoe.setTitle("Ivanhoe");
            bookService.save(ivanhoe);
            System.out.println(bookService.findAll());
        };
    }
}


