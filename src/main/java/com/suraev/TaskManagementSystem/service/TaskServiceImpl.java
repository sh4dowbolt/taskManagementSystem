package com.suraev.TaskManagementSystem.service;

import com.suraev.TaskManagementSystem.domain.entity.Comment;
import com.suraev.TaskManagementSystem.domain.entity.Task;
import com.suraev.TaskManagementSystem.domain.mapper.CommentMapper;
import com.suraev.TaskManagementSystem.domain.mapper.TaskMapper;
import com.suraev.TaskManagementSystem.dto.*;
import com.suraev.TaskManagementSystem.exception.BadRequestAlertException;
import com.suraev.TaskManagementSystem.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.suraev.TaskManagementSystem.util.SpecificationUtil.*;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{
    @Value("${spring.application.name}")
    private String applicationName;
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskMapper taskMapper;
    private final CommentMapper commentMapper;
    private Long getCurrentUserId() {
        return userService.getCurrentUser().getId();
    }


    @SneakyThrows
    @Transactional
    public ResponseEntity<TaskDTO> create(TaskCreateDto taskData) {
        HttpHeaders headers = new HttpHeaders();

        var task = taskMapper.map(taskData);
        // Устаналиваем автором текущего авторизованного пользователя(указываем его ID)
        task.setAuthor(getCurrentUserId());
       /* Допускаем возможность, что пользователь решит создать таску без указания исполнителя,
        в таком случае сохраняем тачку без исполнителя.
        Если пользователь указывает ID исполнителя, делаем проверку на наличие исполнителя в БД,
        если исполнитель существует в БД, сохраняем таску с ID исполнителя.
        В ином случае, сохраняем без ID исполнителя во избежании указания не существующего пользователя.*/
        if(task.getExecutor() !=null) {
            if(!taskRepository.existsById(task.getExecutor())) {
                task.setExecutor(null);
                var result = taskRepository.save(task);
                var taskDto = taskMapper.map(result);
                String taskId = result.getId().toString();
                String message = String.format("A new task without executor is created with identifier %s", result.getId());
                headers.add("X-" + applicationName + "-alert", message);
                headers.add("X-" + applicationName + "-params", taskId);
                return ResponseEntity.created(new URI("/api/task" + result.getId())).headers(headers).body(taskDto);
            }
        }
        var result = taskRepository.save(task);
        var taskDto = taskMapper.map(result);
        String taskId = result.getId().toString();
        String message = String.format("A new task is created with identifier %s", result.getId());
        headers.add("X-" + applicationName + "-alert", message);
        headers.add("X-" + applicationName + "-params", taskId);
        return ResponseEntity.created(new URI("/api/task" + result.getId())).headers(headers).body(taskDto);
    }

    @Transactional
    public ResponseEntity<TaskDTO> update(TaskUpdateDTO taskData) {
        HttpHeaders headers = new HttpHeaders();
        if (taskData.getId() == null) {
            throw new BadRequestAlertException("An existing task should have an id", Status.CONFLICT);
        }
        Task existedTask = taskRepository.findById(taskData.getId().get()).orElseThrow(
                () -> new BadRequestAlertException("Such task with this id doesn't exist", Status.CONFLICT));

        if (getCurrentUserId().equals(existedTask.getAuthor())) {
            taskMapper.update(taskData,existedTask);
            var result = taskRepository.save(existedTask);
            var taskDto = taskMapper.map(result);

            String taskId = result.getId().toString();
            String message = String.format("A task is updated with identifier %s", result.getId());
            headers.add("X-" + applicationName + "-alert", message);
            headers.add("X-" + applicationName + "-params", taskId);
            return ResponseEntity.ok().headers(headers).body(taskDto);
        }
            throw new BadRequestAlertException("You have no permission to edit this task", Status.CONFLICT);
    }
    @Transactional
    public ResponseEntity<TaskDTO> getTaskById(Long id) {
        var task = taskRepository.findById(id);
        if (task.isPresent()) {
            TaskDTO taskDTO = taskMapper.map(task.get());
            return ResponseEntity.ok().body(taskDTO);
        }
        throw new BadRequestAlertException("The task was not found", Status.NOT_FOUND);
    }
    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasks() {
        List<Task> model = taskRepository.findAll();

        return model.stream().map(taskMapper::map).collect(Collectors.toList());
    }
    @Transactional
    public ResponseEntity<Void> delete(Long id) {
        HttpHeaders headers = new HttpHeaders();

        Task task = taskRepository.findById(id).orElseThrow(
                () -> new BadRequestAlertException("Such doesn't exist task with this id", Status.CONFLICT));

        if (getCurrentUserId().equals(task.getAuthor())) {
            taskRepository.deleteById(id);

            String message = String.format("A task is deleted with identifier %s", id);
            headers.add("X-" + applicationName + "-alert", message);

            return ResponseEntity.noContent().headers(headers).build();
        } else {
            throw new BadRequestAlertException("You have no permission to delete this task", Status.CONFLICT);
        }
    }

    @Transactional
    public ResponseEntity<TaskDTO> changeStatus(Long id, TaskStatusDTO updatedTask) {
        HttpHeaders headers = new HttpHeaders();

        Task existedTask = taskRepository.findById(id).orElseThrow(() -> new BadRequestAlertException("An existing task should have an id", Status.CONFLICT));

        if (Objects.equals(getCurrentUserId(), existedTask.getAuthor()) || Objects.equals(getCurrentUserId(), existedTask.getExecutor())) {
            existedTask.setStatus(updatedTask.getStatus());
            var result = taskRepository.save(existedTask);
            TaskDTO taskDTO = taskMapper.map(result);

            String taskId = result.getId().toString();
            String message = String.format("Set the new status for the task with identifier %s", result.getId());
            headers.add("X-" + applicationName + "-alert", message);
            headers.add("X-" + applicationName + "-params", taskId);
            return ResponseEntity.ok().headers(headers).body(taskDTO);
        }
        else {
            throw new BadRequestAlertException("You have no permission to change the status of this task", Status.CONFLICT);
        }

    }
    @Transactional
    public ResponseEntity<TaskDTO> changeExecutor(Long id, TaskExecutorDTO updatedTask) {
        HttpHeaders headers = new HttpHeaders();

        Task task = taskRepository.findById(id).orElseThrow(() -> new BadRequestAlertException("An existing task should have an id", Status.CONFLICT));
        if (Objects.equals(getCurrentUserId(), task.getAuthor())) {

            task.setExecutor(updatedTask.getExecutor());

            var result = taskRepository.save(task);
            TaskDTO taskDTO = taskMapper.map(result);

            String taskId = result.getId().toString();
            String message = String.format("Set the new executor with identifier %s for the task with identifier %s",
                    result.getExecutor(), result.getId());
            headers.add("X-" + applicationName + "-alert", message);
            headers.add("X-" + applicationName + "-params", taskId);

            return ResponseEntity.ok().headers(headers).body(taskDTO);
        }
        else
            throw new BadRequestAlertException("You have no permission to change the executor for this task", Status.CONFLICT);
    }

    @Transactional
    public ResponseEntity<CommentDTO> createComment(Long id,CommentCreateDTO comment) {
        HttpHeaders headers = new HttpHeaders();

        Task task = taskRepository.findById(id).orElseThrow(() -> new BadRequestAlertException("An existing task should have an id", Status.CONFLICT));

        Comment commentEntity = commentMapper.map(comment);
        commentEntity.setAuthorId(getCurrentUserId());
        commentEntity.setTask(task);
        task.addComment(commentEntity);

        var result = taskRepository.save(task);
        // для возврата коммента
        Comment commentFromDB = result.getCommentList().get(result.getCommentList().size() - 1);
        CommentDTO actualResult = commentMapper.map(commentFromDB);

        String taskId = result.getId().toString();
        String message = String.format("Added new comment to the task with identifier %s",id);
        headers.add("X-" + applicationName + "-alert", message);
        headers.add("X-" + applicationName + "-params", taskId);
        return ResponseEntity.ok().headers(headers).body(actualResult);
    }


    @Transactional(readOnly = true)
    public Page<TaskFilterDTO> getTasksByAuthorFilteringAndPaging(Long id, Map<String, String> requestFiltration) {
        //дефолтные настройки пагинации, если с запросом приходят данные, вносим корректировку для вывода
        int pageNumber = 0;
        int pageSize = 4;

        String priorityParam = requestFiltration.get("priority");
        String statusParam = requestFiltration.get("status");
        String titleParam = requestFiltration.get("title");
        String descriptionParam=requestFiltration.get("description");

        if (requestFiltration.containsKey("pageNumber")) {
            pageNumber = Integer.parseInt(requestFiltration.get("pageNumber"));
        }
        if (requestFiltration.containsKey("pageSize")) {
            pageSize =Integer.parseInt(requestFiltration.get("pageSize"));
        }
        Page<Task> taskEntity = taskRepository.findAll(
                Specification.allOf(likeAuthorId(id), likePriority(priorityParam), likeStatus(statusParam), likeTitle(titleParam), likeDescription(descriptionParam))
                , PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "id"));

        return taskEntity.map(taskMapper::mapFilter);

    }
    @Transactional(readOnly = true)
    public Page<TaskFilterDTO> getTasksByExecutorFilteringAndPaging(Long id, Map<String, String> requestFiltration) {
        //дефолтные настройки пагинации, если с запросом приходят данные, вносим корректировку для вывода
        int pageNumber = 0;
        int pageSize = 4;

        String priorityParam = requestFiltration.get("priority");
        String statusParam = requestFiltration.get("status");
        String titleParam = requestFiltration.get("title");
        String descriptionParam=requestFiltration.get("description");

        if (requestFiltration.containsKey("pageNumber")) {
            pageNumber = Integer.parseInt(requestFiltration.get("pageNumber"));
        }
        if (requestFiltration.containsKey("pageSize")) {
            pageSize =Integer.parseInt(requestFiltration.get("pageSize"));
        }
        return taskRepository.findAll(
                Specification.allOf(likeExecutorId(id), likePriority(priorityParam), likeStatus(statusParam), likeTitle(titleParam), likeDescription(descriptionParam))
                , PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC,"id")).map(taskMapper::mapFilter);
    }
    public List<TaskDTO> getAllTasksByExecutor(Long id) {
        List<Task> tasks = taskRepository.findAllByExecutor(id);
        return tasks.stream().map(taskMapper::map).collect(Collectors.toList());
    }
    public List<TaskDTO> getAllTasksByAuthor(Long id) {
        List<Task> tasks = taskRepository.findAllByAuthor(id);
        return  tasks.stream().map(taskMapper::map).collect(Collectors.toList());
    }
}



