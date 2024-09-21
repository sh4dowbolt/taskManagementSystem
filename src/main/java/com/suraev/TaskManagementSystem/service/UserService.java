package com.suraev.TaskManagementSystem.service;

import com.suraev.TaskManagementSystem.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    /**
     * Сохранение пользователя
     * @param user - данные пользователя
     * @return - пользователя
     */
    User save(User user);
    /**
     * Создание пользователя
     * @param user - данные пользователя
     * @return - пользователя
     */
    User create(User user);
    /**
     * Получить пользователя по email
     * @param email - email
     * @return - пользователя
     */
    User getByEmail(String email);

    /**
     * Возвращает UserDetailsService
     * @return - пользователя
     */
    UserDetailsService userDetailsService();

    /**
     *Получить текущего пользователя
     * @return - пользователя
     */
    User getCurrentUser();
}
