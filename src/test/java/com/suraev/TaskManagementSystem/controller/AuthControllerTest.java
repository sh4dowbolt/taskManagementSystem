package com.suraev.TaskManagementSystem.controller;

import com.suraev.TaskManagementSystem.dto.JwtAuthenticationResponse;
import com.suraev.TaskManagementSystem.dto.SignInRequest;
import com.suraev.TaskManagementSystem.dto.SignUpRequest;
import com.suraev.TaskManagementSystem.service.AuthenticationServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Tag("AuthController")
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationServiceImpl authenticationService;


    @Test
    @Order(1)
    void signUp() {
        SignUpRequest request = SignUpRequest.builder().email("qwerty@mail.ru").password("qwerty").build();
        Mockito.when(authenticationService.signUp(request)).thenReturn(JwtAuthenticationResponse.builder().token("123").build());

        JwtAuthenticationResponse actualResult = authController.signUp(request);
        assertThat(actualResult.getToken()).isEqualTo("123");
    }
    @Test
    @Order(2)
    void singIn() {
        SignInRequest request = SignInRequest.builder().email("qwerty@mail.ru").password("qwerty").build();
        Mockito.when(authenticationService.signIn(request)).thenReturn(JwtAuthenticationResponse.builder().token("123").build());
        JwtAuthenticationResponse actualResult = authController.singIn(request);
        assertThat(actualResult.getToken()).isEqualTo("123");
    }
}