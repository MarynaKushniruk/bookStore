package com.example.bookstore.dto;

import lombok.Data;

@Data
public class BookSearchParametersDto {
    private String[] authors;

    private String[] titles;
}
