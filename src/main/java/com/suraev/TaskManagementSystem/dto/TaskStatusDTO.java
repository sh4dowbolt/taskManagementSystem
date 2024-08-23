package com.suraev.TaskManagementSystem.dto;

import com.suraev.TaskManagementSystem.domain.entity.enums.Status;
import com.suraev.TaskManagementSystem.validation.EnumNamePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для обновления статуса")
public class TaskStatusDTO {
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Schema(description = "Статус задачи")
    @EnumNamePattern(regexp = "TODO|IN_PROGRESS|DONE|CLOSED")
    private Status status;
}
