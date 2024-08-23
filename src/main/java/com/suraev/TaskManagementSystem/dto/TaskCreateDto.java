package com.suraev.TaskManagementSystem.dto;

import com.suraev.TaskManagementSystem.domain.entity.enums.Priority;
import com.suraev.TaskManagementSystem.domain.entity.enums.Status;
import com.suraev.TaskManagementSystem.validation.EnumNamePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для создания задачи")
public class TaskCreateDto {

        @Column(name = "title")
        @Schema(description = "Наименование задачи", example = "Do chores")
        @NotBlank
        private String title;

        @Column(name = "description")
        @Schema(description = "Описание задачи", example = "Mop the floor")
        @NotBlank
        private String description;

        @Enumerated(EnumType.STRING)
        @Column(name = "priority")
        @Schema(description = "Приоритет задачи", example = "LOW or MEDIUM or HIGH")
        @EnumNamePattern(regexp = "LOW|MEDIUM|HIGH")
        private Priority priority;

        @Enumerated(EnumType.STRING)
        @Column(name = "status")
        @Schema(description = "Статус задачи", example ="TODO or IN_PROGRESS or DONE or CLOSED")
        @EnumNamePattern(regexp = "TODO|IN_PROGRESS|DONE|CLOSED")
        private Status status;

        @Column(name ="executor_id")
        @Schema(description = "Идентификатор исполнителя задачи")
        public Long executor;
}
