package com.suraev.TaskManagementSystem.repository;

import com.suraev.TaskManagementSystem.domain.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>,
        JpaSpecificationExecutor <Task>,
        PagingAndSortingRepository<Task, Long> {

    Page<Task> findAll(Specification<Task> spec, Pageable pageable);
    List<Task> findAllByExecutor(Long id);
    List<Task> findAllByAuthor(Long id);
}
