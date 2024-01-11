package com.example.bookstore.controller;

import com.example.bookstore.dto.order.OrderRequestDto;
import com.example.bookstore.dto.order.OrderRequestUpdateDto;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.dto.orderitem.OrderItemResponseDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Place an order to purchase books from shopping cart")
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto requestDto) {

        return orderService.saveOrder(getAuthenticationName(), requestDto);
    }

    @GetMapping
    @Operation(summary = "Get all orders history of the user")
    public List<OrderResponseDto> getAllO(Pageable pageable) {

        return orderService.getAll(getAuthenticationName(), pageable);
    }

    @GetMapping("/{orderId}/items/{id}")
    @Operation(summary = "Get a particular order item by Id from the order")
    public OrderItemResponseDto getOrderItemByIdFromOrderById(@PathVariable Long orderItemId,
                                                              @PathVariable Long orderId) {
        return orderService.getOrderItemByIdFromOrderById(orderItemId, orderId);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get all order items from order by order Id")
    public List<OrderItemResponseDto> getOrderItemsByOrderId(@PathVariable Long orderId) {
        return orderService.getOrderItemsByOrderId(orderId);
    }

    @PreAuthorize("hasAuthority({'ADMIN'})")
    @PutMapping("/{id}")
    @Operation(summary = "Update the status of the order by id")

    public OrderResponseDto updateStatus(@PathVariable Long orderId,
                                         @RequestBody OrderRequestUpdateDto requestUpdateDto) {
        return orderService.updateStatus(orderId, requestUpdateDto);
    }

    private String getAuthenticationName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        throw new EntityNotFoundException("Can't find authentication name by authentication "
                + authentication);
    }
}
