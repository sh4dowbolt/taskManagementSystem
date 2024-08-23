package com.suraev.TaskManagementSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на авторизацию")
public class SignInRequest {
    @Schema(description = "Email пользователя", example = "test1@gmail.com")
    @Size(min = 4, max = 50, message = "Электронная почта должна содержать от 4 до 50 символов")
    @NotBlank(message = "Электронная почта не должна быть пустой")
    private String email;

    @Schema(description = "Пароль пользователя", example = "qwerty123")
    @Size(min = 8, max = 255, message = "Длина пароля должна быть от 8 до 255 символов")
    @NotBlank(message = "Пароль не должен быть пустым")
    private String password;

}
