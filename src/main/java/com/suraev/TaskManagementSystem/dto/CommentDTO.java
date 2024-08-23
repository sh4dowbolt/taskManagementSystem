package com.suraev.TaskManagementSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для вывода комментария")
public class CommentDTO {
    @Schema(description = "Идентификатор комментария")
    private Long id;
    @Schema(description = "Содержимое комментария", example = "meow-meow")
    private String description;
    @Schema(description = "Идентификатор автора")
    private Long authorId;

}
