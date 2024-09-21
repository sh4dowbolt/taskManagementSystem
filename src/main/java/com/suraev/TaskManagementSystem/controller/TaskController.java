package com.suraev.TaskManagementSystem.controller;

import com.suraev.TaskManagementSystem.domain.entity.Task;
import com.suraev.TaskManagementSystem.dto.*;
import com.suraev.TaskManagementSystem.service.TaskServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name="JWT")
@Tag(name="Управления задачами", description = "Панель управления задачами")
public class TaskController {

    private final TaskServiceImpl taskService;


    @PostMapping(value = "/task", produces = "application/json")
    @Operation(summary = "Создание задачи", description = "Позволяет создавать задачи")
    @ApiResponses({
    @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = TaskDTO.class), mediaType = "application/json")})})
    @SecurityRequirement(name="JWT")
    public ResponseEntity<TaskDTO> createTask(
            @RequestBody @Valid @Parameter(
                    description = "Задача для создания") TaskCreateDto taskData){
        return taskService.create(taskData);
    }

    @PutMapping(value = "/task")
    @Operation(summary = "Обновление задачи", description = "Позволяет обновлять задачи")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = TaskDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "409", content = {@Content(schema = @Schema(), mediaType = "application/json")})})
    @SecurityRequirement(name="JWT")
    public ResponseEntity<TaskDTO> updateTask(
            @RequestBody @Valid @Parameter(description = "Задача для обновления") TaskUpdateDTO taskData){
        return taskService.update(taskData);
    }

    @GetMapping(value = "/task/{id}")
    @Operation(summary = "Получить задачу по ID", description = "Позволяет получать задачу по ID")
    @SecurityRequirement(name="JWT")
    public ResponseEntity<TaskDTO> getTask(
            @PathVariable @Parameter(description = "Идентификатор задачи") String id) {
        return taskService.getTaskById(Long.valueOf(id));
    }

    @GetMapping(value = "/task")
    @Operation(summary = "Получить все задачи", description = "Позволяет получить список всех задач")
    @SecurityRequirement(name="JWT")
    public List<TaskDTO> getAllTask() {
        return taskService.getAllTasks();
    }

    @DeleteMapping(value = "/task/{id}")
    @Operation(summary = "Удалить задачу", description = "Удалить задачу по ID")
    @SecurityRequirement(name="JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema(implementation = TaskDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "409", content = {@Content(schema = @Schema(), mediaType = "application/json")})})
    public ResponseEntity<Void> deleteTask(
            @PathVariable @Parameter(description = "Идентификатор задачи") Long id) {
        return taskService.delete(id);
    }

    @PatchMapping("/task/{id}/changeStatus")
    @Operation(summary = "Поменять статус задачи", description = "Позволяет изменить статус у задачи")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = TaskDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "409", content = {@Content(schema = @Schema(), mediaType = "application/json")})})
    @SecurityRequirement(name="JWT")
    public ResponseEntity<TaskDTO> changeStatus(
            @PathVariable @Parameter(description = "Идентификатор задачи") Long id,
            @Valid @RequestBody @Parameter(description = "Статус для обновления") TaskStatusDTO task ) {
        return taskService.changeStatus(id, task);
    }
    @PatchMapping("/task/{id}/changeExecutor")
    @Operation(summary = "Назначить исполнителя задачи", description = "Позволяет назначить исполнителя  задачи")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = TaskDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "409", content = {@Content(schema = @Schema(), mediaType = "application/json")})})
    @SecurityRequirement(name="JWT")
    public ResponseEntity<TaskDTO> changeExecutor(
            @PathVariable @Parameter(description = "Идентификатор задачи") Long id,
            @Valid @RequestBody @Parameter(description = "Идентификатор исполнителя") TaskExecutorDTO task ) {
        return taskService.changeExecutor(id, task);
    }

    @GetMapping(value = "/task/getAllTasksByAuthor/{id}")
    @Operation(summary = "Получить задачи автора", description = "Позволяет получить задачи автора по ID")
    @SecurityRequirement(name="JWT")
    public List<TaskDTO> getTasksByAuthorId(
            @PathVariable @Parameter(description = "Идентификатор автора") Long id) {
        return taskService.getAllTasksByAuthor(id);
    }

    @GetMapping(value = "/task/getAllTasksByExecutor/{id}")
    @Operation(summary = "Получить задачи исполнителя", description = "Позволяет получить задачи исполнителя по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = TaskDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(), mediaType = "application/json")})})
    @SecurityRequirement(name="JWT")
    public List<TaskDTO> getTasksByExecutorId(@PathVariable @Parameter(description = "Идентификатор исполнителя") Long id) {
        return taskService.getAllTasksByExecutor(id);
    }

    @PostMapping(value = "/task/{taskId}/comment")
    @Operation(summary = "Создание комментария", description = "Позволяет создать комментарий к определенной задаче")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = TaskDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "409", content = {@Content(schema = @Schema(), mediaType = "application/json")})})
    @SecurityRequirement(name="JWT")
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable @Parameter(description = "Идентификатор задачи")Long taskId ,
            @Valid @RequestBody @Parameter(description = "Комментарий") CommentCreateDTO comment) {

        return taskService.createComment(taskId,comment);
    }

    @GetMapping(value = "/task/getTasks/byAuthor/{author_id}")
    @Operation(summary = "Получение задачи конкретного автора",
            description = "Позволяет получать задачи конкретного автора с обеспечением фильтрации и пагинации вывода")
    @SecurityRequirement(name="JWT")
    public Page<TaskFilterDTO> getTasksByAuthorUsingFilteringAndPaging(@PathVariable (value = "author_id") @Parameter(description = "Идентификатор автора") Long authorId,
                                                                       @ParameterObject@RequestParam @Valid @Parameter(description = """
                                                                               Параметры для осуществления фильтрации и пагинации.\nДля фильтрации используются параметры: по приоритету - ?priority, по статусу ?status, по заголовку ?title, по описанию ?description.
                                                                               \nДля пагинации параметры: выбор страницы - ?pageNumber, размер страницы - ?pageSize
                                                                               \nВсе параметры опциональны""",
                                                                       example = "{\n   \"title\":\"Do chores\",\n   \"description\":\"handle with test task\",\n   \"status\":\"IN_PROGRESS\",\n   \"priority\":\"HIGH\",\n\n   \"pageNumber\":0,\n   \"pageSize\":4\n}") Map<String, String> requestFiltration)
     {
         return taskService.getTasksByAuthorFilteringAndPaging(authorId, requestFiltration);
    }

    @GetMapping(value = "/task/getTasks/byExecutor/{executor_id}")
    @Operation(summary = "Получение задачи конкретного исполнителя",
            description = "Позволяет получать задачи конкретного исполнителя с обеспечением фильтрации и пагинации вывода")
    @SecurityRequirement(name="JWT")
    public Page<TaskFilterDTO> getTasksByExecutorUsingFilteringAndPaging(@PathVariable  (value = "executor_id") @Parameter(description = "Идентификатор исполнителя")Long executorId,
                                                                         @ParameterObject@RequestParam @Valid @Parameter(description = """
                                                                               Параметры для осуществления фильтрации и пагинации.\nДля фильтрации используются параметры: по приоритету - ?priority, по статусу ?status, по заголовку ?title, по описанию ?description.
                                                                               \nДля пагинации параметры: выбор страницы - ?pageNumber, размер страницы - ?pageSize
                                                                               \nВсе параметры опциональны""",
                                                                                 example = "{\n   \"title\":\"Do chores\",\n   \"description\":\"handle with test task\",\n   \"status\":\"IN_PROGRESS\",\n   \"priority\":\"HIGH\",\n\n   \"pageNumber\":0,\n   \"pageSize\":4\n}") Map<String, String> requestFiltration)
    {
        return taskService.getTasksByExecutorFilteringAndPaging(executorId, requestFiltration);
    }







}
