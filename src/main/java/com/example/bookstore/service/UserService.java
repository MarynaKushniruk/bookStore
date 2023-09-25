package com.example.bookstore.service;

import com.example.bookstore.dto.UserRegistrationRequestDto;
import com.example.bookstore.dto.UserResponseDto;
import com.example.bookstore.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto request) throws RegistrationException;

}
