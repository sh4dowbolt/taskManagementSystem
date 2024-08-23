package com.suraev.TaskManagementSystem.domain.mapper;

import com.suraev.TaskManagementSystem.domain.entity.Task;
import com.suraev.TaskManagementSystem.dto.TaskCreateDto;
import com.suraev.TaskManagementSystem.dto.TaskDTO;
import com.suraev.TaskManagementSystem.dto.TaskFilterDTO;
import com.suraev.TaskManagementSystem.dto.TaskUpdateDTO;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TaskMapper {
     Task map(TaskCreateDto dto);
     TaskDTO map(Task model);
     TaskFilterDTO mapFilter(Task model);
     void update(TaskUpdateDTO dto, @MappingTarget Task model);
}
