package com.suraev.TaskManagementSystem.service;

import com.suraev.TaskManagementSystem.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface TaskService {
    ResponseEntity<TaskDTO> create(TaskCreateDto taskData);
    ResponseEntity<TaskDTO> update(TaskUpdateDTO taskData);
    ResponseEntity<TaskDTO> getTaskById(Long id);
    List<TaskDTO> getAllTasks();
    ResponseEntity<Void> delete(Long id);
    ResponseEntity<TaskDTO> changeStatus(Long id, TaskStatusDTO updatedTask);
    ResponseEntity<TaskDTO> changeExecutor(Long id, TaskExecutorDTO updatedTask);
    ResponseEntity<CommentDTO> createComment(Long id,CommentCreateDTO comment);
    Page<TaskFilterDTO> getTasksByAuthorFilteringAndPaging(Long id, Map<String, String> requestFiltration);
    Page<TaskFilterDTO> getTasksByExecutorFilteringAndPaging(Long id, Map<String, String> requestFiltration);


}
