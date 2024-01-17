package com.example.bookstore.mapper;

import com.example.bookstore.config.MapperConfig;
import com.example.bookstore.dto.order.OrderRequestDto;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.dto.orderitem.OrderItemResponseDto;
import com.example.bookstore.model.Order;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    OrderResponseDto toDto(Order order);

    Order toModel(OrderRequestDto orderRequestDto);

    @AfterMapping
    default void setOrderItemsResponse(@MappingTarget OrderResponseDto orderResponseDto,
                                       Order order) {
        orderResponseDto.setOrderItems(order.getOrderItems()
                .stream()
                .map(orderItem -> {
                    OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();
                    orderItemResponseDto.setId(orderItem.getId());
                    orderItemResponseDto.setBookId(orderItem.getBook().getId());
                    orderItemResponseDto.setQuantity(orderItem.getQuantity());
                    return orderItemResponseDto;
                })
                .collect(Collectors.toSet()));
    }
}
