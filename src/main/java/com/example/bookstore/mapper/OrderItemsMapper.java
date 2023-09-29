package com.example.bookstore.mapper;

import com.example.bookstore.config.MapperConfig;
import com.example.bookstore.dto.orderitem.OrderItemRequestDto;
import com.example.bookstore.dto.orderitem.OrderItemResponseDto;
import com.example.bookstore.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface OrderItemsMapper {
    OrderItemResponseDto toDto(OrderItem orderItem);

    OrderItem toModel(OrderItemRequestDto orderItemRequestDto);
}
