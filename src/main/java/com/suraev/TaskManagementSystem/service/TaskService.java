package com.suraev.TaskManagementSystem.service;

import com.suraev.TaskManagementSystem.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface TaskService {
    /**
     * Создание задачи
     * @param taskData - ДТО задачи для создания
     * @return - ответ с ДТО задачи
     */
    ResponseEntity<TaskDTO> create(TaskCreateDto taskData);
    /**
     * Обновление задачи
     * @param taskData - ДТО для обновление задачи
     * @return - ответ с ДТО задачи
     */
    ResponseEntity<TaskDTO> update(TaskUpdateDTO taskData);
    /**
     * Получить задачу
     * @param id - идентификатор задачи
     * @return - ответ с ДТО задачи
     */
    ResponseEntity<TaskDTO> getTaskById(Long id);
    /**
     * Получить все задачи
     * @return список задач
     */
    List<TaskDTO> getAllTasks();
    /**
     * Удалить задачу
     * @param id - идентификатор задачи
     * @return - ответ с информцией об удалении
     */
    ResponseEntity<Void> delete(Long id);
    /**
     * Изменить статус задачи
     * @param id - идентификатор задачи
     * @param updatedTask - ДТО для обновления задачи
     * @return - ответ с ДТО задачи
     */
    ResponseEntity<TaskDTO> changeStatus(Long id, TaskStatusDTO updatedTask);
    /**
     * Изменить исполнителя задачи
     * @param id - идентификатор задачи
     * @param updatedTask - ДТО для обновления задачи
     * @return - ответ с ДТО задачи
     */
    ResponseEntity<TaskDTO> changeExecutor(Long id, TaskExecutorDTO updatedTask);
    /**
     * Создать комментарий для задачи
     * @param id - идентификатор задачи
     * @param comment - ДТО комментария
     * @return - ответ с ДТО комментария
     */
    ResponseEntity<CommentDTO> createComment(Long id,CommentCreateDTO comment);
    /**
     * Получить список задач по автору с фильтрацией и пагинацией
     * @param id - идентификатор автора
     * @param requestFiltration - параметры для фильтрации и пагинации
     * @return - page
     */
    Page<TaskFilterDTO> getTasksByAuthorFilteringAndPaging(Long id, Map<String, String> requestFiltration);
    /**
     * Получить список задач по исполнителю с фильтрацией и пагинацией
     * @param id - идентификатор исполнителя
     * @param requestFiltration - параметры для фильтрации и пагинации
     * @return - page
     */
    Page<TaskFilterDTO> getTasksByExecutorFilteringAndPaging(Long id, Map<String, String> requestFiltration);


}
