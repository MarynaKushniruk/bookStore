package com.example.bookstore.mapper;

import com.example.bookstore.config.MapperConfig;
import com.example.bookstore.dto.order.OrderRequestDto;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.model.Order;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderResponseDto toDto(Order order);

    Order toModel(OrderRequestDto orderRequestDto);
}
