package com.example.bookstore.service;

import com.example.bookstore.dto.order.OrderRequestDto;
import com.example.bookstore.dto.order.OrderRequestUpdateDto;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.dto.orderitem.OrderItemResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto saveOrder(String email, OrderRequestDto requestDto);

    List<OrderResponseDto> getAll(String email, Pageable pageable);

    List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId);

    OrderItemResponseDto getOrderItemByIdFromOrderById(Long orderItemId, Long orderId);

    OrderResponseDto updateStatus(Long orderId, OrderRequestUpdateDto requestUpdateDto);
}
