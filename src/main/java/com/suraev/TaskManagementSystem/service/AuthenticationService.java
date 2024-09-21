package com.suraev.TaskManagementSystem.service;

import com.suraev.TaskManagementSystem.dto.JwtAuthenticationResponse;
import com.suraev.TaskManagementSystem.dto.SignInRequest;
import com.suraev.TaskManagementSystem.dto.SignUpRequest;

public interface AuthenticationService {
    /**
     * Запрос на регистрацию
     * @param request - тело запроса
     * @return - ответ с токеном доступа
     */
    JwtAuthenticationResponse signUp(SignUpRequest request);

    /**
     * Запрос на авторизацию
     * @param request - тело запроса
     * @return - ответ с токеном доступа
     */
    JwtAuthenticationResponse signIn(SignInRequest request);

}
