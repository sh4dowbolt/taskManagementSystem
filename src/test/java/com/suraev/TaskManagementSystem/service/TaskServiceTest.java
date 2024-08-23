package com.suraev.TaskManagementSystem.service;

import com.suraev.TaskManagementSystem.dto.*;
import com.suraev.TaskManagementSystem.exception.BadRequestAlertException;
import com.suraev.TaskManagementSystem.domain.entity.Comment;
import com.suraev.TaskManagementSystem.domain.entity.Task;
import com.suraev.TaskManagementSystem.domain.entity.enums.Priority;
import com.suraev.TaskManagementSystem.domain.entity.User;
import com.suraev.TaskManagementSystem.domain.entity.enums.Status;
import com.suraev.TaskManagementSystem.domain.mapper.CommentMapper;
import com.suraev.TaskManagementSystem.domain.mapper.TaskMapper;
import com.suraev.TaskManagementSystem.repository.TaskRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

import static com.suraev.TaskManagementSystem.util.SpecificationUtil.likeAuthorId;
import static com.suraev.TaskManagementSystem.util.SpecificationUtil.likeExecutorId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.refEq;


@ExtendWith({MockitoExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TaskServiceTest {
    @InjectMocks
    private TaskServiceImpl taskService;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private CommentMapper commentMapper;

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "CreateMethod")
    class CreateMethod {

        @Test
        @Order(1)
        void IfExistingExecutor_SaveInDB() {
            // подготовка данных
            User currentUser = User.builder().id(1L).build();
            TaskCreateDto taskDTOtoDB = TaskCreateDto.builder().executor(1L).build();
            TaskDTO taskDTOFromDB = TaskDTO.builder().id(1L).executor(1L).build();
            Task taskToDB = Task.builder().executor(1L).build();
            Task taskFromDB = Task.builder().id(1L).executor(1L).build();

            // получаем id текущего юзера
            Mockito.doReturn(currentUser).when(userService).getCurrentUser();
            //устанавливаем id в entity
            taskToDB.setAuthor(currentUser.getId());
            //мапим из DTO в entity
            Mockito.when(taskMapper.map(taskDTOtoDB)).thenReturn(taskToDB);
            //проверяем в бд существование пользователя, который будет в качестве исполнителя
            Mockito.doReturn(true).when(taskRepository).existsById(taskToDB.getExecutor());
            // сохраняем entity в БД
            Mockito.when(taskRepository.save(taskToDB)).thenReturn(taskFromDB);
            // мапим сущность в DTO
            Mockito.when(taskMapper.map(taskFromDB)).thenReturn(taskDTOFromDB);

            ResponseEntity<TaskDTO> actualResult = taskService.create(taskDTOtoDB);

            assertThat(Objects.requireNonNull(actualResult.getBody()).getExecutor()).isNotNull();
            assertThat(actualResult.getBody().getExecutor()).isEqualTo(taskDTOtoDB.getExecutor());
        }

        @Test
        @Order(2)
        void IfNotExistingExecutor_SetNullAndSaveDb() {
            // подготовка данных
            User currentUser = User.builder().id(1L).build();
            TaskCreateDto taskDTOtoDB = TaskCreateDto.builder().executor(123L).build();
            TaskDTO taskDTOFromDB = TaskDTO.builder().id(1L).executor(null).build();
            Task taskToDB = Task.builder().executor(1L).build();
            Task taskFromDB = Task.builder().id(1L).executor(null).build();

            // получаем id текущего юзера
            Mockito.doReturn(currentUser).when(userService).getCurrentUser();
            //устанавливаем id в entity
            taskToDB.setAuthor(currentUser.getId());
            //мапим из DTO в entity
            Mockito.when(taskMapper.map(taskDTOtoDB)).thenReturn(taskToDB);
            //проверяем в бд существование пользователя, который будет в качестве исполнителя
            Mockito.doReturn(false).when(taskRepository).existsById(taskToDB.getExecutor());
            // устанавливаем null в поле исполнителя
            taskToDB.setExecutor(null);
            // сохраняем entity в БД
            Mockito.when(taskRepository.save(taskToDB)).thenReturn(taskFromDB);
            // мапим сущность в DTO
            Mockito.when(taskMapper.map(taskFromDB)).thenReturn(taskDTOFromDB);

            ResponseEntity<TaskDTO> actualResult = taskService.create(taskDTOtoDB);

            assertThat(Objects.requireNonNull(actualResult.getBody()).getExecutor()).isNull();
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "UpdateMethod")
    class UpdateMethod {
        @Test
        @Order(1)
        void ShouldThrowException_WhenTaskId_isNull() {
            try {
                // подготовка DTO для обновления с id = null
                TaskUpdateDTO taskUpdateDTO = TaskUpdateDTO.builder().id(null).build();

                taskService.update(taskUpdateDTO);
                Assertions.fail("Should thrown exception on null id");
            } catch (BadRequestAlertException e) {
                assertTrue(true);
            }
        }

        @Test
        @Order(2)
        void ShouldThrowException_WhenNotExistingTask_InDB() {
            try {
                // подготовка DTO для обновления с несуществующим ID в БД
                TaskUpdateDTO taskUpdateDTO = TaskUpdateDTO.builder().id(JsonNullable.of(1L)).build();
                // ищем в бд и не находим
                Mockito.when(taskRepository.findById(taskUpdateDTO.getId().get())).thenReturn(Optional.empty());

                taskService.update(taskUpdateDTO);//
                Assertions.fail("Should thrown exception, task does not exist in DB");
            } catch (BadRequestAlertException e) {
                assertTrue(true);
            }
        }

        @Test
        @Order(3)
        void ShouldThrowException_WhenCurrentUserIsNotAuthor() {
            // подготовка пользователя для проверки условия на автора
            User currentUser = User.builder().id(1L).build();
            // подготовка DTO для обновления с существующим ID в БД
            TaskUpdateDTO taskUpdateDTO = TaskUpdateDTO.builder().id(JsonNullable.of(1L)).build();
            // подготвока существующей таски с БД
            Task existedTask = Task.builder().id(1L).author(2l).build(); // ID не совпадает
            // ищем в бд и находим
            Mockito.when(taskRepository.findById(taskUpdateDTO.getId().get())).thenReturn(Optional.of(existedTask));
            // получаем текущего пользователя
            Mockito.when(userService.getCurrentUser()).thenReturn(currentUser);

            Exception e = assertThrows(BadRequestAlertException.class, () -> taskService.update(taskUpdateDTO));

            assertThat(e.getMessage()).isEqualTo("You have no permission to edit this task");
        }

        @Test
        @Order(4)
        void IfExistingExecutorSaveInDB() {
            // подготовка пользователя для проверки условия на автора
            User currentUser = User.builder().id(1L).build();
            // подготовка DTO для обновления с существующим ID в БД
            TaskUpdateDTO taskUpdateDTO = TaskUpdateDTO.builder().id(JsonNullable.of(1L))
                    .title(JsonNullable.of("do chores")).build();
            // подготовка существующей таски с БД
            Task existedTask = Task.builder().id(1L).author(1l).build();  // ID совпадает
            // обновленная таска
            Task updatedTaskFromDB = Task.builder().id(1L).author(1l).title("do chores").build();
            // DTO на контроллер
            TaskDTO taskDTOFromDB = TaskDTO.builder().id(1L).author(1l).title("do chores").build();

            // ищем в бд и находим
            Mockito.when(taskRepository.findById(taskUpdateDTO.getId().get())).thenReturn(Optional.of(existedTask));
            // получаем текущего пользователя
            Mockito.when(userService.getCurrentUser()).thenReturn(currentUser);
            // мапим данные DTO для обновления в Entity
            taskMapper.update(taskUpdateDTO, existedTask);
            //сохраняем в БД
            Mockito.when(taskRepository.save(existedTask)).thenReturn(updatedTaskFromDB);
            // мапим Entity в DTO для ответа
            Mockito.when(taskMapper.map(updatedTaskFromDB)).thenReturn(taskDTOFromDB);

            ResponseEntity<TaskDTO> actualResult = taskService.update(taskUpdateDTO);

            assertAll(
                    () -> assertThat(actualResult.getBody().getTitle()).isNotEmpty(),
                    () -> Mockito.verify(taskRepository, Mockito.atMost(2)).findAll());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "getTaskById")
    class getTaskByID {
        @Test
        @Order(1)
        void shouldReturnTheTaskIfExist() {
            // ID для поиска существующей таски
            Long id = 1L;
            // DTO для ответа
            TaskDTO taskDTOFromDB = TaskDTO.builder().id(1L).author(1l).title("do chores").build();
            // существующая таска из БД
            Task existedTask = Task.builder().id(1L).author(1l).title("do chores").build();

            // находим таску
            Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(existedTask));
            // мапим таску в DTO
            Mockito.when(taskMapper.map(existedTask)).thenReturn(taskDTOFromDB);

            ResponseEntity<TaskDTO> actualResult = taskService.getTaskById(id);

            assertThat(actualResult.getStatusCode().value()).isEqualTo(200);
        }

        @Test
        @Order(2)
        void shouldReturnTheTask_withStatus_NotFound() {
            //ID не существующей таски
            Long id = 1L;
            // пытаемся найти в БД таску по ID
            Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());

            Exception e = assertThrows(BadRequestAlertException.class, () -> taskService.getTaskById(id));

            assertThat(e.getMessage()).isEqualTo("The task was not found");
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "getAllTasks")
    class getAllTasks {
        @Test
        @Order(1)
        void shouldReturnTasks() {
            //подготовка листа с тасками
            Task task1 = Task.builder().id(1L).author(1l).title("do chores").build();
            Task task2 = Task.builder().id(2L).author(1l).title("wash the dishes").build();
            List<Task> taskList = List.of(task1, task2);

            // получаем список тасок
            Mockito.when(taskRepository.findAll())
                    .thenReturn(taskList);
            // мапим
            List<TaskDTO> expectedResult = taskList.stream().map(x -> taskMapper.map(x)).collect(Collectors.toList());


            List<TaskDTO> actualResult = taskService.getAllTasks();
            assertAll(
                    () -> assertThat(actualResult).hasSize(2),
                    () -> assertThat(actualResult).containsAll(expectedResult),
                    () -> Mockito.verify(taskRepository, Mockito.atMost(1)).findAll());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "deleteTask")
    class deleteTask {
        @Test
        @Order(1)
        void shouldThrowException_IfTaskNotExists() {
            // подготовка ID  несуществующей таски в БД
            Long id = 1L;
            Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());

            Exception e = assertThrows(BadRequestAlertException.class, () -> taskService.delete(id));

            assertThat(e.getMessage()).isEqualTo("Such doesn't exist task with this id");
        }

        @Test
        @Order(2)
        void shouldThrowException_IfUserNotAuthor() {
            // подготовка ID  несуществующей таски в БД
            Long id = 1L;
            // подготовка юзера для проверки условия
            User currentUser = User.builder().id(2L).build(); // у текушего юзера ID = 2;
            // подготовка существующей таски с БД
            Task existedTask = Task.builder().id(1L).author(1L).build();  // у таски в БД ID=1, не совпадает

            Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(existedTask));
            Mockito.doReturn(currentUser).when(userService).getCurrentUser();

            Exception e = assertThrows(BadRequestAlertException.class, () -> taskService.delete(id));

            assertThat(e.getMessage()).isEqualTo("You have no permission to delete this task");
        }

        @Test
        @Order(3)
        void deleteTaskAndReturnResponseNoContent() {
            // подготовка ID  несуществующей таски в БД
            Long id = 1L;
            // подготовка юзера для проверки условия
            User currentUser = User.builder().id(1L).build(); // у текушего юзера ID = 1;
            // подготовка существующей таски с БД
            Task existedTask = Task.builder().id(1L).author(1L).build();  // у таски в БД ID=1, совпадают

            Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(existedTask));
            Mockito.doReturn(currentUser).when(userService).getCurrentUser();

            ResponseEntity<Void> actualResult = taskService.delete(id);

            assertAll(
                    () -> assertThat(actualResult.getHeaders()).isNotEmpty(),
                    () -> assertThat(actualResult.getBody()).isNull(),
                    () -> assertThat(actualResult.getStatusCode().value()).isEqualTo(204));
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "changeStatus")
    class changeStatus {
        @Test
        @Order(1)
        void shouldThrowException_IfTaskNotExists() {
            // подготовка ID  несуществующей таски в БД
            Long id = 1L;
            // подготовка DTO для изменения статуса
            TaskStatusDTO taskDTO = TaskStatusDTO.builder().status(Status.DONE).build();


            Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());

            Exception e = assertThrows(BadRequestAlertException.class, () -> taskService.changeStatus(id, taskDTO));

            assertThat(e.getMessage()).isEqualTo("An existing task should have an id");
        }

        @Test
        @Order(2)
        void shouldThrowException_IfUserNotAuthor() {
            // подготовка ID существующей таски в БД
            Long id = 1L;
            // подготовка DTO для изменения статуса
            TaskStatusDTO taskDTO = TaskStatusDTO.builder().status(Status.DONE).build();
            // подготовка существующей таски с БД
            Task existedTask = Task.builder().id(1L).status(Status.IN_PROGRESS).author(2L).build();  // у существующей таски автора ID=2
            // подготовка юзера для проверки условия
            User currentUser = User.builder().id(1L).build(); // у текушего юзера ID = 1;


            Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(existedTask));
            Mockito.doReturn(currentUser).when(userService).getCurrentUser();

            Exception e = assertThrows(BadRequestAlertException.class, () -> taskService.changeStatus(id, taskDTO));

            assertThat(e.getMessage()).isEqualTo("You have no permission to change the status of this task");
        }

        @Test()
        @Order(3)
        void changeStatusAndReturnResponse_Ok() {
            // подготовка ID существующей таски в БД
            Long id = 1L;
            // подготовка DTO для изменения статуса
            TaskStatusDTO taskUpdate = TaskStatusDTO.builder().status(Status.DONE).build();
            // подготовка DTO для ответа
            TaskDTO taskDTO = TaskDTO.builder().status(Status.DONE).build();
            // подготовка существующей таски с БД
            Task existedTask = Task.builder().id(1L).status(Status.IN_PROGRESS).author(1L).build();  // проверку на автора проходит
            // подготовка юзера для проверки условия
            User currentUser = User.builder().id(1L).build(); // у текушего юзера ID = 1;


            Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(existedTask));
            Mockito.when(userService.getCurrentUser()).thenReturn(currentUser);

            Mockito.when(taskRepository.save(existedTask)).thenReturn(existedTask);
            Mockito.when(taskMapper.map(existedTask)).thenReturn(taskDTO);

            var actualResult = taskService.changeStatus(id, taskUpdate);
            actualResult.getHeaders();
            assertAll(
                    () -> assertThat(Objects.requireNonNull(actualResult.getBody()).getStatus()).isEqualTo(taskUpdate.getStatus()),
                    () -> assertThat(actualResult.getStatusCode().value()).isEqualTo(200));
        }

    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "changeExecutor")
    class changeExecutor {
        @Test
        @Order(1)
        void shouldThrowException_IfTaskNotExists() {
            // подготовка ID  несуществующей таски в БД
            Long id = 1L;
            // подготовка DTO для изменения статуса
            TaskExecutorDTO taskDTO = TaskExecutorDTO.builder().build();


            Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());

            Exception e = assertThrows(BadRequestAlertException.class, () -> taskService.changeExecutor(id, taskDTO));

            assertThat(e.getMessage()).isEqualTo("An existing task should have an id");
        }
    }

    @Test
    @Order(2)
    void shouldThrowException_IfUserNotAuthor() {
        // подготовка ID существующей таски в БД
        Long id = 1L;
        // подготовка DTO для изменения статуса
        TaskExecutorDTO taskDTO = TaskExecutorDTO.builder().build();
        // подготовка существующей таски с БД
        Task existedTask = Task.builder().id(1L).status(Status.IN_PROGRESS).author(2L).build();  // у существующей таски автора ID=2
        // подготовка юзера для проверки условия
        User currentUser = User.builder().id(1L).build(); // у текушего юзера ID = 1;

        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(existedTask));
        Mockito.doReturn(currentUser).when(userService).getCurrentUser();

        Exception e = assertThrows(BadRequestAlertException.class, () -> taskService.changeExecutor(id, taskDTO));

        assertThat(e.getMessage()).isEqualTo("You have no permission to change the executor for this task");
    }

    @Test()
    @Order(3)
    void changeExecutorAndReturnResponse_Ok() {
        // подготовка ID существующей таски в БД
        Long id = 1L;
        // подготовка DTO для изменения статуса
        TaskExecutorDTO taskUpdate = TaskExecutorDTO.builder().executor(2L).build(); // хотим обновить исполнителя на "2"
        // подготовка DTO для ответа
        TaskDTO taskDTO = TaskDTO.builder().executor(2L).build();
        // подготовка существующей таски с БД
        Task existedTask = Task.builder().id(1L).author(1L).build();  // проверку на автора проходит
        // подготовка юзера для проверки условия
        User currentUser = User.builder().id(1L).build(); // у текушего юзера ID = 1;


        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(existedTask));
        Mockito.when(userService.getCurrentUser()).thenReturn(currentUser);

        Mockito.when(taskRepository.save(existedTask)).thenReturn(existedTask);
        Mockito.when(taskMapper.map(existedTask)).thenReturn(taskDTO);

        var actualResult = taskService.changeExecutor(id, taskUpdate);
        actualResult.getHeaders();
        assertAll(
                () -> assertThat(Objects.requireNonNull(actualResult.getBody()).getExecutor()).isEqualTo(taskUpdate.getExecutor()),
                () -> assertThat(actualResult.getStatusCode().value()).isEqualTo(200));

    }


    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "createComment")
    class createComment {
        @Test
        @Order(1)
        void shouldThrowException_IfTaskNotExists() {
            // подготовка ID несуществующей таски в БД
            Long id = 1L;
            //подготовка DTO для создания комментария
            CommentCreateDTO comment = CommentCreateDTO.builder().build();

            Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty()); // ищем несуществую таску

            Exception e = assertThrows(BadRequestAlertException.class, () -> taskService.createComment(12L, comment));

            assertThat(e.getMessage()).isEqualTo("An existing task should have an id");
        }

        @Test
        @Order(2)
        void shouldSaveCommentAndResponseOK() {
            // подготовка ID несуществующей таски в БД
            Long id = 1L;
            //подготовка DTO для создания комментария
            CommentCreateDTO commentCreateDTO = CommentCreateDTO.builder().description("test code").build();
            // подготовка comment entity
            Comment comment = Comment.builder().description("test code").build();
            //
            CommentDTO commentDTO = CommentDTO.builder().description("test code").build();
            // таска существующая в БД
            Task task = Task.builder().id(id).build();
            // подготовка юзера
            User currentUser = User.builder().id(1L).build(); // у текушего юзера ID = 1;

            Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(task)); // находим существующую таску
            Mockito.when(userService.getCurrentUser()).thenReturn(currentUser);
            // мапим до entity
            Mockito.when(commentMapper.map(commentCreateDTO)).thenReturn(comment);
            // устаналиваем автора и задачу
            comment.setAuthorId(id);
            comment.setTask(task);
            // добавляем в таску
            task.addComment(comment);

            Mockito.when(taskRepository.save(task)).thenReturn(task);

            Comment commentFromDB = task.getCommentList().get(task.getCommentList().size() - 1);
            Mockito.when(commentMapper.map(commentFromDB)).thenReturn(commentDTO);

            ResponseEntity<CommentDTO> actualResult = taskService.createComment(id, commentCreateDTO);


            assertAll(
                    () -> assertThat(actualResult.getStatusCode().value()).isEqualTo(200),
                    () -> assertThat(actualResult.getBody().getDescription()).isEqualTo("test code"));
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "getTasksByAuthorFilteringAndPaging")
    class getTasksByAuthorFilteringAndPaging {
        @Test
        @Order(1)
        void shouldPagingByDefaultValues() {
            //подготовка дефолтных значений для пагинации
            int pageNumber = 0;
            int pageSize = 4;
            // подготовка id автора
            Long id = 1L;

            Map<String, String> requestFiltration = prepareData();
            // удаляем настраиваемую пагинацию для тест
            requestFiltration.remove("pageSize");
            requestFiltration.remove("pageNumber");

            // подготовка Entity списка
            List<Task> taskList = new ArrayList<>();
            taskList.add(getTask());
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Task> expectedResult = new PageImpl<>(taskList, pageable, taskList.size());
            // подготовка DTO списка
            List<TaskFilterDTO> taskListDTO = new ArrayList<>();
            taskListDTO.add(getTaskDTO());
            Pageable pageableDTO = PageRequest.of(pageNumber, pageSize);
            Page<TaskFilterDTO> expectedResultDTO = new PageImpl<>(taskListDTO, pageableDTO, taskListDTO.size());

            Mockito.doReturn(expectedResult).when(taskRepository).findAll(refEq(Specification.allOf(likeAuthorId(id))),
                    refEq(PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "id")));

            Mockito.when(expectedResult.map(taskMapper::map)).thenAnswer(x -> expectedResultDTO);

            Page<TaskFilterDTO> actualResult = taskService.getTasksByAuthorFilteringAndPaging(id, requestFiltration);

            assertAll(
                    () -> assertThat(actualResult.getTotalElements()).isEqualTo(expectedResultDTO.getTotalElements()),
                    () -> Mockito.verify(taskMapper, Mockito.times(1)).mapFilter(Mockito.any()),
                    () -> Mockito.verify(taskRepository, Mockito.atLeastOnce()).findAll(refEq(Specification.allOf(likeAuthorId(id))),
                            refEq(PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "id"))));
        }

        @Test
        @Order(2)
        void shouldPagingByInputValues() {
            //подготовка дефолтных значений для пагинации
            int pageNumber;
            int pageSize;
            // подготовка id автора
            Long id = 1L;

            // в даной хешмапе есть ключи со значениями для пагинации
            Map<String, String> requestFiltration = prepareData();
            // получаем их
            pageSize = Integer.parseInt(requestFiltration.get("pageSize"));
            pageNumber = Integer.parseInt(requestFiltration.get("pageNumber"));

            // подготовка Entity списка
            List<Task> taskList = new ArrayList<>();
            taskList.add(getTask());
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Task> expectedResult = new PageImpl<>(taskList, pageable, taskList.size());
            // подготовка DTO списка
            List<TaskFilterDTO> taskListDTO = new ArrayList<>();
            taskListDTO.add(getTaskDTO());
            Pageable pageableDTO = PageRequest.of(pageNumber, pageSize);
            Page<TaskFilterDTO> expectedResultDTO = new PageImpl<>(taskListDTO, pageableDTO, taskListDTO.size());

            Mockito.doReturn(expectedResult).when(taskRepository).findAll(refEq(Specification.allOf(likeAuthorId(id))),
                    refEq(PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "id")));

            Mockito.when(expectedResult.map(taskMapper::map)).thenAnswer(x -> expectedResultDTO);

            Page<TaskFilterDTO> actualResult = taskService.getTasksByAuthorFilteringAndPaging(id, requestFiltration);

            assertAll(
                    () -> assertThat(actualResult.getTotalElements()).isEqualTo(expectedResultDTO.getTotalElements()),
                    () -> Mockito.verify(taskMapper, Mockito.times(1)).mapFilter(Mockito.any()),
                    () -> Mockito.verify(taskRepository, Mockito.atLeastOnce()).findAll(refEq(Specification.allOf(likeAuthorId(id))),
                            refEq(PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "id"))));
        }

        private TaskFilterDTO getTaskDTO() {
            return TaskFilterDTO.builder().id(1L).author(1L).title("mop the floor").build();
        }

        private Task getTask() {
            return Task.builder().id(1L).author(1L).title("mop the floor").build();
        }

        private Map<String, String> prepareData() {
            Map<String, String> requestFiltration = new HashMap<>();
            requestFiltration.put("priority", "LOW");
            requestFiltration.put("status", "TODO");
            requestFiltration.put("title", "do chores");
            requestFiltration.put("description", "mop the floor");
            requestFiltration.put("pageNumber", "1");
            requestFiltration.put("pageSize", "2");
            return requestFiltration;
        }
    }


    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "getTasksByExecutorFilteringAndPaging")
    class getTasksByExecutorFilteringAndPaging {
        @Test
        @Order(1)
        void shouldPagingByDefaultValues() {
            //подготовка дефолтных значений для пагинации
            int pageNumber = 0;
            int pageSize = 4;
            // подготовка id исполнителя
            Long id = 1L;

            Map<String, String> requestFiltration = prepareData();
            // удаляем настраиваемую пагинацию для тест
            requestFiltration.remove("pageSize");
            requestFiltration.remove("pageNumber");

            // подготовка Entity списка
            List<Task> taskList = new ArrayList<>();
            taskList.add(getTask());
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Task> expectedResult = new PageImpl<>(taskList, pageable, taskList.size());
            // подготовка DTO списка
            List<TaskFilterDTO> taskListDTO = new ArrayList<>();
            taskListDTO.add(getTaskDTO());
            Pageable pageableDTO = PageRequest.of(pageNumber, pageSize);
            Page<TaskFilterDTO> expectedResultDTO = new PageImpl<>(taskListDTO, pageableDTO, taskListDTO.size());

            Mockito.doReturn(expectedResult).when(taskRepository).findAll(refEq(Specification.allOf(likeExecutorId(id))),
                    refEq(PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "id")));

            Mockito.when(expectedResult.map(taskMapper::map)).thenAnswer(x -> expectedResultDTO);

            Page<TaskFilterDTO> actualResult = taskService.getTasksByAuthorFilteringAndPaging(id, requestFiltration);

            assertAll(
                    () -> assertThat(actualResult.getTotalElements()).isEqualTo(expectedResultDTO.getTotalElements()),
                    () -> Mockito.verify(taskMapper, Mockito.times(1)).mapFilter(Mockito.any()),
                    () -> Mockito.verify(taskRepository, Mockito.atLeastOnce()).findAll(refEq(Specification.allOf(likeExecutorId(id))),
                            refEq(PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "id"))));
        }

        @Test
        @Order(2)
        void shouldPagingByInputValues() {
            //подготовка дефолтных значений для пагинации
            int pageNumber;
            int pageSize;
            // подготовка id автора
            Long id = 1L;

            // в даной хешмапе есть ключи со значениями для пагинации
            Map<String, String> requestFiltration = prepareData();
            // получаем их
            pageSize = Integer.parseInt(requestFiltration.get("pageSize"));
            pageNumber = Integer.parseInt(requestFiltration.get("pageNumber"));

            // подготовка Entity списка
            List<Task> taskList = new ArrayList<>();
            taskList.add(getTask());
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Task> expectedResult = new PageImpl<>(taskList, pageable, taskList.size());
            // подготовка DTO списка
            List<TaskFilterDTO> taskListDTO = new ArrayList<>();
            taskListDTO.add(getTaskDTO());
            Pageable pageableDTO = PageRequest.of(pageNumber, pageSize);
            Page<TaskFilterDTO> expectedResultDTO = new PageImpl<>(taskListDTO, pageableDTO, taskListDTO.size());

            Mockito.doReturn(expectedResult).when(taskRepository).findAll(refEq(Specification.allOf(likeExecutorId(id))),
                    refEq(PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "id")));

            Mockito.when(expectedResult.map(taskMapper::map)).thenAnswer(x -> expectedResultDTO);

            Page<TaskFilterDTO> actualResult = taskService.getTasksByAuthorFilteringAndPaging(id, requestFiltration);

            assertAll(
                    () -> assertThat(actualResult.getTotalElements()).isEqualTo(expectedResultDTO.getTotalElements()),
                    () -> Mockito.verify(taskMapper, Mockito.times(1)).mapFilter(Mockito.any()),
                    () -> Mockito.verify(taskRepository, Mockito.atLeastOnce()).findAll(refEq(Specification.allOf(likeExecutorId(id))),
                            refEq(PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "id"))));
        }

        private TaskFilterDTO getTaskDTO() {
            return TaskFilterDTO.builder().id(1L).author(1L).title("mop the floor").build();
        }

        private Task getTask() {
            return Task.builder().id(1L).author(1L).title("mop the floor").build();
        }
        private Map<String, String> prepareData() {
            Map<String, String> requestFiltration = new HashMap<>();
            requestFiltration.put("priority", "LOW");
            requestFiltration.put("status", "TODO");
            requestFiltration.put("title", "do chores");
            requestFiltration.put("description", "mop the floor");
            requestFiltration.put("pageNumber", "1");
            requestFiltration.put("pageSize", "2");
            return requestFiltration;
        }
    }
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "getAllTasksByExecutor")
    class getAllTasksByExecutor {
        @Test
        @Order(1)
        void shouldReturnTheListOfTask() {
            // подготовка данных
            Long id = 1L;
            List<Task> taskList = List.of(getTask());

            Mockito.when(taskRepository.findAllByAuthor(id)).thenReturn(taskList);
            taskList.stream().map(taskMapper::map).collect(Collectors.toList());

            List<TaskDTO> actualResult = taskService.getAllTasksByAuthor(id);
            assertThat(actualResult.size()).isEqualTo(taskList.size());
        }

        private Task getTask() {
            return Task.builder().id(1L).title("chores").description("mop the floor").priority(Priority.LOW).status(Status.DONE).build();
        }
    }
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag(value = "getAllTasksByAuthor")
    class getAllTasksByAuthor {
        @Test
        @Order(1)
        void shouldReturnTheListOfTask() {
            // подготовка данных
            Long id = 1L;
            List<Task> taskList = List.of(getTask());

            Mockito.when(taskRepository.findAllByAuthor(id)).thenReturn(taskList);
            taskList.stream().map(taskMapper::map).collect(Collectors.toList());

            List<TaskDTO> actualResult = taskService.getAllTasksByAuthor(id);
            assertThat(actualResult.size()).isEqualTo(taskList.size());
        }
        private Task getTask() {
            return Task.builder().id(1L).executor(1L).title("chores").description("mop the floor").priority(Priority.LOW).status(Status.DONE).build();
        }
    }
}



