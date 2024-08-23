package com.suraev.TaskManagementSystem.controller;


import com.suraev.TaskManagementSystem.domain.entity.Task;
import com.suraev.TaskManagementSystem.dto.*;
import com.suraev.TaskManagementSystem.service.TaskServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@Tag("TaskController")
class TaskControllerTest {
    @InjectMocks
    private TaskController taskController;
    @Mock
    private TaskServiceImpl taskService;

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "createTask")
    class createTask {
        @Test
        @Order(1)
        void createTaskWithOkStatus() throws URISyntaxException {
            // подготовка DTO
            TaskCreateDto task = TaskCreateDto.builder().build();
            TaskDTO taskFromDB = TaskDTO.builder().id(1L).author(1L).build();

            when(taskService.create(task)).thenReturn(ResponseEntity.ok().body(taskFromDB));

            var actualResult = taskController.createTask(task);
            assertThat(actualResult.getStatusCode().value()).isEqualTo(200);
        }

        @Test
        @Order(2)
        void createTaskWithBadRequest() throws URISyntaxException {
            //подготовка DTO
            TaskCreateDto task = TaskCreateDto.builder().build();
            TaskDTO taskFromDB = TaskDTO.builder().id(1L).author(1L).build();

            when(taskService.create(task)).thenReturn(ResponseEntity.badRequest().body(taskFromDB));

            var actualResult = taskController.createTask(task);
            assertThat(actualResult.getStatusCode().value()).isEqualTo(400);
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "updateTask")
    class updateTask {
        @Test
        @Order(1)
        void updateTaskWithOkStatus() {
            //подготовка DTO
            TaskUpdateDTO task = TaskUpdateDTO.builder().build();
            TaskDTO taskFromDB = TaskDTO.builder().id(1L).author(1L).build();

            when(taskService.update(task)).thenReturn(ResponseEntity.ok().body(taskFromDB));
            var actualResult = taskController.updateTask(task);
            assertThat(actualResult.getStatusCode().value()).isEqualTo(200);
        }

        @Test
        @Order(2)
        void updateTaskWithBadRequest() {
            //подготовка DTO
            TaskUpdateDTO task = TaskUpdateDTO.builder().build();
            TaskDTO taskFromDB = TaskDTO.builder().id(1L).author(1L).build();

            when(taskService.update(task)).thenReturn(ResponseEntity.badRequest().body(taskFromDB));

            var actualResult = taskController.updateTask(task);
            assertThat(actualResult.getStatusCode().value()).isEqualTo(400);
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "getTaskByID")
    class getTask {
        @Test
        @Order(1)
        void getTaskWithOkStatus() {
            String id = "1";
            when(taskService.getTaskById(Long.valueOf(id))).thenReturn(ResponseEntity.ok().build());

            var actualResult = taskController.getTask(id);

            assertThat(actualResult.getStatusCode().value()).isEqualTo(200);
        }

        @Test
        @Order(2)
        void getTaskWithNotFound() {
            String id = "1";
            when(taskService.getTaskById(Long.valueOf(id))).thenReturn(ResponseEntity.notFound().build());

            var actualResult = taskController.getTask(id);

            assertThat(actualResult.getStatusCode().value()).isEqualTo(404);
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "getAllTask")
    class getAllTask {
        @Test
        @Order(1)
        void getTaskWithOkStatus() {
            //подготовка данных
            TaskDTO taskDTO = TaskDTO.builder().build();
            List<TaskDTO> dtoList = List.of(taskDTO);
            when(taskService.getAllTasks()).thenReturn(dtoList);

            var actualResult = taskController.getAllTask();

            assertThat(actualResult.size()).isGreaterThan(0);
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "deleteTask")
    class deleteTask {
        @Test
        @Order(1)
        void deleteTaskWithNoContent() {
            Long id = 1L;
            when(taskService.delete(id)).thenReturn(ResponseEntity.noContent().build());

            var actualResult = taskController.deleteTask(id);

            assertThat(actualResult.getStatusCode().value()).isEqualTo(204);
        }

        @Test
        @Order(2)
        void deleteTaskWithConflict() {
            Long id = 1L;
            when(taskService.delete(id)).thenReturn(ResponseEntity.status(409).build());

            ResponseEntity<Void> actualResult = taskController.deleteTask(id);

            assertThat(actualResult.getStatusCode().value()).isEqualTo(409);
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "changeStatus")
    class changeStatus {
        @Test
        @Order(1)
        void changeStatusWithOk() {
            Long id = 1L;
            // подготовка данных
            TaskStatusDTO taskUpdate = TaskStatusDTO.builder().build();

            when(taskService.changeStatus(id, taskUpdate)).thenReturn(ResponseEntity.ok().build());

            var actualResult = taskController.changeStatus(id, taskUpdate);

            assertThat(actualResult.getStatusCode().value()).isEqualTo(200);
        }

        @Test
        @Order(2)
        void changeStatusWithConflict() {
            Long id = 1L;
            // подготовка данных
            TaskStatusDTO taskUpdate = TaskStatusDTO.builder().build();

            when(taskService.changeStatus(id, taskUpdate)).thenReturn(ResponseEntity.status(409).build());

            var actualResult = taskController.changeStatus(id, taskUpdate);

            assertThat(actualResult.getStatusCode().value()).isEqualTo(409);
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "changeExecutor")
    class changeExecutor {
        @Test
        @Order(1)
        void changeExecutorWithOk() {
            Long id = 1L;
            // подготовка данных
            TaskExecutorDTO taskUpdate = TaskExecutorDTO.builder().build();

            when(taskService.changeExecutor(id, taskUpdate)).thenReturn(ResponseEntity.ok().build());

            var actualResult = taskController.changeExecutor(id, taskUpdate);

            assertThat(actualResult.getStatusCode().value()).isEqualTo(200);
        }

        @Test
        @Order(2)
        void changeExecutorWithConflict() {
            Long id = 1L;
            // подготовка данных
            TaskExecutorDTO taskUpdate = TaskExecutorDTO.builder().build();

            when(taskService.changeExecutor(id, taskUpdate)).thenReturn(ResponseEntity.status(409).build());

            var actualResult = taskController.changeExecutor(id, taskUpdate);

            assertThat(actualResult.getStatusCode().value()).isEqualTo(409);
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "getTaskByAuthorAndExecutor")
    class getTaskByAuthorId {
        @Test
        @Order(1)
        void getTaskByAuthor() {
            //подготовка данных
            Long id = 1L;
            TaskDTO taskDTO = TaskDTO.builder().build();
            List<TaskDTO> dtoList = List.of(taskDTO);

            when(taskService.getAllTasksByAuthor(id)).thenReturn(dtoList);

            var actualResult = taskController.getTasksByAuthorId(id);

            assertThat(actualResult.size()).isGreaterThan(0);
        }

        @Test
        @Order(2)
        void getTaskByExecutor() {
            //подготовка данных
            Long id = 1L;
            TaskDTO taskDTO = TaskDTO.builder().build();
            List<TaskDTO> dtoList = List.of(taskDTO);
            when(taskService.getAllTasksByExecutor(id)).thenReturn(dtoList);

            var actualResult = taskController.getTasksByExecutorId(id);

            assertThat(actualResult.size()).isGreaterThan(0);
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "createComment")
    class createComment {
        @Test
        @Order(1)
        void createCommentWithOk() {
            //подготовка данных
            Long id = 1L;
            CommentCreateDTO comment = CommentCreateDTO.builder().description("meow-meow").build();
            when(taskService.createComment(id, comment)).thenReturn(ResponseEntity.ok().build());

            var actualResult = taskController.createComment(id, comment);

            assertThat(actualResult.getStatusCode().value()).isEqualTo(200);
        }

        @Test
        @Order(2)
        void createCommentWithConflict() {
            //подготовка данных
            Long id = 1L;
            CommentCreateDTO comment = CommentCreateDTO.builder().description("meow-meow").build();
            when(taskService.createComment(id, comment)).thenReturn(ResponseEntity.status(409).build());

            var actualResult = taskController.createComment(id, comment);

            assertThat(actualResult.getStatusCode().value()).isEqualTo(409);
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "getTaskByAuthorFilterAndPaging")
    class getTaskByAuthorFilterAndPaging {
        @Test
        @Order(1)
        void getPageOfTask() {
            // подготовка данных
            Long id = 1L;
            Map<String, String> requestFiltration = getRequestFiltration();

            doAnswer(x -> prepareExpectedResult()).when(taskService).getTasksByAuthorFilteringAndPaging(id, requestFiltration);

            var actualResult = taskController.getTasksByAuthorUsingFilteringAndPaging(id, requestFiltration);

            assertAll(
                    () -> assertThat(actualResult.getTotalElements()).isEqualTo(prepareExpectedResult().getTotalElements()),
                    () -> verify(taskService, times(1)).getTasksByAuthorFilteringAndPaging(id, requestFiltration));
        }

        private Map<String, String> getRequestFiltration() {
            Map<String, String> requestFiltration = new HashMap<>();
            requestFiltration.put("priority", "LOW");
            requestFiltration.put("status", "DONE");
            requestFiltration.put("title", "chores");
            requestFiltration.put("description", "mop the floor");
            return requestFiltration;
        }

        private Page<Task> prepareExpectedResult() {
            int pageNumber = 0;
            int pageSize = 4;

            List<Task> taskList = new ArrayList<>();
            taskList.add(Task.builder().author(1L).title("mop the floor").build());
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            return new PageImpl<>(taskList, pageable, taskList.size());
        }
    }


    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "getTaskByAuthorFilterAndPaging")
    class getTaskByExecutorFilterAndPaging {
        @Test
        @Order(1)
        void getPageOfTask() {
            // подготовка данных
            Long id = 1L;
            Map<String, String> requestFiltration = getRequestFiltration();

            doAnswer(x -> prepareExpectedResult()).when(taskService).getTasksByExecutorFilteringAndPaging(id, requestFiltration);

            var actualResult = taskController.getTasksByExecutorUsingFilteringAndPaging(id, requestFiltration);

            assertAll(
                    () -> assertThat(actualResult.getTotalElements()).isEqualTo(prepareExpectedResult().getTotalElements()),
                    () -> verify(taskService, times(1)).getTasksByExecutorFilteringAndPaging(id, requestFiltration));
        }

        private Map<String, String> getRequestFiltration() {
            Map<String, String> requestFiltration = new HashMap<>();
            requestFiltration.put("priority", "LOW");
            requestFiltration.put("status", "AWAITING");
            requestFiltration.put("title", "chores");
            requestFiltration.put("description", "mop the floor");
            return requestFiltration;
        }
        private Page<Task> prepareExpectedResult() {
            int pageNumber = 0;
            int pageSize = 4;

            List<Task> taskList = new ArrayList<>();
            taskList.add(Task.builder().executor(1L).title("mop the floor").build());
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            return new PageImpl<>(taskList, pageable, taskList.size());
        }
    }
}











