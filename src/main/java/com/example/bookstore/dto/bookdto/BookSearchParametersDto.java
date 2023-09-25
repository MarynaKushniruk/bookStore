package com.example.bookstore.dto.bookdto;

import lombok.Data;

@Data
public class BookSearchParametersDto {
    private String[] authors;

    private String[] titles;
}
