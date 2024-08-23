package com.suraev.TaskManagementSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для назначения исполнителя")
public class TaskExecutorDTO {
    @Column(name ="executor_id")
    @Schema(description = "Идентификатор исполнителя задачи")
    @NotNull
    public Long executor;

}
