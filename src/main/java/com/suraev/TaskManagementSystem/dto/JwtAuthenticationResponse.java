package com.suraev.TaskManagementSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с токеном доступа")
public class JwtAuthenticationResponse {
    @Schema(description = "Токен доступа", example = "a3453yJhOzUxMiJ9.dWiOiJhZGV45...")
    private String token;
}
