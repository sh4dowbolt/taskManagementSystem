package com.suraev.TaskManagementSystem.service;

import com.suraev.TaskManagementSystem.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    User save(User user);
    User create(User user);
    User getByEmail(String email);
    UserDetailsService userDetailsService();
    User getCurrentUser();
}
