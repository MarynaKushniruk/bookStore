package com.example.bookstore.controller;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import com.example.bookstore.dto.userdto.UserLoginRequestDto;
import com.example.bookstore.dto.userdto.UserLoginResponseDto;
import com.example.bookstore.dto.userdto.UserRegistrationRequestDto;
import com.example.bookstore.dto.userdto.UserResponseDto;
import com.example.bookstore.exception.RegistrationException;
import com.example.bookstore.service.AuthenticationService;
import com.example.bookstore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger LOGGER = LogManager.getLogger(AuthController.class);
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/register", consumes = APPLICATION_JSON_VALUE)

    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        LOGGER.info("Received registration request for user with email: {}", request.getEmail());
        return userService.register(request);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
