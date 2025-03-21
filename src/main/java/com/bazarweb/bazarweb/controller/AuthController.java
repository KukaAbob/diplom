package com.bazarweb.bazarweb.controller;

import com.bazarweb.bazarweb.DTO.Requests.Auth.JwtAuthenticationResponse;
import com.bazarweb.bazarweb.DTO.Requests.Auth.SignInRequest;
import com.bazarweb.bazarweb.DTO.Requests.Auth.SignUpRequest;
import com.bazarweb.bazarweb.service.Admin.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    @Operation(summary = "Регистрация пользователя")
    public JwtAuthenticationResponse signUp(@RequestBody SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        // Передаем объект SignInRequest в метод signIn
        JwtAuthenticationResponse response = authenticationService.signIn(request);
        
        return ResponseEntity.ok(response); // Возвращаем токен в ответе
    }
}
