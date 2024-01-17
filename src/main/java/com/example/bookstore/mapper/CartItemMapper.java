package com.example.bookstore.mapper;

import com.example.bookstore.config.MapperConfig;
import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.cartitem.CartItemResponseDto;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(source = "cartItem.book.id", target = "bookId")
    @Mapping(source = "cartItem.book.title", target = "bookTitle")
    @Mapping(source = "cartItem.book.author", target = "author")
    CartItemResponseDto toDto(CartItem cartItem);

    CartItem toModel(CartItemRequestDto cartItemRequestDto);

    @AfterMapping
    default void setBookForCreating(@MappingTarget CartItem cartItem,
                                    CartItemRequestDto cartItemRequestDto) {
        Book book = new Book();
        book.setId(cartItemRequestDto.getBookId());
        cartItem.setBook(book);
    }
}
