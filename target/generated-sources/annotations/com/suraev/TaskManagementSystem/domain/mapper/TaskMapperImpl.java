package com.suraev.TaskManagementSystem.domain.mapper;

import com.suraev.TaskManagementSystem.domain.entity.Comment;
import com.suraev.TaskManagementSystem.domain.entity.Task;
import com.suraev.TaskManagementSystem.domain.entity.Task.TaskBuilder;
import com.suraev.TaskManagementSystem.dto.TaskCreateDto;
import com.suraev.TaskManagementSystem.dto.TaskDTO;
import com.suraev.TaskManagementSystem.dto.TaskDTO.TaskDTOBuilder;
import com.suraev.TaskManagementSystem.dto.TaskFilterDTO;
import com.suraev.TaskManagementSystem.dto.TaskFilterDTO.TaskFilterDTOBuilder;
import com.suraev.TaskManagementSystem.dto.TaskUpdateDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-23T22:52:06+0400",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Autowired
    private JsonNullableMapper jsonNullableMapper;

    @Override
    public Task map(TaskCreateDto dto) {
        if ( dto == null ) {
            return null;
        }

        TaskBuilder task = Task.builder();

        task.title( dto.getTitle() );
        task.description( dto.getDescription() );
        task.priority( dto.getPriority() );
        task.status( dto.getStatus() );
        task.executor( dto.getExecutor() );

        return task.build();
    }

    @Override
    public TaskDTO map(Task model) {
        if ( model == null ) {
            return null;
        }

        TaskDTOBuilder taskDTO = TaskDTO.builder();

        taskDTO.id( model.getId() );
        taskDTO.title( model.getTitle() );
        taskDTO.description( model.getDescription() );
        taskDTO.priority( model.getPriority() );
        taskDTO.status( model.getStatus() );
        taskDTO.author( model.getAuthor() );
        taskDTO.executor( model.getExecutor() );

        return taskDTO.build();
    }

    @Override
    public TaskFilterDTO mapFilter(Task model) {
        if ( model == null ) {
            return null;
        }

        TaskFilterDTOBuilder taskFilterDTO = TaskFilterDTO.builder();

        taskFilterDTO.id( model.getId() );
        taskFilterDTO.title( model.getTitle() );
        taskFilterDTO.description( model.getDescription() );
        taskFilterDTO.priority( model.getPriority() );
        taskFilterDTO.status( model.getStatus() );
        taskFilterDTO.author( model.getAuthor() );
        taskFilterDTO.executor( model.getExecutor() );
        List<Comment> list = model.getCommentList();
        if ( list != null ) {
            taskFilterDTO.commentList( new ArrayList<Comment>( list ) );
        }

        return taskFilterDTO.build();
    }

    @Override
    public void update(TaskUpdateDTO dto, Task model) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            model.setId( jsonNullableMapper.unwrap( dto.getId() ) );
        }
        if ( dto.getTitle() != null ) {
            model.setTitle( jsonNullableMapper.unwrap( dto.getTitle() ) );
        }
        if ( dto.getDescription() != null ) {
            model.setDescription( jsonNullableMapper.unwrap( dto.getDescription() ) );
        }
        if ( dto.getPriority() != null ) {
            model.setPriority( jsonNullableMapper.unwrap( dto.getPriority() ) );
        }
        if ( dto.getStatus() != null ) {
            model.setStatus( jsonNullableMapper.unwrap( dto.getStatus() ) );
        }
        if ( dto.getExecutor() != null ) {
            model.setExecutor( jsonNullableMapper.unwrap( dto.getExecutor() ) );
        }
    }
}
