package com.suraev.TaskManagementSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "DTO для создания комментария")
public class CommentCreateDTO {
    @Schema(description = "Содержимое комментария")
    @NotBlank
    private String description;

}
