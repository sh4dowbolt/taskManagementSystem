package com.suraev.TaskManagementSystem.service;

import com.suraev.TaskManagementSystem.dto.JwtAuthenticationResponse;
import com.suraev.TaskManagementSystem.dto.SignInRequest;
import com.suraev.TaskManagementSystem.dto.SignUpRequest;
import com.suraev.TaskManagementSystem.domain.entity.enums.Role;
import com.suraev.TaskManagementSystem.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
    private final UserServiceImpl userService;
    private final JwtServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрация пользователя
     * @param request - тело запроса
     * @return токен
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user);

        return new JwtAuthenticationResponse(jwt);

    }

    /** Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userService
                .getByEmail(request.getEmail());
        var jwt = jwtService.generateToken(user);

        return new JwtAuthenticationResponse(jwt);

    }
}
