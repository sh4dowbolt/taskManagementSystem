package com.suraev.TaskManagementSystem.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Schema(description = "Сущность комментария")
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор комментария", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "description")
    @Schema(description = "Содержимое комментария")
    @NotBlank
    private String description;
    @Column
    @Schema(description = "Автор комментария")
    private Long authorId;

    @Schema(description = "Идентификатор задачи", accessMode = Schema.AccessMode.READ_ONLY)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name ="task_id", referencedColumnName = "id")
    @JsonBackReference
    private Task task;
}
