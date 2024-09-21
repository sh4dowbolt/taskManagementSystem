package com.suraev.TaskManagementSystem.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.suraev.TaskManagementSystem.domain.entity.enums.Priority;
import com.suraev.TaskManagementSystem.domain.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность задачи")
public class Task {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор задачи", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "title")
    @Schema(description = "Наименование задачи", example = "Do chores")
    private String title;


    @Column(name = "description")
    @Schema(description = "Описание задачи", example = "Mop the floor")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    @Schema(description = "Приоритет задачи", example = "LOW or MEDIUM or HIGH")
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Schema(description = "Статус задачи", example ="TODO or IN_PROGRESS or DONE or CLOSED")
    private Status status;

    @Column(name = "author_id")
    @Schema(description = "Идентификатор автора задачи")
    public Long author;

    @Column(name ="executor_id")
    @Schema(description = "Идентификатор исполнителя задачи")
    public Long executor;


    @OneToMany(mappedBy = "task", fetch =  FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Schema(description = "Список комментариев задачи")
    private List<Comment> commentList;

    public void addComment(Comment comment) {
        if(commentList==null) {
            commentList = new ArrayList<>();
        }
        commentList.add(comment);
    }
}
