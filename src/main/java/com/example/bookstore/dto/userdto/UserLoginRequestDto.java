package com.example.bookstore.dto.userdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @Email
    @NotEmpty
    @Size(min = 8, max = 20)
    private String email;
    @NotEmpty
    @Size(min = 8, max = 20)
    private String password;
}
