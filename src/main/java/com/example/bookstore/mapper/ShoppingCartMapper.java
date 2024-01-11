package com.example.bookstore.mapper;

import com.example.bookstore.config.MapperConfig;
import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.bookstore.model.ShoppingCart;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);

    ShoppingCart toModel(ShoppingCartResponseDto shoppingCartResponseDto);

    @AfterMapping
    default void setUserId(@MappingTarget ShoppingCartResponseDto responseDto,
                           ShoppingCart shoppingCart) {
        responseDto.setUserId(shoppingCart.getUser().getId());

    }
}
