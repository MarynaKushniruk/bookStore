package com.example.bookstore.dto.order;

import com.example.bookstore.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequestUpdateDto {
    @NotNull
    private Status status;
}
