package com.suraev.TaskManagementSystem.controller;

import com.suraev.TaskManagementSystem.dto.JwtAuthenticationResponse;
import com.suraev.TaskManagementSystem.dto.SignInRequest;
import com.suraev.TaskManagementSystem.dto.SignUpRequest;
import com.suraev.TaskManagementSystem.dto.TaskDTO;
import com.suraev.TaskManagementSystem.service.AuthenticationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутенфикация", description = "Процесс регистрации и авторизации")

public class AuthController {

    private final AuthenticationServiceImpl authenticationService;

    @Operation(summary = "Регистрация пользователя", description = "Позволяет зарегистрировать пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = TaskDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ResponseEntityExceptionHandler.class), mediaType = "application/json")})
    })
    @PostMapping(value = "/sign-up", produces = "application/json")
    public JwtAuthenticationResponse signUp(
            @RequestBody @Valid @Parameter(description ="Email и пароль для регистрации") SignUpRequest request) {
        return authenticationService.signUp(request);
    }
    @Operation(summary = "Авторизация пользователя", description = "Позволяет авторизоваться пользователю")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = TaskDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema(implementation = ResponseEntityExceptionHandler.class), mediaType = "application/json")})
    })
    @PostMapping(value = "/sign-in", produces = "application/json")
    public JwtAuthenticationResponse singIn(
            @RequestBody @Valid @Parameter(description ="Email и пароль для авторизации")SignInRequest request) {
        return authenticationService.signIn(request);
    }

}
