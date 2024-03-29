package com.example.bookstore.dto.order;

import com.example.bookstore.dto.orderitem.OrderItemResponseDto;
import com.example.bookstore.model.Status;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class OrderResponseDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    private Status status;
    @NotNull
    private BigDecimal total;
    @NotNull
    private LocalDateTime orderDate;
    private Set<OrderItemResponseDto> orderItems;
}
