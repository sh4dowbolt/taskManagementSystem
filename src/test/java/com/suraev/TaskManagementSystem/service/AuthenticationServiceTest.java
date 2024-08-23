package com.suraev.TaskManagementSystem.service;

import com.suraev.TaskManagementSystem.dto.SignInRequest;
import com.suraev.TaskManagementSystem.dto.SignUpRequest;
import com.suraev.TaskManagementSystem.domain.entity.enums.Role;
import com.suraev.TaskManagementSystem.domain.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
@ExtendWith({MockitoExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@Tag(value = "JwtAuthenticationResponse")
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private JwtServiceImpl jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private AuthenticationManager authenticationManager;

        @Test
        @Order(1)
        void singUP() {

            SignUpRequest request = SignUpRequest.builder().email("qwerty@mail.ru").password("qwerty").build();
            User user = User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ROLE_USER)
                    .build();
            Mockito.when(userService.create(user)).thenReturn(user);
            Mockito.when(jwtService.generateToken(user)).thenReturn("123456678");

            var actualResult = authenticationService.signUp(request);
            Assertions.assertThat(actualResult.getToken()).isEqualTo("123456678");
        }

        @Test
        @Order(2)
        void singIn() {
        SignInRequest request = SignInRequest.builder().email("qwerty@mail.ru").password("qwerty").build();
            User user = User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ROLE_USER)
                    .build();

        Mockito.when(userService.getByEmail(request.getEmail())).thenReturn(user);

        Mockito.when(jwtService.generateToken(user)).thenReturn("123456678");
        var actualResult = authenticationService.signIn(request);

        Assertions.assertThat(actualResult.getToken()).isEqualTo("123456678");
    }

    }

