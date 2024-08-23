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
@Schema(description = "Запрос на регистрацию")
public class SignUpRequest {
    @Schema(description = "Email пользователя", example = "test3@gmail.com")
    @Size(min = 4, max = 50, message = "Электронная почта должна содержать от 4 до 50 символов")
    @NotBlank(message = "Email shouldn't be empty")
    private String email;
    @Schema(description = "Пароль пользователя", example = "qwerty123")
    @Size(min = 8, max = 255, message = "Длина пароля должна быть от 8 до 255 символов")
    @NotBlank(message = "Email shouldn't be empty")
    private String password;

}
