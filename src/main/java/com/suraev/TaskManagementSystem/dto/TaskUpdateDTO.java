package com.suraev.TaskManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.suraev.TaskManagementSystem.domain.entity.enums.Priority;
import com.suraev.TaskManagementSystem.domain.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.openapitools.jackson.nullable.JsonNullable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateDTO {
    @NotNull
    @Schema(description = "Идентификатор задачи", example = "1")
    @JsonProperty("id")
    private JsonNullable<Long> id;
    @NotNull
    @Schema(description = "Наименование задачи", example = "make some tea")

    private JsonNullable<String> title;
    @Schema(description = "Описание задачи", example = "take a cup, sugar, milk")
    @NotNull
    private JsonNullable<String> description;
    @Enumerated(EnumType.STRING)
    @NotNull
    @Schema(description = "Приоритет задачи", example = "LOW, HIGH,MEDIUM")
    private JsonNullable<Priority> priority;
    @Enumerated(EnumType.STRING)
    @NotNull
    @Schema(description = "Статус задачи", example = "TODO, IN_PROGRESS,DONE,CLOSED")
    private JsonNullable<Status> status;

    @NotNull
    @Schema(description = "Исполнитель задачи", example = "1")
    @JsonProperty("executor_id")
    public JsonNullable<Long> executor;
}
