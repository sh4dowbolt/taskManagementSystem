package com.suraev.TaskManagementSystem.dto;

import com.suraev.TaskManagementSystem.domain.entity.enums.Priority;
import com.suraev.TaskManagementSystem.domain.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для ответа")
public class TaskDTO {
        @Schema(description = "Идентификатор задачи")
        private Long id;
        @Schema(description = "Наименование задачи", example = "Do chores")
        private String title;
        @Schema(description = "Описание задачи", example = "Mop the floor")
        private String description;
        @Schema(description = "Приоритет задачи", example = "LOW or MEDIUM or HIGH")
        private Priority priority;
        @Schema(description = "Статус задачи", example ="TODO or IN_PROGRESS or DONE or CLOSED")
        private Status status;
        @Schema(description = "Идентификатор автора задачи")
        public Long author;
        @Schema(description = "Идентификатор исполнителя задачи")
        public Long executor;



    }


