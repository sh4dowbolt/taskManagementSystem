package com.suraev.TaskManagementSystem.service;


import com.suraev.TaskManagementSystem.exception.BadRequestAlertException;
import com.suraev.TaskManagementSystem.domain.entity.User;
import com.suraev.TaskManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.zalando.problem.Status;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository repository;
    /**
     * Сохранение пользователя
     * @param user - пользователь
     * @return - созданный пользователь
     */
    public User save(User user) {
        return repository.save(user);
    }

    /**
     * Создание пользователя
     *
     * @param user - пользователь
     * @return созданный пользователь
     */

    public User create(User user) {
        if(repository.existsByEmail(user.getEmail())) {
            throw new BadRequestAlertException("User exists", Status.BAD_REQUEST);
        }
        return save(user);
    }

    /**
     * Получение пользователя по email
     * @param email - email
     * @return пользователь или exception
     */
    public User getByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new BadRequestAlertException("User does not exist", Status.NOT_FOUND));
    }

    /**
     * Получение пользователя по email для пользователя для Spring Security
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {

        return this::getByEmail;
    }

    /** Получение текущего пользователя
     *
     * @return текущий пользователь
     */

    public User getCurrentUser() {
        //Получение email пользователя из контекста Spring Security
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByEmail(email);
    }

  /*  public void setExecutorOfTask() {
        User currentUser = getCurrentUser();
        currentUser.setRole(Role.ROLE_EXECUTOR);
    }*/

}
