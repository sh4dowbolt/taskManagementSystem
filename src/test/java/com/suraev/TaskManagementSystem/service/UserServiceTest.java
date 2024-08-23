package com.suraev.TaskManagementSystem.service;

import com.suraev.TaskManagementSystem.exception.BadRequestAlertException;
import com.suraev.TaskManagementSystem.domain.entity.User;
import com.suraev.TaskManagementSystem.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith({MockitoExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Tag("UserService")
class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "saveUser")
    class saveUser {
        @Test
        @Order(1)
        void saveUser() {
            User newUser = User.builder().email("test@test.com").password("secret").build();
            User userFromDB = User.builder().id(1L).email("test@test.com").password("secret").build();
            Mockito.when(userRepository.save(newUser)).thenReturn(userFromDB);

            var actualResult = userService.save(newUser);

            assertThat(actualResult.getEmail()).isEqualTo(userFromDB.getEmail());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "createUser")
    class createUser {
        @Test
        @Order(1)
        void throwExceptionIfUserExisted() {
            User userFromDB = User.builder().id(1L).email("test@test.com").password("secret").build();
            Mockito.when(userRepository.existsByEmail(userFromDB.getEmail())).thenReturn(true);
            assertThrows(BadRequestAlertException.class, () -> userService.create(userFromDB));
        }

        @Test
        @Order(2)
        void shouldCreateUser() {
            User userFromDB = User.builder().id(1L).email("test@test.com").password("secret").build();

            Mockito.when(userRepository.existsByEmail(userFromDB.getEmail())).thenReturn(false);
            Mockito.when(userService.save(userFromDB)).thenReturn(userFromDB);

            var actualResult = userService.create(userFromDB);
            assertThat(actualResult.getId()).isNotNull();
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "getByEmail")
    class getByEmail {
        @Test
        @Order(1)
        void throwExceptionIfUserDoesNotExist() {
            User userFromDB = User.builder().id(1L).email("email").password("secret").build();
            Mockito.when(userRepository.findByEmail(userFromDB.getEmail())).thenReturn(Optional.empty());
            assertThrows(BadRequestAlertException.class, () -> userService.getByEmail("email"));
        }

        @Test
        @Order(2)
        void shouldGetUserByEmail() {
            User userFromDB = User.builder().id(1L).email("test@test.com").password("secret").build();
            Mockito.when(userRepository.findByEmail(userFromDB.getEmail())).thenReturn(Optional.of(userFromDB));

            var actualResult = userService.getByEmail("test@test.com");
            assertThat(actualResult.getId()).isNotNull();
        }
    }

//    @Nested
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    @Tag(value = "userDetailsService")
//    class getUserDetailsService {
//        @Test
//        @Order(1)
//        void getUserDetailService() {
//            User userFromDB = User.builder().email("test@test.com").password("secret").build();
//
//            Mockito.when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(userFromDB));
//
//            Mockito.when(userService.getByEmail("test@test.com")).thenReturn(userFromDB);
//
//
//            UserDetailsService actualResult = userService.userDetailsService();
//
//            assertThat(actualResult).isNotNull();
//        }

//        @Nested
//        @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//        @Tag(value = "getCurrentUser")
//        class getCurrentUser {
//            @Test
//            @Order(1)
//            void getCurrentUser() {
//                User userFromDB = User.builder().email("test@gmail.com").password("secret").build();
//                Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("test@gmail.com");
//                Mockito.when(userService.getByEmail("test@gmail.com")).thenReturn(userFromDB);
//                User actualResult = userService.getCurrentUser();
//
//
//                assertThat(actualResult.getEmail()).isEqualTo("test@gmail.com");
//            }
//
//        }
    }
