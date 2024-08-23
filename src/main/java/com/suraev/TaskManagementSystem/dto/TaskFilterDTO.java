package com.suraev.TaskManagementSystem.dto;

import com.suraev.TaskManagementSystem.domain.entity.Comment;
import com.suraev.TaskManagementSystem.domain.entity.enums.Priority;
import com.suraev.TaskManagementSystem.domain.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "DTO для вывода офильтрованных задач")
public class TaskFilterDTO {
        @Schema(description = "Идентификатор задачи")
        private Long id;
        @Schema(description = "Наименование задачи")
        private String title;
        @Schema(description = "Описание задачи")
        private String description;
        @Schema(description = "Приоритет задачи")
        private Priority priority;
        @Schema(description = "Статус задачи")
        private Status status;
        @Schema(description = "Идентификатор автора задачи")
        public Long author;
        @Schema(description = "Идентификатор исполнителя задачи")
        public Long executor;
        @Schema(description = "Список комментариев к задаче")
        private List<Comment> commentList;
    }

