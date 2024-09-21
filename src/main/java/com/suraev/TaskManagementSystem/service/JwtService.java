package com.suraev.TaskManagementSystem.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    /**
     * Извлечение username из токена
     * @param token - токен
     * @return - username
     */
    String extractUserName(String token);
    /**
     * Генерация токена
     * @param userDetails - данные пользователя
     * @return - токен
     */
    String generateToken(UserDetails userDetails);
    /**
     * Проверка токена на валидность
     * @param token - токен
     * @param userDetails - данные пользователя
     */
    boolean isTokenValid(String token, UserDetails userDetails);

}
